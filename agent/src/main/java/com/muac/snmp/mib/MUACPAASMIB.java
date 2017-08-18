
package com.muac.snmp.mib;
//--AgentGen BEGIN=_BEGIN
//--AgentGen END

import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.*;
import org.snmp4j.agent.mo.snmp.smi.*;
import org.snmp4j.agent.mo.snmp.tc.TextualConvention;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import org.snmp4j.smi.*;


//--AgentGen BEGIN=_IMPORT
//--AgentGen END

public class MUACPAASMIB
//--AgentGen BEGIN=_EXTENDS
//--AgentGen END
        implements MOGroup
//--AgentGen BEGIN=_IMPLEMENTS
//--AgentGen END
{

    /**
     * OID of this MIB module for usage which can be
     * used for its identification.
     */
    public static final OID oidMuacPaasMib =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1});

    //--AgentGen BEGIN=_STATIC
//--AgentGen END
    // Identities
    public static final OID oidMpaasInfrastructure =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 1});

    // Constants
    public static final OID oidMpaasVmWareCluster =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 1, 1});
    public static final OID oidMpaasFunctional =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 2});
    // Scalars
    public static final OID oidMpaasClusterStatusInt =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 1, 1, 1, 0});
    public static final OID oidMpaasClusterStatusAct =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 1, 1, 2, 0});
    public static final OID oidMpaasClusterStatusDetails =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 1, 1, 3, 0});
    public static final OID oidMpaasClusterIsReachable =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 1, 1, 4, 0});
    // Notifications
    public static final OID oidMpaasClusterStatusTrap =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 0, 1});
    public static final OID oidTrapVarMpaasClusterStatusInt =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 1, 1, 1});
    // Tables
    public static final OID oidTrapVarMpaasClusterStatusAct =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 1, 1, 2});
    public static final OID oidTrapVarMpaasClusterStatusDetails =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 1, 1, 3});
    public static final OID oidTrapVarMpaasClusterIsReachable =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 1, 1, 4});
    public static final OID oidMpaasVirtualMachineStatusTrap =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 0, 2});
    public static final OID oidTrapVarMpaasVirtualMachineStatusInt =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 1, 2, 1, 2});
    public static final OID oidTrapVarMpaasVirtualMachineStatusAct =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 1, 2, 1, 3});
    public static final OID oidTrapVarMpaasVirtualMachineStatusDetails =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 1, 2, 1, 4});
    public static final OID oidTrapVarMpaasVirtualMachineIsReachable =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 1, 2, 1, 5});
    public static final OID oidMpaasDataStreamStatusTrap =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 0, 11});
    public static final OID oidTrapVarMpaasDataStreamStatusInt =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 2, 1, 1, 3});
    public static final OID oidTrapVarMpaasDataStreamStatusAct =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 2, 1, 1, 4});
    public static final OID oidTrapVarMpaasDataStreamStatusDetails =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 2, 1, 1, 5});
    public static final OID oidTrapVarMpaasDataStreamIsReachable =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 2, 1, 1, 6});
    // Tables
    public static final OID oidMpaasVirtualMachineEntry =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 1, 2, 1});
    // Index OID definitions
    public static final OID oidMpaasVirtualMachineName =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 1, 2, 1, 1});


    // Enumerations
    // Column TC definitions for mpaasVirtualMachineEntry:
    public static final String tcModuleMuacPaasMib = "MUAC-PAAS-MIB";
    public static final String tcDefMpaasStatusIntendedType = "MpaasStatusIntendedType";
    public static final String tcDefMpaasStatusActualType = "MpaasStatusActualType";
    public static final String tcDefMpaasStatusDetailsText = "MpaasStatusDetailsText";
    public static final String tcModuleSNMPv2Tc = "SNMPv2-TC";
    public static final String tcDefTruthValue = "TruthValue";
    public static final String tcDefMpaasGenericNameType = "MpaasGenericNameType";
    // Column sub-identifier definitions for mpaasVirtualMachineEntry:
    public static final int colMpaasVirtualMachineStatusInt = 2;
    public static final int colMpaasVirtualMachineStatusAct = 3;
    public static final int colMpaasVirtualMachineStatusDetails = 4;
    public static final int colMpaasVirtualMachineIsReachable = 5;
    public static final int colMpaasVirtualMachineCategory = 6;
    // Column index definitions for mpaasVirtualMachineEntry:
    public static final int idxMpaasVirtualMachineStatusInt = 0;
    public static final int idxMpaasVirtualMachineStatusAct = 1;
    public static final int idxMpaasVirtualMachineStatusDetails = 2;
    public static final int idxMpaasVirtualMachineIsReachable = 3;
    public static final int idxMpaasVirtualMachineCategory = 4;
    public static final OID oidMpaasDataStreamEntry =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 2, 1, 1});
    // Index OID definitions
    public static final OID oidMpaasDataStreamHost =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 2, 1, 1, 1});
    public static final OID oidMpaasDataStreamName =
            new OID(new int[]{1, 3, 6, 1, 4, 1, 2363, 3, 17, 1, 1, 2, 1, 1, 2});
    // Column TC definitions for mpaasDataStreamEntry:
    public static final String tcDefMpaasCriticalityType = "MpaasCriticalityType";
    // Column sub-identifier definitions for mpaasDataStreamEntry:
    public static final int colMpaasDataStreamStatusInt = 3;
    public static final int colMpaasDataStreamStatusAct = 4;
    public static final int colMpaasDataStreamStatusDetails = 5;
    public static final int colMpaasDataStreamIsReachable = 6;
    public static final int colMpaasDataStreamProducerSystem = 7;
    public static final int colMpaasDataStreamConsumerSystem = 8;
    public static final int colMpaasDataStreamCriticality = 9;
    // Column index definitions for mpaasDataStreamEntry:
    public static final int idxMpaasDataStreamStatusInt = 0;
    public static final int idxMpaasDataStreamStatusAct = 1;
    public static final int idxMpaasDataStreamStatusDetails = 2;
    public static final int idxMpaasDataStreamIsReachable = 3;
    public static final int idxMpaasDataStreamProducerSystem = 4;
    public static final int idxMpaasDataStreamConsumerSystem = 5;
    public static final int idxMpaasDataStreamCriticality = 6;
    private static final LogAdapter LOGGER =
            LogFactory.getLogger(MUACPAASMIB.class);
    // TextualConventions
    private static final String TC_MODULE_MUAC_PAAS_MIB = "MUAC-PAAS-MIB";
    private static final String TC_MODULE_SNMPV2_TC = "SNMPv2-TC";
    private static final String TC_MPAASSTATUSDETAILSTEXT = "MpaasStatusDetailsText";
    private static final String TC_MPAASGENERICNAMETYPE = "MpaasGenericNameType";
    private static final String TC_MPAASSTATUSINTENDEDTYPE = "MpaasStatusIntendedType";
    private static final String TC_MPAASSTATUSACTUALTYPE = "MpaasStatusActualType";
    private static final String TC_TRUTHVALUE = "TruthValue";
    private static final String TC_MPAASCRITICALITYTYPE = "MpaasCriticalityType";
    // Factory
    private MOFactory moFactory =
            DefaultMOFactory.getInstance();
    // Scalars
    private MOScalar<Integer32> mpaasClusterStatusInt;
    private MOScalar<Integer32> mpaasClusterStatusAct;
    private MOScalar<OctetString> mpaasClusterStatusDetails;
    private MOScalar<Integer32> mpaasClusterIsReachable;
    private MOTableSubIndex[] mpaasVirtualMachineEntryIndexes;
    private MOTableIndex mpaasVirtualMachineEntryIndex;
    private MOTable<MpaasVirtualMachineEntryRow,
            MOColumn,
            MOTableModel<MpaasVirtualMachineEntryRow>> mpaasVirtualMachineEntry;
    private MOTableModel<MpaasVirtualMachineEntryRow> mpaasVirtualMachineEntryModel;
    private MOTableSubIndex[] mpaasDataStreamEntryIndexes;
    private MOTableIndex mpaasDataStreamEntryIndex;

    private MOTable<MpaasDataStreamEntryRow,
            MOColumn,
            MOTableModel<MpaasDataStreamEntryRow>> mpaasDataStreamEntry;
    private MOTableModel<MpaasDataStreamEntryRow> mpaasDataStreamEntryModel;


//--AgentGen BEGIN=_MEMBERS
//--AgentGen END

    /**
     * Constructs a MUAC-PAAS-MIB instance without actually creating its
     * <code>ManagedObject</code> instances. This has to be done in a
     * sub-class constructor or after construction by calling
     * {@link #createMO(MOFactory moFactory)}.
     */
    protected MUACPAASMIB() {
//--AgentGen BEGIN=_DEFAULTCONSTRUCTOR
//--AgentGen END
    }

    /**
     * Constructs a MUAC-PAAS-MIB instance and actually creates its
     * <code>ManagedObject</code> instances using the supplied
     * <code>MOFactory</code> (by calling
     * {@link #createMO(MOFactory moFactory)}).
     *
     * @param moFactory the <code>MOFactory</code> to be used to create the
     *                  managed objects for this module.
     */
    public MUACPAASMIB(MOFactory moFactory) {
        this();
        createMO(moFactory);
//--AgentGen BEGIN=_FACTORYCONSTRUCTOR
//--AgentGen END
    }

//--AgentGen BEGIN=_CONSTRUCTORS
//--AgentGen END

    /**
     * Create the ManagedObjects defined for this MIB module
     * using the specified {@link MOFactory}.
     *
     * @param moFactory the <code>MOFactory</code> instance to use for object
     *                  creation.
     */
    protected void createMO(MOFactory moFactory) {
        addTCsToFactory(moFactory);
        mpaasClusterStatusInt =
                moFactory.createScalar(oidMpaasClusterStatusInt,
                        moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                        null,
                        TC_MODULE_MUAC_PAAS_MIB, TC_MPAASSTATUSINTENDEDTYPE);
        mpaasClusterStatusAct =
                moFactory.createScalar(oidMpaasClusterStatusAct,
                        moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                        null,
                        TC_MODULE_MUAC_PAAS_MIB, TC_MPAASSTATUSACTUALTYPE);
        mpaasClusterStatusDetails =
                moFactory.createScalar(oidMpaasClusterStatusDetails,
                        moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                        null,
                        TC_MODULE_MUAC_PAAS_MIB, TC_MPAASSTATUSDETAILSTEXT);
        mpaasClusterIsReachable =
                moFactory.createScalar(oidMpaasClusterIsReachable,
                        moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                        null,
                        TC_MODULE_SNMPV2_TC, TC_TRUTHVALUE);
        createMpaasVirtualMachineEntry(moFactory);
        createMpaasDataStreamEntry(moFactory);
    }

    public MOScalar<Integer32> getMpaasClusterStatusInt() {
        return mpaasClusterStatusInt;
    }

    public MOScalar<Integer32> getMpaasClusterStatusAct() {
        return mpaasClusterStatusAct;
    }

    public MOScalar<OctetString> getMpaasClusterStatusDetails() {
        return mpaasClusterStatusDetails;
    }

    public MOScalar<Integer32> getMpaasClusterIsReachable() {
        return mpaasClusterIsReachable;
    }


    public MOTable<MpaasVirtualMachineEntryRow, MOColumn, MOTableModel<MpaasVirtualMachineEntryRow>> getMpaasVirtualMachineEntry() {
        return mpaasVirtualMachineEntry;
    }


    @SuppressWarnings(value = {"unchecked"})
    private void createMpaasVirtualMachineEntry(MOFactory moFactory) {
        // Index definition
        mpaasVirtualMachineEntryIndexes =
                new MOTableSubIndex[]{
                        moFactory.createSubIndex(oidMpaasVirtualMachineName,
                                SMIConstants.SYNTAX_OCTET_STRING, 1, 24)
                };

        mpaasVirtualMachineEntryIndex =
                moFactory.createIndex(mpaasVirtualMachineEntryIndexes,
                        false,
                        new MOTableIndexValidator() {
                            public boolean isValidIndex(OID index) {
                                boolean isValidIndex = true;
                                //--AgentGen BEGIN=mpaasVirtualMachineEntry::isValidIndex
                                //--AgentGen END
                                return isValidIndex;
                            }
                        });

        // Columns
        MOColumn[] mpaasVirtualMachineEntryColumns = new MOColumn[5];
        mpaasVirtualMachineEntryColumns[idxMpaasVirtualMachineStatusInt] =
                moFactory.createColumn(colMpaasVirtualMachineStatusInt,
                        SMIConstants.SYNTAX_INTEGER,
                        moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                        tcModuleMuacPaasMib,
                        tcDefMpaasStatusIntendedType);
        mpaasVirtualMachineEntryColumns[idxMpaasVirtualMachineStatusAct] =
                moFactory.createColumn(colMpaasVirtualMachineStatusAct,
                        SMIConstants.SYNTAX_INTEGER,
                        moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                        tcModuleMuacPaasMib,
                        tcDefMpaasStatusActualType);
        mpaasVirtualMachineEntryColumns[idxMpaasVirtualMachineStatusDetails] =
                moFactory.createColumn(colMpaasVirtualMachineStatusDetails,
                        SMIConstants.SYNTAX_OCTET_STRING,
                        moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                        tcModuleMuacPaasMib,
                        tcDefMpaasStatusDetailsText);
        mpaasVirtualMachineEntryColumns[idxMpaasVirtualMachineIsReachable] =
                moFactory.createColumn(colMpaasVirtualMachineIsReachable,
                        SMIConstants.SYNTAX_INTEGER,
                        moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                        tcModuleSNMPv2Tc,
                        tcDefTruthValue);
        mpaasVirtualMachineEntryColumns[idxMpaasVirtualMachineCategory] =
                moFactory.createColumn(colMpaasVirtualMachineCategory,
                        SMIConstants.SYNTAX_OCTET_STRING,
                        moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                        tcModuleMuacPaasMib,
                        tcDefMpaasGenericNameType);
        // Table model
        mpaasVirtualMachineEntryModel =
                moFactory.createTableModel(oidMpaasVirtualMachineEntry,
                        mpaasVirtualMachineEntryIndex,
                        mpaasVirtualMachineEntryColumns);
        ((MOMutableTableModel<MpaasVirtualMachineEntryRow>) mpaasVirtualMachineEntryModel).setRowFactory(
                new MpaasVirtualMachineEntryRowFactory());
        mpaasVirtualMachineEntry =
                moFactory.createTable(oidMpaasVirtualMachineEntry,
                        mpaasVirtualMachineEntryIndex,
                        mpaasVirtualMachineEntryColumns,
                        mpaasVirtualMachineEntryModel);
    }

    public MOTable<MpaasDataStreamEntryRow, MOColumn, MOTableModel<MpaasDataStreamEntryRow>> getMpaasDataStreamEntry() {
        return mpaasDataStreamEntry;
    }


    @SuppressWarnings(value = {"unchecked"})
    private void createMpaasDataStreamEntry(MOFactory moFactory) {
        // Index definition
        mpaasDataStreamEntryIndexes =
                new MOTableSubIndex[]{
                        moFactory.createSubIndex(oidMpaasDataStreamHost,
                                SMIConstants.SYNTAX_OCTET_STRING, 1, 24)
                        ,
                        moFactory.createSubIndex(oidMpaasDataStreamName,
                                SMIConstants.SYNTAX_OCTET_STRING, 1, 24)
                };

        mpaasDataStreamEntryIndex =
                moFactory.createIndex(mpaasDataStreamEntryIndexes,
                        false,
                        new MOTableIndexValidator() {
                            public boolean isValidIndex(OID index) {
                                boolean isValidIndex = true;
                                //--AgentGen BEGIN=mpaasDataStreamEntry::isValidIndex
                                //--AgentGen END
                                return isValidIndex;
                            }
                        });

        // Columns
        MOColumn[] mpaasDataStreamEntryColumns = new MOColumn[7];
        mpaasDataStreamEntryColumns[idxMpaasDataStreamStatusInt] =
                moFactory.createColumn(colMpaasDataStreamStatusInt,
                        SMIConstants.SYNTAX_INTEGER,
                        moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                        tcModuleMuacPaasMib,
                        tcDefMpaasStatusIntendedType);
        mpaasDataStreamEntryColumns[idxMpaasDataStreamStatusAct] =
                moFactory.createColumn(colMpaasDataStreamStatusAct,
                        SMIConstants.SYNTAX_INTEGER,
                        moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                        tcModuleMuacPaasMib,
                        tcDefMpaasStatusActualType);
        mpaasDataStreamEntryColumns[idxMpaasDataStreamStatusDetails] =
                moFactory.createColumn(colMpaasDataStreamStatusDetails,
                        SMIConstants.SYNTAX_OCTET_STRING,
                        moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                        tcModuleMuacPaasMib,
                        tcDefMpaasStatusDetailsText);
        mpaasDataStreamEntryColumns[idxMpaasDataStreamIsReachable] =
                moFactory.createColumn(colMpaasDataStreamIsReachable,
                        SMIConstants.SYNTAX_INTEGER,
                        moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                        tcModuleSNMPv2Tc,
                        tcDefTruthValue);
        mpaasDataStreamEntryColumns[idxMpaasDataStreamProducerSystem] =
                moFactory.createColumn(colMpaasDataStreamProducerSystem,
                        SMIConstants.SYNTAX_OCTET_STRING,
                        moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                        tcModuleMuacPaasMib,
                        tcDefMpaasGenericNameType);
        mpaasDataStreamEntryColumns[idxMpaasDataStreamConsumerSystem] =
                moFactory.createColumn(colMpaasDataStreamConsumerSystem,
                        SMIConstants.SYNTAX_OCTET_STRING,
                        moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                        tcModuleMuacPaasMib,
                        tcDefMpaasGenericNameType);
        mpaasDataStreamEntryColumns[idxMpaasDataStreamCriticality] =
                moFactory.createColumn(colMpaasDataStreamCriticality,
                        SMIConstants.SYNTAX_INTEGER,
                        moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                        tcModuleMuacPaasMib,
                        tcDefMpaasCriticalityType);
        // Table model
        mpaasDataStreamEntryModel =
                moFactory.createTableModel(oidMpaasDataStreamEntry,
                        mpaasDataStreamEntryIndex,
                        mpaasDataStreamEntryColumns);
        ((MOMutableTableModel<MpaasDataStreamEntryRow>) mpaasDataStreamEntryModel).setRowFactory(
                new MpaasDataStreamEntryRowFactory());
        mpaasDataStreamEntry =
                moFactory.createTable(oidMpaasDataStreamEntry,
                        mpaasDataStreamEntryIndex,
                        mpaasDataStreamEntryColumns,
                        mpaasDataStreamEntryModel);
    }


    public void registerMOs(MOServer server, OctetString context)
            throws DuplicateRegistrationException {
        // Scalar Objects
        server.register(this.mpaasClusterStatusInt, context);
        server.register(this.mpaasClusterStatusAct, context);
        server.register(this.mpaasClusterStatusDetails, context);
        server.register(this.mpaasClusterIsReachable, context);
        server.register(this.mpaasVirtualMachineEntry, context);
        server.register(this.mpaasDataStreamEntry, context);
//--AgentGen BEGIN=_registerMOs
//--AgentGen END
    }

    public void unregisterMOs(MOServer server, OctetString context) {
        // Scalar Objects
        server.unregister(this.mpaasClusterStatusInt, context);
        server.unregister(this.mpaasClusterStatusAct, context);
        server.unregister(this.mpaasClusterStatusDetails, context);
        server.unregister(this.mpaasClusterIsReachable, context);
        server.unregister(this.mpaasVirtualMachineEntry, context);
        server.unregister(this.mpaasDataStreamEntry, context);
//--AgentGen BEGIN=_unregisterMOs
//--AgentGen END
    }

    // Notifications
    public void mpaasClusterStatusTrap(NotificationOriginator notificationOriginator,
                                       OctetString context, VariableBinding[] vbs) {
        if (vbs.length < 4) {
            throw new IllegalArgumentException("Too few notification objects (mpaasClusterStatusTrap): " +
                    vbs.length + "<4");
        }
        // mpaasClusterStatusInt
        if (!(vbs[0].getOid().startsWith(oidTrapVarMpaasClusterStatusInt))) {
            throw new IllegalArgumentException("Variable 0 (mpaasClusterStatusInt)) has wrong OID: " + vbs[0].getOid() +
                    " does not start with " + oidTrapVarMpaasClusterStatusInt);
        }

        // mpaasClusterStatusAct
        if (!(vbs[1].getOid().startsWith(oidTrapVarMpaasClusterStatusAct))) {
            throw new IllegalArgumentException("Variable 1 (mpaasClusterStatusAct)) has wrong OID: " + vbs[1].getOid() +
                    " does not start with " + oidTrapVarMpaasClusterStatusAct);
        }

        // mpaasClusterStatusDetails
        if (!(vbs[2].getOid().startsWith(oidTrapVarMpaasClusterStatusDetails))) {
            throw new IllegalArgumentException("Variable 2 (mpaasClusterStatusDetails)) has wrong OID: " + vbs[2].getOid() +
                    " does not start with " + oidTrapVarMpaasClusterStatusDetails);
        }
        {
            OctetString os = (OctetString) vbs[2].getVariable();
            if (!(((os.length() >= 0) && (os.length() <= 160)))) {
                throw new IllegalArgumentException("Illegal length of variable 2 (mpaasClusterStatusDetails)): " + os.length());
            }
        }

        // mpaasClusterIsReachable
        if (!(vbs[3].getOid().startsWith(oidTrapVarMpaasClusterIsReachable))) {
            throw new IllegalArgumentException("Variable 3 (mpaasClusterIsReachable)) has wrong OID: " + vbs[3].getOid() +
                    " does not start with " + oidTrapVarMpaasClusterIsReachable);
        }

        notificationOriginator.notify(context, oidMpaasClusterStatusTrap, vbs);
    }


    public void mpaasVirtualMachineStatusTrap(NotificationOriginator notificationOriginator,
                                              OctetString context, VariableBinding[] vbs) {
        if (vbs.length < 4) {
            throw new IllegalArgumentException("Too few notification objects (mpaasVirtualMachineStatusTrap): " +
                    vbs.length + "<4");
        }
        // mpaasVirtualMachineStatusInt
        if (!(vbs[0].getOid().startsWith(oidTrapVarMpaasVirtualMachineStatusInt))) {
            throw new IllegalArgumentException("Variable 0 (mpaasVirtualMachineStatusInt)) has wrong OID: " + vbs[0].getOid() +
                    " does not start with " + oidTrapVarMpaasVirtualMachineStatusInt);
        }
        if (!mpaasVirtualMachineEntryIndex.isValidIndex(mpaasVirtualMachineEntry.getIndexPart(vbs[0].getOid()))) {
            throw new IllegalArgumentException("Illegal index for variable 0 (mpaasVirtualMachineStatusInt)) specified: " +
                    mpaasVirtualMachineEntry.getIndexPart(vbs[0].getOid()));
        }

        // mpaasVirtualMachineStatusAct
        if (!(vbs[1].getOid().startsWith(oidTrapVarMpaasVirtualMachineStatusAct))) {
            throw new IllegalArgumentException("Variable 1 (mpaasVirtualMachineStatusAct)) has wrong OID: " + vbs[1].getOid() +
                    " does not start with " + oidTrapVarMpaasVirtualMachineStatusAct);
        }
        if (!mpaasVirtualMachineEntryIndex.isValidIndex(mpaasVirtualMachineEntry.getIndexPart(vbs[1].getOid()))) {
            throw new IllegalArgumentException("Illegal index for variable 1 (mpaasVirtualMachineStatusAct)) specified: " +
                    mpaasVirtualMachineEntry.getIndexPart(vbs[1].getOid()));
        }

        // mpaasVirtualMachineStatusDetails
        if (!(vbs[2].getOid().startsWith(oidTrapVarMpaasVirtualMachineStatusDetails))) {
            throw new IllegalArgumentException("Variable 2 (mpaasVirtualMachineStatusDetails)) has wrong OID: " + vbs[2].getOid() +
                    " does not start with " + oidTrapVarMpaasVirtualMachineStatusDetails);
        }
        if (!mpaasVirtualMachineEntryIndex.isValidIndex(mpaasVirtualMachineEntry.getIndexPart(vbs[2].getOid()))) {
            throw new IllegalArgumentException("Illegal index for variable 2 (mpaasVirtualMachineStatusDetails)) specified: " +
                    mpaasVirtualMachineEntry.getIndexPart(vbs[2].getOid()));
        }
        {
            OctetString os = (OctetString) vbs[2].getVariable();
            if (!(((os.length() >= 0) && (os.length() <= 160)))) {
                throw new IllegalArgumentException("Illegal length of variable 2 (mpaasVirtualMachineStatusDetails)): " + os.length());
            }
        }

        // mpaasVirtualMachineIsReachable
        if (!(vbs[3].getOid().startsWith(oidTrapVarMpaasVirtualMachineIsReachable))) {
            throw new IllegalArgumentException("Variable 3 (mpaasVirtualMachineIsReachable)) has wrong OID: " + vbs[3].getOid() +
                    " does not start with " + oidTrapVarMpaasVirtualMachineIsReachable);
        }
        if (!mpaasVirtualMachineEntryIndex.isValidIndex(mpaasVirtualMachineEntry.getIndexPart(vbs[3].getOid()))) {
            throw new IllegalArgumentException("Illegal index for variable 3 (mpaasVirtualMachineIsReachable)) specified: " +
                    mpaasVirtualMachineEntry.getIndexPart(vbs[3].getOid()));
        }

        notificationOriginator.notify(context, oidMpaasVirtualMachineStatusTrap, vbs);
    }


    public void mpaasDataStreamStatusTrap(NotificationOriginator notificationOriginator,
                                          OctetString context, VariableBinding[] vbs) {
        if (vbs.length < 4) {
            throw new IllegalArgumentException("Too few notification objects (mpaasDataStreamStatusTrap): " +
                    vbs.length + "<4");
        }
        // mpaasDataStreamStatusInt
        if (!(vbs[0].getOid().startsWith(oidTrapVarMpaasDataStreamStatusInt))) {
            throw new IllegalArgumentException("Variable 0 (mpaasDataStreamStatusInt)) has wrong OID: " + vbs[0].getOid() +
                    " does not start with " + oidTrapVarMpaasDataStreamStatusInt);
        }
        if (!mpaasDataStreamEntryIndex.isValidIndex(mpaasDataStreamEntry.getIndexPart(vbs[0].getOid()))) {
            throw new IllegalArgumentException("Illegal index for variable 0 (mpaasDataStreamStatusInt)) specified: " +
                    mpaasDataStreamEntry.getIndexPart(vbs[0].getOid()));
        }

        // mpaasDataStreamStatusAct
        if (!(vbs[1].getOid().startsWith(oidTrapVarMpaasDataStreamStatusAct))) {
            throw new IllegalArgumentException("Variable 1 (mpaasDataStreamStatusAct)) has wrong OID: " + vbs[1].getOid() +
                    " does not start with " + oidTrapVarMpaasDataStreamStatusAct);
        }
        if (!mpaasDataStreamEntryIndex.isValidIndex(mpaasDataStreamEntry.getIndexPart(vbs[1].getOid()))) {
            throw new IllegalArgumentException("Illegal index for variable 1 (mpaasDataStreamStatusAct)) specified: " +
                    mpaasDataStreamEntry.getIndexPart(vbs[1].getOid()));
        }

        // mpaasDataStreamStatusDetails
        if (!(vbs[2].getOid().startsWith(oidTrapVarMpaasDataStreamStatusDetails))) {
            throw new IllegalArgumentException("Variable 2 (mpaasDataStreamStatusDetails)) has wrong OID: " + vbs[2].getOid() +
                    " does not start with " + oidTrapVarMpaasDataStreamStatusDetails);
        }
        if (!mpaasDataStreamEntryIndex.isValidIndex(mpaasDataStreamEntry.getIndexPart(vbs[2].getOid()))) {
            throw new IllegalArgumentException("Illegal index for variable 2 (mpaasDataStreamStatusDetails)) specified: " +
                    mpaasDataStreamEntry.getIndexPart(vbs[2].getOid()));
        }
        {
            OctetString os = (OctetString) vbs[2].getVariable();
            if (!(((os.length() >= 0) && (os.length() <= 160)))) {
                throw new IllegalArgumentException("Illegal length of variable 2 (mpaasDataStreamStatusDetails)): " + os.length());
            }
        }

        // mpaasDataStreamIsReachable
        if (!(vbs[3].getOid().startsWith(oidTrapVarMpaasDataStreamIsReachable))) {
            throw new IllegalArgumentException("Variable 3 (mpaasDataStreamIsReachable)) has wrong OID: " + vbs[3].getOid() +
                    " does not start with " + oidTrapVarMpaasDataStreamIsReachable);
        }
        if (!mpaasDataStreamEntryIndex.isValidIndex(mpaasDataStreamEntry.getIndexPart(vbs[3].getOid()))) {
            throw new IllegalArgumentException("Illegal index for variable 3 (mpaasDataStreamIsReachable)) specified: " +
                    mpaasDataStreamEntry.getIndexPart(vbs[3].getOid()));
        }

        notificationOriginator.notify(context, oidMpaasDataStreamStatusTrap, vbs);
    }


    // Scalars

    // Value Validators


    // Rows and Factories

    // Textual Definitions of MIB module MUAC-PAAS-MIB
    protected void addTCsToFactory(MOFactory moFactory) {
        moFactory.addTextualConvention(new MpaasStatusDetailsText());
        moFactory.addTextualConvention(new MpaasGenericNameType());
        moFactory.addTextualConvention(new MpaasStatusIntendedType());
        moFactory.addTextualConvention(new MpaasStatusActualType());
        moFactory.addTextualConvention(new MpaasCriticalityType());
    }

    // Textual Definitions of other MIB modules
    public void addImportedTCsToFactory(MOFactory moFactory) {
    }

    public class MpaasVirtualMachineEntryRow extends DefaultMOMutableRow2PC {

        //--AgentGen BEGIN=mpaasVirtualMachineEntry::RowMembers
        //--AgentGen END

        public MpaasVirtualMachineEntryRow(OID index, Variable[] values) {
            super(index, values);
            //--AgentGen BEGIN=mpaasVirtualMachineEntry::RowConstructor
            //--AgentGen END
        }

        public Integer32 getMpaasVirtualMachineStatusInt() {
            //--AgentGen BEGIN=mpaasVirtualMachineEntry::getMpaasVirtualMachineStatusInt
            //--AgentGen END
            return (Integer32) super.getValue(idxMpaasVirtualMachineStatusInt);
        }

        public void setMpaasVirtualMachineStatusInt(Integer32 newColValue) {
            //--AgentGen BEGIN=mpaasVirtualMachineEntry::setMpaasVirtualMachineStatusInt
            //--AgentGen END
            super.setValue(idxMpaasVirtualMachineStatusInt, newColValue);
        }

        public Integer32 getMpaasVirtualMachineStatusAct() {
            //--AgentGen BEGIN=mpaasVirtualMachineEntry::getMpaasVirtualMachineStatusAct
            //--AgentGen END
            return (Integer32) super.getValue(idxMpaasVirtualMachineStatusAct);
        }

        public void setMpaasVirtualMachineStatusAct(Integer32 newColValue) {
            //--AgentGen BEGIN=mpaasVirtualMachineEntry::setMpaasVirtualMachineStatusAct
            //--AgentGen END
            super.setValue(idxMpaasVirtualMachineStatusAct, newColValue);
        }

        public OctetString getMpaasVirtualMachineStatusDetails() {
            //--AgentGen BEGIN=mpaasVirtualMachineEntry::getMpaasVirtualMachineStatusDetails
            //--AgentGen END
            return (OctetString) super.getValue(idxMpaasVirtualMachineStatusDetails);
        }

        public void setMpaasVirtualMachineStatusDetails(OctetString newColValue) {
            //--AgentGen BEGIN=mpaasVirtualMachineEntry::setMpaasVirtualMachineStatusDetails
            //--AgentGen END
            super.setValue(idxMpaasVirtualMachineStatusDetails, newColValue);
        }

        public Integer32 getMpaasVirtualMachineIsReachable() {
            //--AgentGen BEGIN=mpaasVirtualMachineEntry::getMpaasVirtualMachineIsReachable
            //--AgentGen END
            return (Integer32) super.getValue(idxMpaasVirtualMachineIsReachable);
        }

        public void setMpaasVirtualMachineIsReachable(Integer32 newColValue) {
            //--AgentGen BEGIN=mpaasVirtualMachineEntry::setMpaasVirtualMachineIsReachable
            //--AgentGen END
            super.setValue(idxMpaasVirtualMachineIsReachable, newColValue);
        }

        public OctetString getMpaasVirtualMachineCategory() {
            //--AgentGen BEGIN=mpaasVirtualMachineEntry::getMpaasVirtualMachineCategory
            //--AgentGen END
            return (OctetString) super.getValue(idxMpaasVirtualMachineCategory);
        }

        public void setMpaasVirtualMachineCategory(OctetString newColValue) {
            //--AgentGen BEGIN=mpaasVirtualMachineEntry::setMpaasVirtualMachineCategory
            //--AgentGen END
            super.setValue(idxMpaasVirtualMachineCategory, newColValue);
        }

        public Variable getValue(int column) {
            //--AgentGen BEGIN=mpaasVirtualMachineEntry::RowGetValue
            //--AgentGen END
            switch (column) {
                case idxMpaasVirtualMachineStatusInt:
                    return getMpaasVirtualMachineStatusInt();
                case idxMpaasVirtualMachineStatusAct:
                    return getMpaasVirtualMachineStatusAct();
                case idxMpaasVirtualMachineStatusDetails:
                    return getMpaasVirtualMachineStatusDetails();
                case idxMpaasVirtualMachineIsReachable:
                    return getMpaasVirtualMachineIsReachable();
                case idxMpaasVirtualMachineCategory:
                    return getMpaasVirtualMachineCategory();
                default:
                    return super.getValue(column);
            }
        }

        public void setValue(int column, Variable value) {
            //--AgentGen BEGIN=mpaasVirtualMachineEntry::RowSetValue
            //--AgentGen END
            switch (column) {
                case idxMpaasVirtualMachineStatusInt:
                    setMpaasVirtualMachineStatusInt((Integer32) value);
                    break;
                case idxMpaasVirtualMachineStatusAct:
                    setMpaasVirtualMachineStatusAct((Integer32) value);
                    break;
                case idxMpaasVirtualMachineStatusDetails:
                    setMpaasVirtualMachineStatusDetails((OctetString) value);
                    break;
                case idxMpaasVirtualMachineIsReachable:
                    setMpaasVirtualMachineIsReachable((Integer32) value);
                    break;
                case idxMpaasVirtualMachineCategory:
                    setMpaasVirtualMachineCategory((OctetString) value);
                    break;
                default:
                    super.setValue(column, value);
            }
        }

        //--AgentGen BEGIN=mpaasVirtualMachineEntry::Row
        //--AgentGen END
    }

    class MpaasVirtualMachineEntryRowFactory
            implements MOTableRowFactory<MpaasVirtualMachineEntryRow> {
        public synchronized MpaasVirtualMachineEntryRow createRow(OID index, Variable[] values)
                throws UnsupportedOperationException {
            MpaasVirtualMachineEntryRow row =
                    new MpaasVirtualMachineEntryRow(index, values);
            //--AgentGen BEGIN=mpaasVirtualMachineEntry::createRow
            //--AgentGen END
            return row;
        }

        public synchronized void freeRow(MpaasVirtualMachineEntryRow row) {
            //--AgentGen BEGIN=mpaasVirtualMachineEntry::freeRow
            //--AgentGen END
        }

        //--AgentGen BEGIN=mpaasVirtualMachineEntry::RowFactory
        //--AgentGen END
    }


//--AgentGen BEGIN=_METHODS
//--AgentGen END

    public class MpaasDataStreamEntryRow extends DefaultMOMutableRow2PC {

        //--AgentGen BEGIN=mpaasDataStreamEntry::RowMembers
        //--AgentGen END

        public MpaasDataStreamEntryRow(OID index, Variable[] values) {
            super(index, values);
            //--AgentGen BEGIN=mpaasDataStreamEntry::RowConstructor
            //--AgentGen END
        }

        public Integer32 getMpaasDataStreamStatusInt() {
            //--AgentGen BEGIN=mpaasDataStreamEntry::getMpaasDataStreamStatusInt
            //--AgentGen END
            return (Integer32) super.getValue(idxMpaasDataStreamStatusInt);
        }

        public void setMpaasDataStreamStatusInt(Integer32 newColValue) {
            //--AgentGen BEGIN=mpaasDataStreamEntry::setMpaasDataStreamStatusInt
            //--AgentGen END
            super.setValue(idxMpaasDataStreamStatusInt, newColValue);
        }

        public Integer32 getMpaasDataStreamStatusAct() {
            //--AgentGen BEGIN=mpaasDataStreamEntry::getMpaasDataStreamStatusAct
            //--AgentGen END
            return (Integer32) super.getValue(idxMpaasDataStreamStatusAct);
        }

        public void setMpaasDataStreamStatusAct(Integer32 newColValue) {
            //--AgentGen BEGIN=mpaasDataStreamEntry::setMpaasDataStreamStatusAct
            //--AgentGen END
            super.setValue(idxMpaasDataStreamStatusAct, newColValue);
        }

        public OctetString getMpaasDataStreamStatusDetails() {
            //--AgentGen BEGIN=mpaasDataStreamEntry::getMpaasDataStreamStatusDetails
            //--AgentGen END
            return (OctetString) super.getValue(idxMpaasDataStreamStatusDetails);
        }

        public void setMpaasDataStreamStatusDetails(OctetString newColValue) {
            //--AgentGen BEGIN=mpaasDataStreamEntry::setMpaasDataStreamStatusDetails
            //--AgentGen END
            super.setValue(idxMpaasDataStreamStatusDetails, newColValue);
        }

        public Integer32 getMpaasDataStreamIsReachable() {
            //--AgentGen BEGIN=mpaasDataStreamEntry::getMpaasDataStreamIsReachable
            //--AgentGen END
            return (Integer32) super.getValue(idxMpaasDataStreamIsReachable);
        }

        public void setMpaasDataStreamIsReachable(Integer32 newColValue) {
            //--AgentGen BEGIN=mpaasDataStreamEntry::setMpaasDataStreamIsReachable
            //--AgentGen END
            super.setValue(idxMpaasDataStreamIsReachable, newColValue);
        }

        public OctetString getMpaasDataStreamProducerSystem() {
            //--AgentGen BEGIN=mpaasDataStreamEntry::getMpaasDataStreamProducerSystem
            //--AgentGen END
            return (OctetString) super.getValue(idxMpaasDataStreamProducerSystem);
        }

        public void setMpaasDataStreamProducerSystem(OctetString newColValue) {
            //--AgentGen BEGIN=mpaasDataStreamEntry::setMpaasDataStreamProducerSystem
            //--AgentGen END
            super.setValue(idxMpaasDataStreamProducerSystem, newColValue);
        }

        public OctetString getMpaasDataStreamConsumerSystem() {
            //--AgentGen BEGIN=mpaasDataStreamEntry::getMpaasDataStreamConsumerSystem
            //--AgentGen END
            return (OctetString) super.getValue(idxMpaasDataStreamConsumerSystem);
        }

        public void setMpaasDataStreamConsumerSystem(OctetString newColValue) {
            //--AgentGen BEGIN=mpaasDataStreamEntry::setMpaasDataStreamConsumerSystem
            //--AgentGen END
            super.setValue(idxMpaasDataStreamConsumerSystem, newColValue);
        }

        public Integer32 getMpaasDataStreamCriticality() {
            //--AgentGen BEGIN=mpaasDataStreamEntry::getMpaasDataStreamCriticality
            //--AgentGen END
            return (Integer32) super.getValue(idxMpaasDataStreamCriticality);
        }

        public void setMpaasDataStreamCriticality(Integer32 newColValue) {
            //--AgentGen BEGIN=mpaasDataStreamEntry::setMpaasDataStreamCriticality
            //--AgentGen END
            super.setValue(idxMpaasDataStreamCriticality, newColValue);
        }

        public Variable getValue(int column) {
            //--AgentGen BEGIN=mpaasDataStreamEntry::RowGetValue
            //--AgentGen END
            switch (column) {
                case idxMpaasDataStreamStatusInt:
                    return getMpaasDataStreamStatusInt();
                case idxMpaasDataStreamStatusAct:
                    return getMpaasDataStreamStatusAct();
                case idxMpaasDataStreamStatusDetails:
                    return getMpaasDataStreamStatusDetails();
                case idxMpaasDataStreamIsReachable:
                    return getMpaasDataStreamIsReachable();
                case idxMpaasDataStreamProducerSystem:
                    return getMpaasDataStreamProducerSystem();
                case idxMpaasDataStreamConsumerSystem:
                    return getMpaasDataStreamConsumerSystem();
                case idxMpaasDataStreamCriticality:
                    return getMpaasDataStreamCriticality();
                default:
                    return super.getValue(column);
            }
        }

        public void setValue(int column, Variable value) {
            //--AgentGen BEGIN=mpaasDataStreamEntry::RowSetValue
            //--AgentGen END
            switch (column) {
                case idxMpaasDataStreamStatusInt:
                    setMpaasDataStreamStatusInt((Integer32) value);
                    break;
                case idxMpaasDataStreamStatusAct:
                    setMpaasDataStreamStatusAct((Integer32) value);
                    break;
                case idxMpaasDataStreamStatusDetails:
                    setMpaasDataStreamStatusDetails((OctetString) value);
                    break;
                case idxMpaasDataStreamIsReachable:
                    setMpaasDataStreamIsReachable((Integer32) value);
                    break;
                case idxMpaasDataStreamProducerSystem:
                    setMpaasDataStreamProducerSystem((OctetString) value);
                    break;
                case idxMpaasDataStreamConsumerSystem:
                    setMpaasDataStreamConsumerSystem((OctetString) value);
                    break;
                case idxMpaasDataStreamCriticality:
                    setMpaasDataStreamCriticality((Integer32) value);
                    break;
                default:
                    super.setValue(column, value);
            }
        }

        //--AgentGen BEGIN=mpaasDataStreamEntry::Row
        //--AgentGen END
    }

    class MpaasDataStreamEntryRowFactory
            implements MOTableRowFactory<MpaasDataStreamEntryRow> {
        public synchronized MpaasDataStreamEntryRow createRow(OID index, Variable[] values)
                throws UnsupportedOperationException {
            MpaasDataStreamEntryRow row =
                    new MpaasDataStreamEntryRow(index, values);
            //--AgentGen BEGIN=mpaasDataStreamEntry::createRow
            //--AgentGen END
            return row;
        }

        public synchronized void freeRow(MpaasDataStreamEntryRow row) {
            //--AgentGen BEGIN=mpaasDataStreamEntry::freeRow
            //--AgentGen END
        }

        //--AgentGen BEGIN=mpaasDataStreamEntry::RowFactory
        //--AgentGen END
    }

    public class MpaasStatusDetailsText implements TextualConvention {

        public MpaasStatusDetailsText() {
        }

        public String getModuleName() {
            return TC_MODULE_MUAC_PAAS_MIB;
        }

        public String getName() {
            return TC_MPAASSTATUSDETAILSTEXT;
        }

        public Variable createInitialValue() {
            Variable v = new OctetString();
            if (v instanceof AssignableFromLong) {
                ((AssignableFromLong) v).setValue(0L);
            }
            // further modify value to comply with TC constraints here:
            //--AgentGen BEGIN=MpaasStatusDetailsText::createInitialValue
            //--AgentGen END
            return v;
        }

        public MOScalar createScalar(OID oid, MOAccess access, Variable value) {
            MOScalar scalar = moFactory.createScalar(oid, access, value);
            ValueConstraint vc = new ConstraintsImpl();
            ((ConstraintsImpl) vc).add(new Constraint(0L, 160L));
            scalar.addMOValueValidationListener(new ValueConstraintValidator(vc));
            //--AgentGen BEGIN=MpaasStatusDetailsText::createScalar
            //--AgentGen END
            return scalar;
        }

        public MOColumn createColumn(int columnID, int syntax, MOAccess access,
                                     Variable defaultValue, boolean mutableInService) {
            MOColumn col = moFactory.createColumn(columnID, syntax, access,
                    defaultValue, mutableInService);
            if (col instanceof MOMutableColumn) {
                MOMutableColumn mcol = (MOMutableColumn) col;
                ValueConstraint vc = new ConstraintsImpl();
                ((ConstraintsImpl) vc).add(new Constraint(0L, 160L));
                mcol.addMOValueValidationListener(new ValueConstraintValidator(vc));
            }
            //--AgentGen BEGIN=MpaasStatusDetailsText::createColumn
            //--AgentGen END
            return col;
        }
    }

    public class MpaasGenericNameType implements TextualConvention {

        public MpaasGenericNameType() {
        }

        public String getModuleName() {
            return TC_MODULE_MUAC_PAAS_MIB;
        }

        public String getName() {
            return TC_MPAASGENERICNAMETYPE;
        }

        public Variable createInitialValue() {
            Variable v = new OctetString();
            if (v instanceof AssignableFromLong) {
                ((AssignableFromLong) v).setValue(1L);
            }
            // further modify value to comply with TC constraints here:
            //--AgentGen BEGIN=MpaasGenericNameType::createInitialValue
            //--AgentGen END
            return v;
        }

        public MOScalar createScalar(OID oid, MOAccess access, Variable value) {
            MOScalar scalar = moFactory.createScalar(oid, access, value);
            ValueConstraint vc = new ConstraintsImpl();
            ((ConstraintsImpl) vc).add(new Constraint(1L, 24L));
            scalar.addMOValueValidationListener(new ValueConstraintValidator(vc));
            //--AgentGen BEGIN=MpaasGenericNameType::createScalar
            //--AgentGen END
            return scalar;
        }

        public MOColumn createColumn(int columnID, int syntax, MOAccess access,
                                     Variable defaultValue, boolean mutableInService) {
            MOColumn col = moFactory.createColumn(columnID, syntax, access,
                    defaultValue, mutableInService);
            if (col instanceof MOMutableColumn) {
                MOMutableColumn mcol = (MOMutableColumn) col;
                ValueConstraint vc = new ConstraintsImpl();
                ((ConstraintsImpl) vc).add(new Constraint(1L, 24L));
                mcol.addMOValueValidationListener(new ValueConstraintValidator(vc));
            }
            //--AgentGen BEGIN=MpaasGenericNameType::createColumn
            //--AgentGen END
            return col;
        }
    }

    public class MpaasStatusIntendedType implements TextualConvention {
        public static final int operational = 1;
        public static final int nonOperational = 2;

        public MpaasStatusIntendedType() {
        }

        public String getModuleName() {
            return TC_MODULE_MUAC_PAAS_MIB;
        }

        public String getName() {
            return TC_MPAASSTATUSINTENDEDTYPE;
        }

        public Variable createInitialValue() {
            Variable v = new Integer32();
            if (v instanceof AssignableFromLong) {
                ((AssignableFromLong) v).setValue(1);
            }
            // further modify value to comply with TC constraints here:
            //--AgentGen BEGIN=MpaasStatusIntendedType::createInitialValue
            //--AgentGen END
            return v;
        }

        public MOScalar createScalar(OID oid, MOAccess access, Variable value) {
            MOScalar scalar = moFactory.createScalar(oid, access, value);
            ValueConstraint vc = new EnumerationConstraint(
                    new int[]{operational,
                            nonOperational});
            scalar.addMOValueValidationListener(new ValueConstraintValidator(vc));
            //--AgentGen BEGIN=MpaasStatusIntendedType::createScalar
            //--AgentGen END
            return scalar;
        }

        public MOColumn createColumn(int columnID, int syntax, MOAccess access,
                                     Variable defaultValue, boolean mutableInService) {
            MOColumn col = moFactory.createColumn(columnID, syntax, access,
                    defaultValue, mutableInService);
            if (col instanceof MOMutableColumn) {
                MOMutableColumn mcol = (MOMutableColumn) col;
                ValueConstraint vc = new EnumerationConstraint(
                        new int[]{operational,
                                nonOperational});
                mcol.addMOValueValidationListener(new ValueConstraintValidator(vc));
            }
            //--AgentGen BEGIN=MpaasStatusIntendedType::createColumn
            //--AgentGen END
            return col;
        }
    }

    public class MpaasStatusActualType implements TextualConvention {
        public static final int operational = 1;
        public static final int warning = 2;
        public static final int minor = 3;
        public static final int major = 4;
        public static final int failed = 5;
        public static final int nonOperational = 6;

        public MpaasStatusActualType() {
        }

        public String getModuleName() {
            return TC_MODULE_MUAC_PAAS_MIB;
        }

        public String getName() {
            return TC_MPAASSTATUSACTUALTYPE;
        }

        public Variable createInitialValue() {
            Variable v = new Integer32();
            if (v instanceof AssignableFromLong) {
                ((AssignableFromLong) v).setValue(1);
            }
            // further modify value to comply with TC constraints here:
            //--AgentGen BEGIN=MpaasStatusActualType::createInitialValue
            //--AgentGen END
            return v;
        }

        public MOScalar createScalar(OID oid, MOAccess access, Variable value) {
            MOScalar scalar = moFactory.createScalar(oid, access, value);
            ValueConstraint vc = new EnumerationConstraint(
                    new int[]{operational,
                            warning,
                            minor,
                            major,
                            failed,
                            nonOperational});
            scalar.addMOValueValidationListener(new ValueConstraintValidator(vc));
            //--AgentGen BEGIN=MpaasStatusActualType::createScalar
            //--AgentGen END
            return scalar;
        }

        public MOColumn createColumn(int columnID, int syntax, MOAccess access,
                                     Variable defaultValue, boolean mutableInService) {
            MOColumn col = moFactory.createColumn(columnID, syntax, access,
                    defaultValue, mutableInService);
            if (col instanceof MOMutableColumn) {
                MOMutableColumn mcol = (MOMutableColumn) col;
                ValueConstraint vc = new EnumerationConstraint(
                        new int[]{operational,
                                warning,
                                minor,
                                major,
                                failed,
                                nonOperational});
                mcol.addMOValueValidationListener(new ValueConstraintValidator(vc));
            }
            //--AgentGen BEGIN=MpaasStatusActualType::createColumn
            //--AgentGen END
            return col;
        }
    }


//--AgentGen BEGIN=_TC_CLASSES_IMPORTED_MODULES_BEGIN
//--AgentGen END

    public class MpaasCriticalityType implements TextualConvention {
        public static final int criticalityNonCritical = 1;
        public static final int criticalityLow = 2;
        public static final int criticalityHigh = 3;
        public static final int criticalityCritical = 4;

        public MpaasCriticalityType() {
        }

        public String getModuleName() {
            return TC_MODULE_MUAC_PAAS_MIB;
        }

        public String getName() {
            return TC_MPAASCRITICALITYTYPE;
        }

        public Variable createInitialValue() {
            Variable v = new Integer32();
            if (v instanceof AssignableFromLong) {
                ((AssignableFromLong) v).setValue(1);
            }
            // further modify value to comply with TC constraints here:
            //--AgentGen BEGIN=MpaasCriticalityType::createInitialValue
            //--AgentGen END
            return v;
        }

        public MOScalar createScalar(OID oid, MOAccess access, Variable value) {
            MOScalar scalar = moFactory.createScalar(oid, access, value);
            ValueConstraint vc = new EnumerationConstraint(
                    new int[]{criticalityNonCritical,
                            criticalityLow,
                            criticalityHigh,
                            criticalityCritical});
            scalar.addMOValueValidationListener(new ValueConstraintValidator(vc));
            //--AgentGen BEGIN=MpaasCriticalityType::createScalar
            //--AgentGen END
            return scalar;
        }

        public MOColumn createColumn(int columnID, int syntax, MOAccess access,
                                     Variable defaultValue, boolean mutableInService) {
            MOColumn col = moFactory.createColumn(columnID, syntax, access,
                    defaultValue, mutableInService);
            if (col instanceof MOMutableColumn) {
                MOMutableColumn mcol = (MOMutableColumn) col;
                ValueConstraint vc = new EnumerationConstraint(
                        new int[]{criticalityNonCritical,
                                criticalityLow,
                                criticalityHigh,
                                criticalityCritical});
                mcol.addMOValueValidationListener(new ValueConstraintValidator(vc));
            }
            //--AgentGen BEGIN=MpaasCriticalityType::createColumn
            //--AgentGen END
            return col;
        }
    }


//--AgentGen BEGIN=_TC_CLASSES_IMPORTED_MODULES_END
//--AgentGen END

//--AgentGen BEGIN=_CLASSES
//--AgentGen END

//--AgentGen BEGIN=_END
//--AgentGen END
}


