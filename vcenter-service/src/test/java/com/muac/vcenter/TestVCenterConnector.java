package com.muac.vcenter;

import com.muac.beans.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest({"server.port:0", "eureka.client.enabled:false"})
public class TestVCenterConnector {

    @Autowired
    private
    VCenterConnector connector;

    @Test
    public void testVCenterConnector() {

        MMCDataCenter mmcDataCenter = connector.queryVCenter();

        System.out.println("mmcDataCenter = " + mmcDataCenter.getName());

        List<MMCCluster> clusters = mmcDataCenter.getClusters();
        for (int i = 0; i < clusters.size(); i++) {
            MMCCluster mmcCluster = clusters.get(i);
            List<MMCAlarm> mmcAlarms = mmcCluster.getMmcAlarms();
            for (int l = 0; l < mmcAlarms.size(); l++) {
                MMCAlarm mmcAlarm = mmcAlarms.get(l);
                String key = mmcAlarm.getKey();
                System.out.println("key = " + key);
            }

            System.out.println("\tmmcCluster.getName() = " + mmcCluster.getName());

            List<MMCHost> nodes = mmcCluster.getNodes();
            for (int j = 0; j < nodes.size(); j++) {
                MMCHost mmcHost = nodes.get(j);

                System.out.println("\t\tmmcHost = " + mmcHost.getName());
                printAlarms(mmcHost);

                List<MMCVm> vms = mmcHost.getVms();

                for (int k = 0; k < vms.size(); k++) {
                    MMCVm vm = vms.get(k);
                    System.out.println("\t\tvm = " + vm.getName());

                    printAlarms(vm);

                }
            }
        }
    }

    private void printAlarms(MMCBean mmcHost) {
        List<MMCAlarm> mmcAlarms = mmcHost.getMmcAlarms();
        for (int l = 0; l < mmcAlarms.size(); l++) {
            MMCAlarm mmcAlarm = mmcAlarms.get(l);
            String key = mmcAlarm.getKey();
            System.out.println("key = " + key);
        }
    }
}
