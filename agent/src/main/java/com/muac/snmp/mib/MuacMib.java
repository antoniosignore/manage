package com.muac.snmp.mib;

import com.muac.beans.*;
import com.muac.snmp.ApplicationConfig;
import com.muac.snmp.utils.SnmpUtils;
import org.slf4j.LoggerFactory;
import org.snmp4j.agent.agentx.subagent.AgentXSubagent;
import org.snmp4j.agent.mo.MOFactory;
import org.snmp4j.smi.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class MuacMib extends MUACPAASMIB {

    public static final String MAX_DETAILS_SIZE = "%1.79s";
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MuacMib.class);
    @Autowired
    public ApplicationConfig config;
    @Autowired
    public
    AgentXSubagent subagent;
    public ClusterWrapper clusterWrapper;
    @Autowired
    protected RestTemplate restTemplate;
    DataStreamsTableWrapper dataStreamsTableWrapper;
    private ActualStatusEnum maxActual;
    private List<MMCBean> yellows;
    private List<MMCBean> reds;
    private List<MMCHost> inMaintenanceMode;

    public MuacMib(MOFactory moFactory) {
        super(moFactory);

        clusterWrapper = new ClusterWrapper(this);
        dataStreamsTableWrapper = new DataStreamsTableWrapper(this);

        clusterWrapper.setClusterStatus(MpaasStatusIntendedType.operational, MpaasStatusActualType.operational, "", 1);
    }

    public void getDataCenterAndUpdateMib(String url1) {
        try {
            MMCDataCenter dataCenter = this.restTemplate.getForObject(url1, MMCDataCenter.class);
            SnmpUtils.prettyPrint(dataCenter);
            updateMib(dataCenter);
        } catch (Exception e) {
            logger.warn("VCenter service not available");
        }
    }

    public void getRSyncSnapshotAndUpdateMib(String rsyncService) {
        try {
            MMCMountPoints mountPoints = this.restTemplate.getForObject(rsyncService, MMCMountPoints.class);
            SnmpUtils.prettyPrint(mountPoints);
            updateMib(mountPoints);
        } catch (Exception e) {
            logger.warn("rsync service not available");
        }
    }

    public void updateMib(MMCMountPoints mmcMountPoints) {
        List<MMCMountPoint> mountPoints = mmcMountPoints.getMmcMountPoints();
        for (int j = 0; j < mountPoints.size(); j++) {
            MMCMountPoint mmcMountPoint = mountPoints.get(j);
            processMountPoint(mmcMountPoint);
        }
    }


    public void updateMib(MMCDataCenter dataCenter) {

        clearGlobals();
        List<MMCHost> nodes = new ArrayList<>();

        maxActual = updateMaxStatus(maxActual, dataCenter);

        List<MMCDatastore> datastores = dataCenter.getDatastores();
        for (int i = 0; i < datastores.size(); i++) {
            MMCDatastore mmcDatastore = datastores.get(i);
            maxActual = updateMaxStatus(maxActual, mmcDatastore);
        }

        List<MMCCluster> clusters = dataCenter.getClusters();
        for (int i = 0; i < clusters.size(); i++) {
            MMCCluster mmcCluster = clusters.get(i);
            maxActual = updateMaxStatus(maxActual, mmcCluster);
            nodes = mmcCluster.getNodes();
            for (int j = 0; j < nodes.size(); j++) {
                MMCHost mmcHost = nodes.get(j);
                if (mmcHost.isInMaintenanceMode()) {
                    inMaintenanceMode.add(mmcHost);
                    continue;
                }
                maxActual = updateMaxStatus(maxActual, mmcHost);
            }

            List<MMCHost> hosts = mmcCluster.getNodes();
            for (int j = 0; j < hosts.size(); j++) {
                MMCHost mmcHost = hosts.get(j);
                List<MMCVm> vms = mmcHost.getVms();
                for (int k = 0; k < vms.size(); k++) {
                    MMCVm vm = vms.get(k);
                    processVM(vm);
                }
            }
        }

        if (maxActual == ActualStatusEnum.RED) {
            setStatusValueAndSendTrap(
                    MpaasStatusIntendedType.operational,
                    MpaasStatusActualType.major,
                    getDetailsMessage(reds),
                    1);
        }

        // case 3/4 at least one yellow
        else if (maxActual == ActualStatusEnum.YELLOW) {
            setStatusValueAndSendTrap(
                    MpaasStatusIntendedType.operational,
                    MpaasStatusActualType.warning,
                    getDetailsMessage(yellows),
                    1);
        }

        // all good - except maintenance hosts
        else if (maxActual == ActualStatusEnum.GREEN) {

            if (inMaintenanceMode.size() >= 1) {

                int actualtype = MpaasStatusActualType.warning;
                this.getMpaasClusterStatusAct().setValue(new Integer32(actualtype));

                if (inMaintenanceMode.size() == nodes.size()) {

                    // all nodes are in maintenance mode --> it was intended not to be operational
                    String detail = "All Nodes of the cluster are in maintenance mode";

                    setStatusValueAndSendTrap(MpaasStatusIntendedType.nonOperational, MpaasStatusActualType.nonOperational, detail, 1);

                } else {

                    // not all all nodes are in maintenance mode
                    String detail = getMaintenanceMessage(inMaintenanceMode);
                    setStatusValueAndSendTrap(MpaasStatusIntendedType.operational, MpaasStatusActualType.warning, detail, 1);

                }

            } else
                setStatusValueAndSendTrap(MpaasStatusIntendedType.operational,
                        MpaasStatusActualType.operational, "", 1);

        }
    }

    private void clearGlobals() {
        maxActual = ActualStatusEnum.GREEN;
        yellows = new ArrayList<>();
        reds = new ArrayList<>();
        inMaintenanceMode = new ArrayList<>();
    }

    private ActualStatusEnum updateMaxStatus(ActualStatusEnum maxActual, MMCBean bean) {
        normalizeGrayToGreen(bean);
        int clusterStatus = Enum.valueOf(ActualStatusEnum.class, bean.getStatus().toUpperCase()).value;
        if (clusterStatus >= maxActual.value) {
            maxActual = Enum.valueOf(ActualStatusEnum.class, bean.getStatus().toUpperCase());
            if (maxActual == ActualStatusEnum.RED) reds.add(bean);
            if (maxActual == ActualStatusEnum.YELLOW) yellows.add(bean);
        }
        return maxActual;
    }

    private void normalizeGrayToGreen(MMCBean folder) {
        if (folder.getStatus().equalsIgnoreCase("gray"))
            folder.setStatus("green");
    }

    private void sendVMTrap(int intendedType, int actualType, String detail, int reacheable) {
        // send trap non
        VariableBinding[] payload = new VariableBinding[4];
        payload[0] = new VariableBinding(oidTrapVarMpaasClusterStatusInt, new Integer32(intendedType));
        payload[1] = new VariableBinding(oidTrapVarMpaasClusterStatusAct, new Integer32(actualType));
        payload[2] = new VariableBinding(oidTrapVarMpaasClusterStatusDetails, new OctetString(String.format(MAX_DETAILS_SIZE, detail)));
        payload[3] = new VariableBinding(oidTrapVarMpaasClusterIsReachable, new Integer32(reacheable));

        mpaasClusterStatusTrap(subagent, null, payload);
    }

    private void setStatusValueAndSendTrap(int intended, int actual, String detailedMessage, int reacheable) {
        Integer32 currentActualStatus = this.getMpaasClusterStatusAct().getValue();
        OctetString currentDetail = this.getMpaasClusterStatusDetails().getValue();
        Integer32 currentReachable = this.getMpaasClusterIsReachable().getValue();

        // send trap only if there is a change of any of the 3 values
        if (currentActualStatus.getValue() != actual ||
                !detailedMessage.equals(currentDetail.toString()) ||
                currentReachable.getValue() != reacheable) {
            sendVMTrap(intended, actual, detailedMessage, reacheable);
        }

        clusterWrapper.setClusterStatus(intended, actual, detailedMessage, reacheable);
    }

    private String getDetailsMessage(List<MMCBean> beans) {
        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < beans.size(); j++) {
            MMCBean bean = beans.get(j);
            List<MMCAlarm> mmcAlarms = bean.getMmcAlarms();
            if (mmcAlarms.size() == 0) {
                String replace = bean.getName()
                        .replace(".muac.corp.eurocontrol.int", "")
                        .replace(".ps-ssa.local", "");

                sb.append(replace);
                sb.append(":").append("reason unknown")
                        .append((j != beans.size() - 1) ? ", " : "");
            } else {
                for (int k = 0; k < mmcAlarms.size(); k++) {
                    MMCAlarm mmcAlarm = mmcAlarms.get(k);
                    String name = bean.getName().replace(".muac.corp.eurocontrol.int", "");
                    name = name.replace(".ps-ssa.local", "");
                    sb.append(name).append(":").append(mmcAlarm.getKey())
                            .append((j != beans.size() - 1) ? ", " : "");
                }
            }
        }
        return sb.toString();
    }

    private String getMaintenanceMessage(List<MMCHost> beans) {
        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < beans.size(); j++) {
            MMCHost mmcHost = beans.get(j);
            if (mmcHost.isInMaintenanceMode()) {
                String name = purgeName(mmcHost);
                sb.append(name)
                        .append(":").append(" is in maintenance mode")
                        .append((j != beans.size() - 1) ? ", " : "");
            }
        }
        return sb.toString();
    }

    private String purgeName(MMCHost mmcHost) {
        String name = mmcHost.getName().replace(".muac.corp.eurocontrol.int", "");
        name = name.replace(".ps-ssa.local", "");
        return name;
    }

    private void processMountPoint(MMCMountPoint mp) {
        Double minor = config.minor;
        Double major = config.major;
        int currentState = MpaasStatusActualType.operational;
        String currentStatusTxt = "Mount point level " + mp.getPercent() + "% is under alarm threshold of :" + minor;
        if (mp.getPercent() > minor && mp.getPercent() < major) {
            currentState = MpaasStatusActualType.minor;
            currentStatusTxt = "Mount point level " + mp.getPercent() + "% is in between:" + minor + "% and " + major + "%";
        } else if (mp.getPercent() > major && mp.getPercent() < 100D) {
            currentState = MpaasStatusActualType.major;
            currentStatusTxt = "Mount point level " + mp.getPercent() + "% is in between:" + major + "% and 100%";
        } else if (mp.getPercent() > 100D) {
            currentState = MpaasStatusActualType.failed;
            currentStatusTxt = "Mount point level " + mp.getPercent() + "%i s above 100%";
        }

        Variable[] rowValues = new Variable[]{
                new Integer32(MpaasStatusIntendedType.operational),
                new Integer32(currentState),
                new OctetString(currentStatusTxt),
                new Integer32(MpaasStatusActualType.operational),
                new OctetString("normal"),
                new OctetString("normal"),
                new Integer32(MpaasStatusActualType.operational)
        };

        addMountPointRow(mp, rowValues);
    }

    private void addMountPointRow(MMCMountPoint mp, Variable[] row) {

        OctetString virtualMachineName = new OctetString(mp.getHostname());
        OID index = virtualMachineName.toSubIndex(false);

        OctetString dsname = new OctetString(mp.getName());
        OID dsnameindex = dsname.toSubIndex(false);
        OID multipleOid = index.append(dsnameindex);

        MpaasDataStreamEntryRow entryRow = dataStreamsTableWrapper.getByName(
                mp.getHostname(), mp.getName());

        if (entryRow == null) {
            dataStreamsTableWrapper.addRow(new MpaasDataStreamEntryRow(multipleOid, row));
        } else if (
                entryRow.getMpaasDataStreamStatusInt().getValue() != row[1].toLong() ||
                        !entryRow.getMpaasDataStreamStatusDetails().toString().equals(row[2].toString()) ||
                        entryRow.getMpaasDataStreamStatusAct().getValue() != row[3].toLong()) {

            sendMountPointTrap(entryRow.getMpaasDataStreamStatusInt(), row[1], row[2], row[4], entryRow.getIndex());

            dataStreamsTableWrapper.setValue(mp.getHostname(), mp.getName(), 0, row[0]);
            dataStreamsTableWrapper.setValue(mp.getHostname(), mp.getName(), 1, row[1]);
            dataStreamsTableWrapper.setValue(mp.getHostname(), mp.getName(), 2, row[2]);
            dataStreamsTableWrapper.setValue(mp.getHostname(), mp.getName(), 3, row[3]);

        }
    }

    private void processVM(MMCVm vm) {

        Variable[] row = new Variable[]{
                new Integer32(MpaasStatusIntendedType.operational),
                new Integer32(MpaasStatusActualType.operational),
                new OctetString("normal"),
                new Integer32(0),
                new OctetString(vm.getCategory()),
        };

        if (vm.getState().equalsIgnoreCase("poweredOn")) {

            row[0] = new Integer32(MpaasStatusIntendedType.operational);
            row[1] = new Integer32(MpaasStatusActualType.operational);
            row[2] = new OctetString("Powered On");

            ArrayList<MMCBean> objects = new ArrayList<>();
            objects.add(vm);
            if (vm.getStatus().equalsIgnoreCase("yellow")) {
                row[1] = new Integer32(MpaasStatusActualType.warning);
                row[2] = new OctetString(getDetailsMessage(objects));
            }

            if (vm.getStatus().equalsIgnoreCase("red")) {
                row[1] = new Integer32(MpaasStatusActualType.major);
                row[2] = new OctetString(getDetailsMessage(objects));
            }

        } else if (vm.getState().equalsIgnoreCase("poweredOff")) {
            row[0] = new Integer32(MpaasStatusIntendedType.nonOperational);
            row[1] = new Integer32(MpaasStatusActualType.nonOperational);
            row[2] = new OctetString("Powered Off");
        }

        OctetString virtualMachineName = new OctetString(vm.getName());
        OID vmOid = virtualMachineName.toSubIndex(false);

        MpaasVirtualMachineEntryRow row1 = clusterWrapper.getByVmName(vm.getName());
        if (row1 == null)
            clusterWrapper.addRow(new MpaasVirtualMachineEntryRow(vmOid, row));
        else {

            if (row1.getMpaasVirtualMachineStatusAct().getValue() != row[1].toLong() ||
                    !row1.getMpaasVirtualMachineStatusDetails().toString().equals(row[2].toString()) ||
                    row1.getMpaasVirtualMachineIsReachable().getValue() != row[3].toLong()) {

                sendVMTrap(row1.getMpaasVirtualMachineStatusInt(), row[1], row[2], row[4], row1.getIndex());

                clusterWrapper.setValue(vm.getName(), 0, row[0]);
                clusterWrapper.setValue(vm.getName(), 1, row[1]);
                clusterWrapper.setValue(vm.getName(), 2, row[2]);
                clusterWrapper.setValue(vm.getName(), 3, row[3]);

            }
        }
    }

    private void sendVMTrap(Variable intended, Variable actual, Variable details, Variable reacheable, OID index) {
        VariableBinding[] payload = new VariableBinding[4];
        payload[0] = new VariableBinding(new OID(oidTrapVarMpaasVirtualMachineStatusInt).append(index), intended);
        payload[1] = new VariableBinding(new OID(oidTrapVarMpaasVirtualMachineStatusAct).append(index), actual);
        payload[2] = new VariableBinding(new OID(oidTrapVarMpaasVirtualMachineStatusDetails).append(index), details);
        payload[3] = new VariableBinding(new OID(oidTrapVarMpaasVirtualMachineIsReachable).append(index), reacheable);
        mpaasVirtualMachineStatusTrap(subagent, null, payload);
    }

    private void sendMountPointTrap(Variable intended, Variable actual, Variable details, Variable reacheable, OID index) {
        VariableBinding[] payload = new VariableBinding[4];
        payload[0] = new VariableBinding(new OID(oidTrapVarMpaasDataStreamStatusInt).append(index), intended);
        payload[1] = new VariableBinding(new OID(oidTrapVarMpaasDataStreamStatusAct).append(index), actual);
        payload[2] = new VariableBinding(new OID(oidTrapVarMpaasDataStreamStatusDetails).append(index), details);
        payload[3] = new VariableBinding(new OID(oidTrapVarMpaasDataStreamIsReachable).append(index), reacheable);
        mpaasDataStreamStatusTrap(subagent, null, payload);
    }
}
