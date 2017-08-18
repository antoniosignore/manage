package com.muac.snmp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muac.snmp.mib.MUACPAASMIB;
import com.muac.snmp.mib.MuacMib;
import org.slf4j.LoggerFactory;
import org.snmp4j.agent.mo.MOColumn;
import org.snmp4j.agent.mo.MOTable;
import org.snmp4j.agent.mo.MOTableModel;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;

import java.io.Serializable;
import java.util.Iterator;

public class SnmpUtils {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SnmpUtils.class);


    public static MUACPAASMIB.MpaasVirtualMachineEntryRow getRow(MuacMib muacMib, String vmName) {
        OctetString virtualMachineName = new OctetString(vmName);
        OID vmOid = virtualMachineName.toSubIndex(false);

        MOTable<MUACPAASMIB.MpaasVirtualMachineEntryRow, MOColumn, MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow>>
                table = muacMib.getMpaasVirtualMachineEntry();

        MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow> model = table.getModel();

        Iterator iterator = model.iterator();
        while (iterator.hasNext()) {
            MUACPAASMIB.MpaasVirtualMachineEntryRow row = (MUACPAASMIB.MpaasVirtualMachineEntryRow) iterator.next();
            OID index = row.getIndex();
            if (index.toDottedString().equalsIgnoreCase(vmOid.toString())) {
                return row;
            }
        }
        return null;
    }

    public static MUACPAASMIB.MpaasDataStreamEntryRow getDataStreamRow(MuacMib muacMib, String hostname, String dsname) {

        OctetString virtualMachineName = new OctetString(hostname);
        OID index = virtualMachineName.toSubIndex(false);

        OctetString dsnameString = new OctetString(dsname);
        OID dsnameindex = dsnameString.toSubIndex(false);
        OID multipleOid = index.append(dsnameindex);

        MOTable<MUACPAASMIB.MpaasDataStreamEntryRow, MOColumn, MOTableModel<MUACPAASMIB.MpaasDataStreamEntryRow>>
                table = muacMib.getMpaasDataStreamEntry();

        MOTableModel<MUACPAASMIB.MpaasDataStreamEntryRow> model = table.getModel();
        Iterator iterator = model.iterator();
        while (iterator.hasNext()) {
            MUACPAASMIB.MpaasDataStreamEntryRow row = (MUACPAASMIB.MpaasDataStreamEntryRow) iterator.next();
            OID in = row.getIndex();
            String s = in.toDottedString();
            String anotherString = multipleOid.toString();
            System.out.println("anotherString = " + anotherString);

            if (s.equalsIgnoreCase(anotherString))
                return row;
        }
        return null;
    }

    public static void prettyPrint(Serializable obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            log.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
