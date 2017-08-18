package com.muac.vcenter;

import com.muac.beans.*;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class VCenterConnector {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(VCenterController.class);

    private final VCenterConfiguration configuration;

    @Autowired
    public VCenterConnector(VCenterConfiguration configuration) {
        this.configuration = configuration;
    }

    public MMCDataCenter queryVCenter() {

        MMCDataCenter dataCenter = null;
        ServiceInstance serviceInstance = null;
        try {
            serviceInstance =
                    new ServiceInstance(
                            new URL(configuration.url), configuration.user, configuration.password,
                            true);

            CustomFieldsManager customFieldsManager = serviceInstance.getCustomFieldsManager();
            CustomFieldDef[] field = customFieldsManager.getField();
            Folder rootFolder = serviceInstance.getRootFolder();

            ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("Datacenter");
            if (mes == null || mes.length == 0) {
                throw new RuntimeException("cannot connect to VCenter to retrieve Datacenter");
            }

            for (ManagedEntity dcmes : mes) {
                Datacenter datacenter = (Datacenter) dcmes;

                dataCenter = new MMCDataCenter();
                dataCenter.setName(datacenter.getName());
                dataCenter.setStatus(datacenter.getOverallStatus().toString());
                checkIfThereAreAlarmsAssociated(serviceInstance, datacenter, dataCenter);

                Datastore[] datastores = datacenter.getDatastores();
                for (int i = 0; i < datastores.length; i++) {
                    Datastore datastore = datastores[i];
                    MMCDatastore mmcDatastore = new MMCDatastore();
                    mmcDatastore.setName(datastore.getName());
                    mmcDatastore.setStatus(datastore.getOverallStatus().toString());
                    checkIfThereAreAlarmsAssociated(serviceInstance, datastore, mmcDatastore);
                    dataCenter.getDatastores().add(mmcDatastore);
                }

                mes = new InventoryNavigator(datacenter).searchManagedEntities("ClusterComputeResource");
                if (mes == null || mes.length == 0) {
                    throw new RuntimeException("cannot connect to VCenter to retrieve Cluster");
                }

                //            for (ManagedEntity clrmes : mes) {

                ClusterComputeResource cluster = (ClusterComputeResource) mes[0];
                MMCCluster cl = new MMCCluster();
                cl.setName(cluster.getName());
                cl.setStatus(cluster.getSummary().getOverallStatus().toString());

                log.debug("check if there are Cluster level alarms");
                checkIfThereAreAlarmsAssociated(serviceInstance, cluster, cl);
                log.debug("CLUSTER level alarms number :" + cl.getMmcAlarms().size());

                dataCenter.getClusters().add(cl);
                mes = new InventoryNavigator(cluster).searchManagedEntities("HostSystem");

                if (mes == null || mes.length == 0) {
                    throw new RuntimeException("cannot connect to VCenter to retrieve Hosts");
                }

                for (ManagedEntity hostmes : mes) {

                    HostSystem hostSystem = (HostSystem) hostmes;
                    MMCHost mmcHost = new MMCHost();
                    mmcHost.setName(hostmes.getName());
                    mmcHost.setStatus(hostSystem.getSummary().getOverallStatus().toString());
                    mmcHost.setState(hostSystem.getRuntime().powerState.toString());
                    mmcHost.setInMaintenanceMode(hostSystem.getRuntime().isInMaintenanceMode());

                    log.debug("check if there are Host level alarms");
                    checkIfThereAreAlarmsAssociated(serviceInstance, hostSystem, mmcHost);
                    log.debug("HOST level alarms number :" + mmcHost.getMmcAlarms().size());

                    cl.getNodes().add(mmcHost);
                    VirtualMachine vmm[] = hostSystem.getVms();

                    for (VirtualMachine vm : vmm) {

                        log.debug("\n\n==============================================\nvm = " + vm);
                        CustomFieldValue[] customValue = vm.getSummary().getCustomValue();
                        String category = "";
                        if (customValue != null) {
                            for (int i = 0; i < customValue.length; i++) {
                                CustomFieldValue customFieldValue = customValue[i];
                                CustomFieldStringValue xxx = (CustomFieldStringValue) customFieldValue;
                                category = xxx.getValue();
                            }
                        }
                        MMCVm mmcvm = new MMCVm();
                        mmcvm.setName(vm.getName());
                        mmcvm.setStatus(vm.getSummary().getOverallStatus().toString());
                        mmcvm.setState(vm.getRuntime().powerState.toString());

                        log.debug("check if there are VM level alarms");
                        checkIfThereAreAlarmsAssociated(serviceInstance, vm, mmcvm);
                        log.debug("VM alarms number :" + mmcvm.getMmcAlarms().size());


                        mmcvm.setCategory(category);
                        mmcHost.getVms().add(mmcvm);
                    }
                }
            }
            //  }
            serviceInstance.getServerConnection().logout();

            return dataCenter;

        } catch (Exception ex) {
            if (serviceInstance != null) serviceInstance.getServerConnection().logout();
        }

        return null;
    }

    private void checkIfThereAreAlarmsAssociated(
            ServiceInstance serviceInstance,
            ManagedEntity mo,
            MMCBean mmcEntity) {

        log.debug("mo.getName() ----> " + mo.getName());
        log.debug("mo.getName() ----> " + mmcEntity.getClass().toString());

        AlarmState[] triggeredAlarmState = mo.getTriggeredAlarmState();
        if (triggeredAlarmState != null)
            for (AlarmState alarmState : triggeredAlarmState) {
                ManagedObjectReference alarm = alarmState.getAlarm();

                Alarm instance = new Alarm(serviceInstance.getServerConnection(), alarm);
                AlarmInfo alarmInfo = instance.getAlarmInfo();

                MMCAlarm alarm1 = new MMCAlarm();
                alarm1.setKey(alarmInfo.getName());
                alarm1.setDate(alarmInfo.getLastModifiedTime().getTime());

                mmcEntity.getMmcAlarms().add(alarm1);
            }
    }
}
