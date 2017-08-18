package com.muac.snmp.agent;

import com.muac.snmp.ApplicationConfig;
import com.muac.snmp.mib.MuacMib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class VCenterPollerTask {

    private static final Logger log = LoggerFactory.getLogger(VCenterPollerTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    final ApplicationConfig config;
    final MuacMib mib;

    @Autowired
    public VCenterPollerTask(ApplicationConfig config, MuacMib mib) {
        this.config = config;
        this.mib = mib;
    }

    @Scheduled(fixedDelayString = "${snmp.agent.polling}")
    public void polling() {
        log.info("Polling Nutanix. The time is now {}", dateFormat.format(new Date()));
        mib.getDataCenterAndUpdateMib(config.folderService);
    }

    @Scheduled(fixedDelayString = "${snmp.agent.polling}")
    public void pollingRSync() {
        log.info("Polling RSync. The time is now {}", dateFormat.format(new Date()));
        mib.getRSyncSnapshotAndUpdateMib(config.rsyncService + "/" + config.box);
    }


}