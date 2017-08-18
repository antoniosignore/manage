package com.muac.snmp.agent;

import com.muac.snmp.mib.MuacMib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.agent.BaseAgent;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOServer;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.agentx.AgentXProtocol;
import org.snmp4j.agent.agentx.AgentXSession;
import org.snmp4j.agent.agentx.subagent.AgentXSubagent;
import org.snmp4j.agent.agentx.subagent.RegistrationCallback;
import org.snmp4j.agent.mo.MOTable;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.agent.mo.snmp.SNMPv2MIB.SysUpTimeImpl;
import org.snmp4j.agent.mo.snmp4j.Snmp4jConfigMib;
import org.snmp4j.agent.mo.snmp4j.Snmp4jLogMib;
import org.snmp4j.log.Log4jLogFactory;
import org.snmp4j.log.LogFactory;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.ConnectionOrientedTransportMapping;
import org.snmp4j.transport.TransportStateEvent;
import org.snmp4j.transport.TransportStateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

import static org.snmp4j.transport.TransportStateEvent.*;


@Service
public class MuacAgent implements TransportStateListener, RegistrationCallback {

    private static final Logger logger = LoggerFactory.getLogger(BaseAgent.class);

    static {
        LogFactory.setLogFactory(new Log4jLogFactory());
    }

    private final SysUpTimeImpl sessionContextUpTime = new SysUpTimeImpl();
    @Value("${agent.name}")
    private String agentName;
    @Value("${agentX.masterAddress}")
    private String agentXMasterAddress;
    @Value("${agentX.pingDelay:30}")
    private int agentXPingDelay;
    @Value("${agentX.timeout:3}")
    private int agentXTimeout;
    @Value("${agentX.reconnectTimeout:3}")
    private int agentXReconnectTimeout;
    @Value("${agentX.reconnectAttempts:20000}")
    private int agentXReconnectAttempts;
    @Autowired
    private AgentXSubagent subagent;
    @Autowired
    private MOServer server;
    @Autowired
    private MuacMib mib;
    private Address masterAddress;
    private Address localAddress;
    private AgentXSession session;
    private int sessionId = 0;
    private Snmp4jConfigMib snmp4jConfigMib;
    private Snmp4jLogMib snmp4jLogMib;

    private static OctetString getSessionContext(int sessionId) {
        return new OctetString("session=" + sessionId);
    }

    @PostConstruct
    void init() {

        agentXReconnectTimeout = Math.max(0, agentXReconnectTimeout);
        agentXTimeout = Math.max(0, agentXTimeout);
        masterAddress = new TcpAddress(agentXMasterAddress);
        localAddress = new TcpAddress();

        unregisterSessionDependent();
        session = new AgentXSession(++sessionId);
        session.setTimeout((byte) agentXTimeout);
        subagent.setPingDelay(agentXPingDelay);

        if (connectWithRetry(masterAddress)) {
            subagent.notify(null, SnmpConstants.warmStart,
                    new VariableBinding[]{
                            new VariableBinding(SnmpConstants.sysDescr,
                                    new OctetString(agentName))});
        } else {
            halt();
        }
    }

    protected boolean connectWithRetry(Address masterAddress) {
        boolean isConnected = connect(masterAddress);
        if (!isConnected) {
            if (agentXReconnectAttempts < 0) {
                while (!isConnected) {
                    sleep(agentXReconnectTimeout);
                    isConnected = connect(masterAddress);
                }
            } else {
                for (int i = 0; (i < agentXReconnectAttempts) && !isConnected; ++i) {
                    sleep(agentXReconnectTimeout);
                    logger.info(this + ": reconnect attempt {} of {},",
                            (i + 1), agentXReconnectAttempts);
                    isConnected = connect(masterAddress);
                }
            }
        }
        return isConnected;
    }

    protected boolean connect(Address masterAddress) {
        try {
            int status = subagent.connect(masterAddress, localAddress, session);
            if (status == AgentXProtocol.AGENTX_SUCCESS) {
                logger.info(this + ": connected to {}, session {}({}).",
                        masterAddress, session.getSessionID(), sessionId);
                session.setClosed(false);
                subagent.addAgentCaps(session, new OctetString(),
                        new OID("1.3.6.1.4.1.2363.17.1"), new OctetString(agentName));

                registerSessionDependent();

                subagent.registerRegions(session, new OctetString(), null, this);
                TimeTicks upTime = new TimeTicks();
                subagent.registerRegions(session, getSessionContext(session.getSessionID()), upTime, this);
                sessionContextUpTime.setValue(upTime);
                ((ConnectionOrientedTransportMapping) session.getPeer().getTransport()).
                        addTransportStateListener(this);
                return true;
            } else {
                logger.error(this + ": Failed to connect to {}; status={}.",
                        masterAddress, status);
            }
        } catch (IOException ex) {
            logger.error(this + ": Failed to connect to {}; {}.", masterAddress, ex);
            halt();
        }
        return false;
    }

    protected void registerSessionDependent() {
        try {
            OctetString sessionContext = getSessionContext(session.getSessionID());
            server.addContext(sessionContext);
            snmp4jConfigMib = new Snmp4jConfigMib(sessionContextUpTime);
            snmp4jConfigMib.registerMOs(server, sessionContext);
            snmp4jLogMib = new Snmp4jLogMib();
            snmp4jLogMib.registerMOs(server, sessionContext);
        } catch (DuplicateRegistrationException ex) {
            logger.error(this + ": Ignoring {}.", ex);
        }
    }

    protected void unregisterSessionDependent() {
        if (session != null) {
            OctetString sessionContext = getSessionContext(session.getSessionID());
            server.removeContext(sessionContext);
            if (snmp4jConfigMib != null) {
                snmp4jConfigMib.unregisterMOs(server, sessionContext);
            }
            if (snmp4jLogMib != null) {
                snmp4jLogMib.unregisterMOs(server, sessionContext);
            }
        }
    }

    @Override
    public void connectionStateChanged(TransportStateEvent tse) {
        String msg = this + ": connection state changed to {}.";
        switch (tse.getNewState()) {
            case STATE_UNKNOWN:
                logger.info(msg, "Unknown");
                break;
            case STATE_CONNECTED:
                logger.info(msg, "StateConnected");
                break;
            case STATE_DISCONNECTED_REMOTELY:
                logger.info(msg, "StateDisconnectedRemotely");
                reconnect(tse);
                break;
            case STATE_DISCONNECTED_TIMEOUT:
                logger.info(msg, "StateDisconnectedTimeout");
                break;
            case STATE_CLOSED:
                logger.info(msg, "StateClosed");
                break;
            default:
                logger.info(msg, "Undefined state");
        }
    }

    private void reconnect(final TransportStateEvent tse) {
        session.setClosed(true);
        unregisterSessionDependent();
        ((ConnectionOrientedTransportMapping) session.getPeer().getTransport()).
                removeTransportStateListener(this);

        if (agentXReconnectAttempts == 0) {
            halt();
        } else {
            Thread t = new Thread(() -> {
                if (!connectWithRetry(tse.getPeerAddress())) {
                    halt();
                }
            });
            t.start();
        }
    }

    @Override
    public void registrationEvent(OctetString context,
                                  ManagedObject mo, int status) {
        if (status != AgentXProtocol.AGENTX_SUCCESS) {
            // optionally remove objects from the server,
            // which could not be registered with the master agent here, but
            // that would prevent their registration after a reconnect:
            //      server.unregister(mo, context);
        }
    }

    @Override
    public boolean tableRegistrationEvent(OctetString os, MOTable mot, MOTableRow motr, boolean bln, int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void unregistrationEvent(OctetString os, ManagedObject mo, int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void tableUnregistrationEvent(OctetString os, MOTable mot, MOTableRow motr, boolean bln, int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void sleep(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException ex) {
        }
    }

    private void halt() {
        logger.error(this + ": Giving up.");
        System.exit(-1);
    }

    @PreDestroy
    void destroy() {
        try {
            subagent.close(session, AgentXProtocol.REASON_SHUTDOWN);
        } catch (IOException ex) {
        }
    }

    @Override
    public String toString() {
        return "[" + agentName + "]";
    }
}