package com.muac.snmp.mib;

import com.muac.snmp.utils.SnmpUtils;
import org.slf4j.LoggerFactory;
import org.snmp4j.agent.mo.MOColumn;
import org.snmp4j.agent.mo.MOMutableTableModel;
import org.snmp4j.agent.mo.MOTable;
import org.snmp4j.agent.mo.MOTableModel;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

public class ClusterWrapper {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ClusterWrapper.class);

    private final MuacMib mib;
    private MOTable<MUACPAASMIB.MpaasVirtualMachineEntryRow, MOColumn, MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow>>
            table;

    public ClusterWrapper(MuacMib muacMib) {
        this.mib = muacMib;
        this.table = mib.getMpaasVirtualMachineEntry();
    }

    public MOTable<MUACPAASMIB.MpaasVirtualMachineEntryRow, MOColumn, MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow>> getTable() {
        return table;
    }

    public void addRow(MUACPAASMIB.MpaasVirtualMachineEntryRow mpaasVirtualMachineEntryRow) {
        ((MOMutableTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow>) table.getModel()).addRow(mpaasVirtualMachineEntryRow);
    }

    public MUACPAASMIB.MpaasVirtualMachineEntryRow getByVmName(String vmName) {
        return SnmpUtils.getRow(mib, vmName);
    }

    public void setValue(String vm, int index, Variable variable) {
        MUACPAASMIB.MpaasVirtualMachineEntryRow byVmName = getByVmName(vm);
        byVmName.setValue(index, variable);
    }

    public Integer32 getActual() {
        return mib.getMpaasClusterStatusAct().getValue();
    }

    public void setActual(int actual) {
        mib.getMpaasClusterStatusAct().setValue(new Integer32(actual));
    }

    public Integer32 getIntended() {
        return mib.getMpaasClusterStatusInt().getValue();
    }

    public void setIntended(int intended) {
        mib.getMpaasClusterStatusInt().setValue(new Integer32(intended));
    }

    public void setReacheable(int reacheable) {
        mib.getMpaasClusterIsReachable().setValue(new Integer32(reacheable));
    }

    public OctetString getDetails() {
        return mib.getMpaasClusterStatusDetails().getValue();
    }

    public void setDetails(String str) {
        mib.getMpaasClusterStatusDetails().setValue(new OctetString(str));
    }

    public void setClusterStatus(int intended, int actual, String details, int reacheable) {
        setIntended(intended);
        setActual(actual);
        setDetails(details);
        setReacheable(reacheable);
    }


}
