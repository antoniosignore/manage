package com.muac.snmp.mib;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muac.beans.MMCDataCenter;
import com.muac.beans.MMCMountPoints;
import com.muac.snmp.ApplicationConfig;
import com.muac.snmp.agent.MuacAgent;
import com.muac.snmp.agent.VCenterPollerTask;
import org.snmp4j.agent.MOServer;
import org.snmp4j.agent.agentx.subagent.AgentXSubagent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

public class MibTest {

    @MockBean
    protected RestTemplate restTemplate;

    @MockBean
    protected ApplicationConfig config;

    @MockBean
    protected AgentXSubagent subagent;

    @MockBean
    VCenterPollerTask poller;

    @MockBean
    private MOServer server;

    @MockBean
    private MuacAgent agent;

    protected MMCDataCenter getMmcDataCenter(String filename) throws IOException {
        File file = ResourceUtils.getFile(
                "classpath:json/" + filename + ".json");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, MMCDataCenter.class);
    }

    protected MMCMountPoints getMmcMountPoints(String filename) throws IOException {
        File file = ResourceUtils.getFile(
                "classpath:json/" + filename + ".json");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, MMCMountPoints.class);
    }


}
