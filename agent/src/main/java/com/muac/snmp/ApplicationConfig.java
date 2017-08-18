package com.muac.snmp;

import com.muac.snmp.mib.MuacMib;
import org.snmp4j.agent.DefaultMOServer;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOServer;
import org.snmp4j.agent.agentx.AgentX;
import org.snmp4j.agent.agentx.AgentXMessageDispatcher;
import org.snmp4j.agent.agentx.AgentXMessageDispatcherImpl;
import org.snmp4j.agent.agentx.subagent.AgentXSubagent;
import org.snmp4j.agent.mo.DefaultMOFactory;
import org.snmp4j.agent.mo.MOFactory;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.util.ThreadPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = "com.muac.snmp")
public class ApplicationConfig {

    @Value("${snmp.agent.host}")
    public String agentHost;
    @Value("${snmp.agent.port}")
    public Integer port;
    @Value("${snmp.agent.polling}")
    public long polling;
    @Value("${vcenter.folderService}")
    public String folderService;
    @Value("${rsync.service}")
    public String rsyncService;
    @Value("${rsync.box}")
    public String box;
    @Value("${threshold.minor}")
    public Double minor;
    @Value("${threshold.major}")
    public Double major;
    @Value("${agent.name}")
    private String agentName;

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean("agentxFactory")
    public AgentX agentxFactory() {
        AgentXMessageDispatcher dispatcher = new AgentXMessageDispatcherImpl();
        AgentX agentX = new AgentX(dispatcher);
        return agentX;
    }

    @Bean("server")
    @DependsOn("agentxFactory")
    public MOServer server() {
        MOServer server = new DefaultMOServer();
        server.addContext(new OctetString());
        return server;
    }

    @Bean("subAgentFactory")
    @DependsOn("server")
    public AgentXSubagent subAgentFactory(AgentX agentX, MOServer server) {
        OID agentId = new OID();
        OctetString agentDescription = new OctetString(agentName);

        AgentXSubagent subagent = new AgentXSubagent(agentX, agentId, agentDescription);
        subagent.setThreadPool(ThreadPool.create(agentName, 3));
        subagent.addMOServer(server);
        return subagent;
    }

    @Bean
    @DependsOn("subAgentFactory")
    public MuacMib mib() throws DuplicateRegistrationException {
        MOFactory factory = DefaultMOFactory.getInstance();
        DefaultMOFactory.addSNMPv2TCs(factory);
        MuacMib mib = new MuacMib(factory);
        mib.registerMOs(server(), null);
        return mib;
    }

}
