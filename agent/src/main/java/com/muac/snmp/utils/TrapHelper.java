package com.muac.snmp.utils;

import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class TrapHelper {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(TrapHelper.class);

    public static void sendTrap(String ipAddress, int port, String community,
                                VariableBinding[] payload, OID trapOID, Variable severity) {
        try {

            // Create Transport Mapping
            TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
            transport.listen();

            // Create Target
            CommunityTarget comtarget = new CommunityTarget();
            comtarget.setCommunity(new OctetString(community));
            comtarget.setVersion(SnmpConstants.version2c);
            comtarget.setAddress(new UdpAddress(ipAddress + "/" + port));
            comtarget.setRetries(2);
            comtarget.setTimeout(5000);

            PDU pdu = new PDU();
            for (int i = 0; i < payload.length; i++) {
                pdu.add(payload[i]);
            }

            pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress, new IpAddress(ipAddress)));
            pdu.add(new VariableBinding(trapOID));
            pdu.setType(PDU.NOTIFICATION);

            // Send the PDU
            Snmp snmp = new Snmp(transport);
            snmp.send(pdu, comtarget);
            snmp.close();
        } catch (Exception e) {
            System.err.println("Error in Sending V2 Trap to " + ipAddress + " on Port " + port);
            System.err.println("Exception Message = " + e.getMessage());
        }
    }
}
