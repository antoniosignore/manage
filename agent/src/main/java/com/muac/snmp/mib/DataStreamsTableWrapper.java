package com.muac.snmp.mib;

import com.muac.snmp.utils.SnmpUtils;
import org.slf4j.LoggerFactory;
import org.snmp4j.agent.mo.MOColumn;
import org.snmp4j.agent.mo.MOMutableTableModel;
import org.snmp4j.agent.mo.MOTable;
import org.snmp4j.agent.mo.MOTableModel;
import org.snmp4j.smi.Variable;

public class DataStreamsTableWrapper {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DataStreamsTableWrapper.class);

    private final MuacMib mib;
    private MOTable<MUACPAASMIB.MpaasDataStreamEntryRow, MOColumn, MOTableModel<MUACPAASMIB.MpaasDataStreamEntryRow>>
            table;

    public DataStreamsTableWrapper(MuacMib muacMib) {
        this.mib = muacMib;
        this.table = mib.getMpaasDataStreamEntry();
    }

    public void addRow(MUACPAASMIB.MpaasDataStreamEntryRow row) {
        ((MOMutableTableModel<MUACPAASMIB.MpaasDataStreamEntryRow>) table.getModel()).addRow(row);
    }

    public MUACPAASMIB.MpaasDataStreamEntryRow getByName(String hostname, String datastreamName) {
        return SnmpUtils.getDataStreamRow(mib, hostname, datastreamName);
    }

    public void setValue(String hostname, String mp, int index, Variable variable) {
        MUACPAASMIB.MpaasDataStreamEntryRow byVmName = getByName(hostname, mp);
        byVmName.setValue(index, variable);
    }

}
