package com.muac.snmp;

import com.muac.beans.MMCDataCenter;
import com.muac.beans.MMCMountPoint;
import com.muac.beans.MMCMountPoints;
import com.muac.snmp.mib.ClusterWrapper;
import com.muac.snmp.mib.MUACPAASMIB;
import com.muac.snmp.mib.MibTest;
import com.muac.snmp.mib.MuacMib;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.snmp4j.agent.mo.*;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OctetString;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class MuacMibTest extends MibTest {

    MuacMib mib;

    @Before
    public void setUp() throws Exception {
        MOFactory factory = DefaultMOFactory.getInstance();
        DefaultMOFactory.addSNMPv2TCs(factory);
        mib = new MuacMib(factory);

        config.minor = 30D;
        config.major = 70D;
    }

    @Test
    public void noAlarms_shouldDisplayAllOperational() throws IOException {
        greenResults();
        allRedResults();
        greenResults();
        noAlarmsAllInMaintenance();
        greenResults();
        allRedResults();
        allRedResults();

        greenResults();
        oneAlarmsUpToMinusOneInRed();
        greenResults();
    }

    @Test
    public void greenResults() throws IOException {

        MMCDataCenter dataCenter = getMmcDataCenter("noAlarms");
        mib.config = config;
        mib.subagent = subagent;

        mib.updateMib(dataCenter);

        Integer32 intended = mib.clusterWrapper.getIntended();
        Integer32 actual = mib.clusterWrapper.getActual();
        OctetString details = mib.clusterWrapper.getDetails();

        assertEquals(MUACPAASMIB.MpaasStatusIntendedType.operational, intended.getValue());
        assertEquals(MUACPAASMIB.MpaasStatusActualType.operational, actual.getValue());
        assertEquals("", details.toString());

        ClusterWrapper mibVmWareWrapper = mib.clusterWrapper;

        MOTable<MUACPAASMIB.MpaasVirtualMachineEntryRow, MOColumn, MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow>> table1 = mibVmWareWrapper.getTable();
        MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow> model = table1.getModel();

        Assert.assertEquals(16, model.getRowCount());

        MMCMountPoints mmcMountPoints = getMmcMountPoints("mountpoints");
        mib.updateMib(mmcMountPoints);
        Assert.assertEquals(4, mmcMountPoints.getMmcMountPoints().size());

        printMountPoints(mmcMountPoints);

        printVmDetailMessages(model);
    }

    @Test
    public void greenResultsChangeMountPoint() throws IOException {

        mib.config = config;
        mib.subagent = subagent;

        MMCMountPoints mmcMountPoints = getMmcMountPoints("mountpoints");
        mib.updateMib(mmcMountPoints);
        Assert.assertEquals(4, mmcMountPoints.getMmcMountPoints().size());

        mmcMountPoints = getMmcMountPoints("mountpoints-changed");
        mib.updateMib(mmcMountPoints);

    }


    private void printVmDetailMessages(MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow> model) {
        Iterator<MUACPAASMIB.MpaasVirtualMachineEntryRow> iterator = model.iterator();
        while (iterator.hasNext()) {
            MUACPAASMIB.MpaasVirtualMachineEntryRow next = iterator.next();
            OctetString mpaasVirtualMachineStatusDetails = next.getMpaasVirtualMachineStatusDetails();
            System.out.println("mpaasVirtualMachineStatusDetails.toString() = " + mpaasVirtualMachineStatusDetails.toString());
        }
    }

    private void printMountPoints(MMCMountPoints mmcMountPoints) {
        List<MMCMountPoint> mmcMountPoints1 = mmcMountPoints.getMmcMountPoints();
        for (int i = 0; i < mmcMountPoints1.size(); i++) {
            MMCMountPoint mmcMountPoint = mmcMountPoints1.get(i);
            System.out.println("mmcMountPoint.getPercent() = " + mmcMountPoint.getPercent());
        }
    }

    @Test
    public void allRedResults() throws IOException {

        MMCDataCenter dataCenter = getMmcDataCenter("noAlarmsAllRed");
        mib.subagent = subagent;
        mib.config = config;
        mib.updateMib(dataCenter);

        Integer32 intended = mib.clusterWrapper.getIntended();
        Integer32 actual = mib.clusterWrapper.getActual();
        OctetString details = mib.clusterWrapper.getDetails();

        assertEquals(MUACPAASMIB.MpaasStatusIntendedType.operational, intended.getValue());
        assertEquals(MUACPAASMIB.MpaasStatusActualType.major, actual.getValue());
        assertEquals("CL-MSSA910:reason unknown, mssae911:reason unknown, mssae912:reason unknown, mssae913:reason unknown", details.toString());

        ClusterWrapper mibVmWareWrapper = mib.clusterWrapper;

        MOTable<MUACPAASMIB.MpaasVirtualMachineEntryRow, MOColumn, MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow>> table1 = mibVmWareWrapper.getTable();
        MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow> model = table1.getModel();

        Assert.assertEquals(16, model.getRowCount());

        MMCMountPoints mmcMountPoints = getMmcMountPoints("mountpoints-changed");
        Assert.assertEquals(4, mmcMountPoints.getMmcMountPoints().size());

        printMountPoints(mmcMountPoints);
    }

    @Test
    public void noAlarmsOneInMaintenance() throws IOException {

        MMCDataCenter dataCenter = getMmcDataCenter("noAlarmsOneInMaintenance");
        mib.subagent = subagent;
        mib.config = config;
        mib.updateMib(dataCenter);

        Integer32 intended = mib.clusterWrapper.getIntended();
        Integer32 actual = mib.clusterWrapper.getActual();
        OctetString details = mib.clusterWrapper.getDetails();

        assertEquals(MUACPAASMIB.MpaasStatusIntendedType.operational, intended.getValue());
        assertEquals(MUACPAASMIB.MpaasStatusActualType.warning, actual.getValue());
        assertEquals("mssae911: is in maintenance mode", details.toString());

        ClusterWrapper mibVmWareWrapper = mib.clusterWrapper;

        MOTable<MUACPAASMIB.MpaasVirtualMachineEntryRow, MOColumn, MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow>> table1 = mibVmWareWrapper.getTable();
        MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow> model = table1.getModel();

        Assert.assertEquals(16, model.getRowCount());

        MMCMountPoints mmcMountPoints = getMmcMountPoints("mountpoints-changed");
        Assert.assertEquals(4, mmcMountPoints.getMmcMountPoints().size());

        printMountPoints(mmcMountPoints);
    }


    @Test
    public void noAlarmsAllInMaintenance() throws IOException {

        MMCDataCenter dataCenter = getMmcDataCenter("noAlarmsAllInMaintenance");
        mib.subagent = subagent;
        mib.config = config;
        mib.updateMib(dataCenter);

        Integer32 intended = mib.clusterWrapper.getIntended();
        Integer32 actual = mib.clusterWrapper.getActual();
        OctetString details = mib.clusterWrapper.getDetails();

        assertEquals(MUACPAASMIB.MpaasStatusIntendedType.nonOperational, intended.getValue());
        assertEquals(MUACPAASMIB.MpaasStatusActualType.nonOperational, actual.getValue());
        assertEquals("All Nodes of the cluster are in maintenance mode", details.toString());

        ClusterWrapper mibVmWareWrapper = mib.clusterWrapper;

        MOTable<MUACPAASMIB.MpaasVirtualMachineEntryRow, MOColumn, MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow>> table1 = mibVmWareWrapper.getTable();
        MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow> model = table1.getModel();

        Assert.assertEquals(16, model.getRowCount());

        MMCMountPoints mmcMountPoints = getMmcMountPoints("mountpoints-changed");
        Assert.assertEquals(4, mmcMountPoints.getMmcMountPoints().size());

        printMountPoints(mmcMountPoints);
    }


    @Test
    public void noAlarmsOneInYellow() throws IOException {

        MMCDataCenter dataCenter = getMmcDataCenter("noAlarmsOneInYellow");
        mib.subagent = subagent;
        mib.config = config;
        mib.updateMib(dataCenter);

        Integer32 intended = mib.clusterWrapper.getIntended();
        Integer32 actual = mib.clusterWrapper.getActual();
        OctetString details = mib.clusterWrapper.getDetails();

        assertEquals(MUACPAASMIB.MpaasStatusIntendedType.operational, intended.getValue());
        assertEquals(MUACPAASMIB.MpaasStatusActualType.warning, actual.getValue());
        assertEquals("mssae911:reason unknown", details.toString());

        ClusterWrapper mibVmWareWrapper = mib.clusterWrapper;

        MOTable<MUACPAASMIB.MpaasVirtualMachineEntryRow, MOColumn, MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow>> table1 = mibVmWareWrapper.getTable();
        MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow> model = table1.getModel();

        Assert.assertEquals(16, model.getRowCount());

        MMCMountPoints mmcMountPoints = getMmcMountPoints("mountpoints-changed");
        Assert.assertEquals(4, mmcMountPoints.getMmcMountPoints().size());

        printMountPoints(mmcMountPoints);
    }


    @Test
    public void noAlarmsTwoInYellow() throws IOException {

        MMCDataCenter dataCenter = getMmcDataCenter("noAlarmsTwoInYellow");
        mib.subagent = subagent;
        mib.config = config;
        mib.updateMib(dataCenter);

        Integer32 intended = mib.clusterWrapper.getIntended();
        Integer32 actual = mib.clusterWrapper.getActual();
        OctetString details = mib.clusterWrapper.getDetails();

        assertEquals(MUACPAASMIB.MpaasStatusIntendedType.operational, intended.getValue());
        assertEquals(MUACPAASMIB.MpaasStatusActualType.warning, actual.getValue());
        assertEquals("mssae911:reason unknown, mssae912:reason unknown", details.toString());

        ClusterWrapper mibVmWareWrapper = mib.clusterWrapper;

        MOTable<MUACPAASMIB.MpaasVirtualMachineEntryRow, MOColumn, MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow>> table1 = mibVmWareWrapper.getTable();
        MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow> model = table1.getModel();

        Assert.assertEquals(16, model.getRowCount());

        MMCMountPoints mmcMountPoints = getMmcMountPoints("mountpoints-changed");
        Assert.assertEquals(4, mmcMountPoints.getMmcMountPoints().size());

        printMountPoints(mmcMountPoints);
    }


    @Test
    public void noAlarmsUpToMinusOneInRed() throws IOException {

        MMCDataCenter dataCenter = getMmcDataCenter("noAlarmsUpToMinusOneInRed");
        mib.subagent = subagent;
        mib.config = config;
        mib.updateMib(dataCenter);

        Integer32 intended = mib.clusterWrapper.getIntended();
        Integer32 actual = mib.clusterWrapper.getActual();
        OctetString details = mib.clusterWrapper.getDetails();

        assertEquals(MUACPAASMIB.MpaasStatusIntendedType.operational, intended.getValue());
        assertEquals(MUACPAASMIB.MpaasStatusActualType.major, actual.getValue());
        assertEquals("mssae911:reason unknown, mssae912:reason unknown", details.toString());

        ClusterWrapper mibVmWareWrapper = mib.clusterWrapper;

        MOTable<MUACPAASMIB.MpaasVirtualMachineEntryRow, MOColumn, MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow>> table1 = mibVmWareWrapper.getTable();
        MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow> model = table1.getModel();

        Assert.assertEquals(16, model.getRowCount());

        MMCMountPoints mmcMountPoints = getMmcMountPoints("mountpoints-changed");
        Assert.assertEquals(4, mmcMountPoints.getMmcMountPoints().size());

        printMountPoints(mmcMountPoints);
    }


    @Test
    public void noAlarmsUpToMinusTwoInRed() throws IOException {

        MMCDataCenter dataCenter = getMmcDataCenter("noAlarmsUpToMinusTwoInRed");
        mib.subagent = subagent;
        mib.config = config;
        mib.updateMib(dataCenter);

        Integer32 intended = mib.clusterWrapper.getIntended();
        Integer32 actual = mib.clusterWrapper.getActual();
        OctetString details = mib.clusterWrapper.getDetails();

        assertEquals(MUACPAASMIB.MpaasStatusIntendedType.operational, intended.getValue());
        assertEquals(MUACPAASMIB.MpaasStatusActualType.major, actual.getValue());
        assertEquals("mssae911:reason unknown, mssae912:reason unknown, mssae913:reason unknown", details.toString());

        ClusterWrapper mibVmWareWrapper = mib.clusterWrapper;

        MOTable<MUACPAASMIB.MpaasVirtualMachineEntryRow, MOColumn, MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow>> table1 = mibVmWareWrapper.getTable();
        MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow> model = table1.getModel();

        Assert.assertEquals(16, model.getRowCount());

        MMCMountPoints mmcMountPoints = getMmcMountPoints("mountpoints-changed");
        Assert.assertEquals(4, mmcMountPoints.getMmcMountPoints().size());

        printMountPoints(mmcMountPoints);
    }

    @Test
    public void oneAlarmsAllRed() throws IOException {

        MMCDataCenter dataCenter = getMmcDataCenter("oneAlarmsAllRed");
        mib.subagent = subagent;
        mib.config = config;
        mib.updateMib(dataCenter);

        Integer32 intended = mib.clusterWrapper.getIntended();
        Integer32 actual = mib.clusterWrapper.getActual();
        OctetString details = mib.clusterWrapper.getDetails();

        assertEquals(MUACPAASMIB.MpaasStatusIntendedType.operational, intended.getValue());
        assertEquals(MUACPAASMIB.MpaasStatusActualType.major, actual.getValue());
        assertEquals("CL-MSSA910:reason unknown, mssae911:Test Alarm, mssae912:reason unknown, mssae913:reason unknown", details.toString());

        ClusterWrapper mibVmWareWrapper = mib.clusterWrapper;

        MOTable<MUACPAASMIB.MpaasVirtualMachineEntryRow, MOColumn, MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow>> table1 = mibVmWareWrapper.getTable();
        MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow> model = table1.getModel();

        Assert.assertEquals(16, model.getRowCount());

        MMCMountPoints mmcMountPoints = getMmcMountPoints("mountpoints-changed");
        Assert.assertEquals(4, mmcMountPoints.getMmcMountPoints().size());

        printMountPoints(mmcMountPoints);
    }


    @Test
    public void oneAlarmsOneInYellow() throws IOException {

        MMCDataCenter dataCenter = getMmcDataCenter("oneAlarmsOneInYellow");
        mib.subagent = subagent;
        mib.config = config;
        mib.updateMib(dataCenter);

        Integer32 intended = mib.clusterWrapper.getIntended();
        Integer32 actual = mib.clusterWrapper.getActual();
        OctetString details = mib.clusterWrapper.getDetails();

        assertEquals(MUACPAASMIB.MpaasStatusIntendedType.operational, intended.getValue());
        assertEquals(MUACPAASMIB.MpaasStatusActualType.warning, actual.getValue());
        assertEquals("CL-MSSA910:reason unknown, mssae911:Test Alarm", details.toString());

        ClusterWrapper mibVmWareWrapper = mib.clusterWrapper;

        MOTable<MUACPAASMIB.MpaasVirtualMachineEntryRow, MOColumn, MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow>> table1 = mibVmWareWrapper.getTable();
        MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow> model = table1.getModel();

        Assert.assertEquals(16, model.getRowCount());

        MMCMountPoints mmcMountPoints = getMmcMountPoints("mountpoints-changed");
        Assert.assertEquals(4, mmcMountPoints.getMmcMountPoints().size());

        printMountPoints(mmcMountPoints);
    }

    @Test
    public void oneAlarmsUpToMinusOneInRed() throws IOException {

        MMCDataCenter dataCenter = getMmcDataCenter("oneAlarmsUpToMinusOneInRedOneInVm");
        mib.subagent = subagent;
        mib.config = config;
        mib.updateMib(dataCenter);

        Integer32 intended = mib.clusterWrapper.getIntended();
        Integer32 actual = mib.clusterWrapper.getActual();
        OctetString details = mib.clusterWrapper.getDetails();

        assertEquals(MUACPAASMIB.MpaasStatusIntendedType.operational, intended.getValue());
        assertEquals(MUACPAASMIB.MpaasStatusActualType.major, actual.getValue());
        assertEquals("mssae911:Test Alarm, mssae912:reason unknown, mssae913:reason unknown", details.toString());

        ClusterWrapper mibVmWareWrapper = mib.clusterWrapper;

        MOTable<MUACPAASMIB.MpaasVirtualMachineEntryRow, MOColumn, MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow>> table1 = mibVmWareWrapper.getTable();
        MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow> model = table1.getModel();

        Assert.assertEquals(16, model.getRowCount());

        MMCMountPoints mmcMountPoints = getMmcMountPoints("mountpoints-changed");
        Assert.assertEquals(4, mmcMountPoints.getMmcMountPoints().size());

        printMountPoints(mmcMountPoints);


        MUACPAASMIB.MpaasVirtualMachineEntryRow row1 = mibVmWareWrapper.getByVmName("miperf");

        Assert.assertEquals("miperf:Test Alarm", row1.getMpaasVirtualMachineStatusDetails().toString());

    }


    @Test
    public void oneAlarmsUpToMinusTwoInRed() throws IOException {

        MMCDataCenter dataCenter = getMmcDataCenter("oneAlarmsUpToMinusTwoInRed");
        mib.subagent = subagent;
        mib.config = config;
        mib.updateMib(dataCenter);

        Integer32 intended = mib.clusterWrapper.getIntended();
        Integer32 actual = mib.clusterWrapper.getActual();
        OctetString details = mib.clusterWrapper.getDetails();

        assertEquals(MUACPAASMIB.MpaasStatusIntendedType.operational, intended.getValue());
        assertEquals(MUACPAASMIB.MpaasStatusActualType.major, actual.getValue());
        assertEquals("mssae911:Test Alarm, mssae912:reason unknown, mssae913:reason unknown", details.toString());

        ClusterWrapper mibVmWareWrapper = mib.clusterWrapper;

        MOTable<MUACPAASMIB.MpaasVirtualMachineEntryRow, MOColumn, MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow>> table1 = mibVmWareWrapper.getTable();
        MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow> model = table1.getModel();

        Assert.assertEquals(16, model.getRowCount());

        MMCMountPoints mmcMountPoints = getMmcMountPoints("mountpoints-changed");
        Assert.assertEquals(4, mmcMountPoints.getMmcMountPoints().size());

        printMountPoints(mmcMountPoints);
    }


    @Test
    public void twoAlarmsAllRed() throws IOException {

        MMCDataCenter dataCenter = getMmcDataCenter("twoAlarmsAllRed");
        mib.subagent = subagent;
        mib.config = config;
        mib.updateMib(dataCenter);

        Integer32 intended = mib.clusterWrapper.getIntended();
        Integer32 actual = mib.clusterWrapper.getActual();
        OctetString details = mib.clusterWrapper.getDetails();

        assertEquals(MUACPAASMIB.MpaasStatusIntendedType.operational, intended.getValue());
        assertEquals(MUACPAASMIB.MpaasStatusActualType.major, actual.getValue());
        assertEquals("CL-MSSA910:reason unknown, mssae911:Test Alarm, mssae912:Test Alarm, mssae913:reason unknown", details.toString());

        ClusterWrapper mibVmWareWrapper = mib.clusterWrapper;

        MOTable<MUACPAASMIB.MpaasVirtualMachineEntryRow, MOColumn, MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow>> table1 = mibVmWareWrapper.getTable();
        MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow> model = table1.getModel();

        Assert.assertEquals(16, model.getRowCount());

        MMCMountPoints mmcMountPoints = getMmcMountPoints("mountpoints-changed");
        Assert.assertEquals(4, mmcMountPoints.getMmcMountPoints().size());

        printMountPoints(mmcMountPoints);
    }

    @Test
    public void twoAlarmsTwoInYellow() throws IOException {

        MMCDataCenter dataCenter = getMmcDataCenter("twoAlarmsTwoInYellow");
        mib.subagent = subagent;
        mib.config = config;
        mib.updateMib(dataCenter);

        Integer32 intended = mib.clusterWrapper.getIntended();
        Integer32 actual = mib.clusterWrapper.getActual();
        OctetString details = mib.clusterWrapper.getDetails();

        assertEquals(MUACPAASMIB.MpaasStatusIntendedType.operational, intended.getValue());
        assertEquals(MUACPAASMIB.MpaasStatusActualType.warning, actual.getValue());
        assertEquals("mssae911:Test Alarm, mssae912:Test Alarm", details.toString());

        ClusterWrapper mibVmWareWrapper = mib.clusterWrapper;

        MOTable<MUACPAASMIB.MpaasVirtualMachineEntryRow, MOColumn, MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow>> table1 = mibVmWareWrapper.getTable();
        MOTableModel<MUACPAASMIB.MpaasVirtualMachineEntryRow> model = table1.getModel();

        Assert.assertEquals(16, model.getRowCount());

        MMCMountPoints mmcMountPoints = getMmcMountPoints("mountpoints-changed");
        Assert.assertEquals(4, mmcMountPoints.getMmcMountPoints().size());

        printMountPoints(mmcMountPoints);
    }


    @Test
    public void noAlarms_shoulddDisplayAllOperational() throws IOException {

        MMCDataCenter dataCenter = getMmcDataCenter("noAlarms");

        mib.updateMib(dataCenter);

        Integer32 intended = mib.clusterWrapper.getIntended();
        Integer32 actual = mib.clusterWrapper.getActual();
        OctetString details = mib.clusterWrapper.getDetails();

        assertEquals(MUACPAASMIB.MpaasStatusIntendedType.operational, intended.getValue());
        assertEquals(MUACPAASMIB.MpaasStatusActualType.operational, actual.getValue());
        assertEquals("", details.toString());
    }

}