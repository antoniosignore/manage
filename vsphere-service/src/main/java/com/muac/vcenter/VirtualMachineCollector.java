package com.muac.vcenter;

import com.muac.beans.MMCAlarm;
import com.muac.beans.MMCVm;
import com.vmware.vim25.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPFaultException;

@Component
public class VirtualMachineCollector {

    ServiceContent serviceContent;
    VimPortType vimPort;

    @Autowired
    VSphereConfiguration configuration;

    public List<MMCVm> machineDTOs = new ArrayList<>();

    public VirtualMachineCollector() {
    }

    private void collectProperties() throws Exception {

        ManagedObjectReference viewMgrRef = serviceContent.getViewManager();
        ManagedObjectReference propColl = serviceContent.getPropertyCollector();

        List<String> vmList = new ArrayList<String>();
        vmList.add("VirtualMachine");

        ManagedObjectReference cViewRef =
                vimPort.createContainerView(viewMgrRef, serviceContent.getRootFolder(), vmList, true);

        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(cViewRef);
        oSpec.setSkip(true);

        // create a traversal spec to select all objects in the view
        TraversalSpec tSpec = new TraversalSpec();
        tSpec.setName("traverseEntities");
        tSpec.setPath("view");
        tSpec.setSkip(false);
        tSpec.setType("ContainerView");

        oSpec.getSelectSet().add(tSpec);

        PropertySpec pSpec = new PropertySpec();
        pSpec.setType("VirtualMachine");
        pSpec.getPathSet().add("name");

        PropertyFilterSpec fSpec = new PropertyFilterSpec();
        fSpec.getObjectSet().add(oSpec);
        fSpec.getPropSet().add(pSpec);

        // Create a list for the filters and add the spec to it
        List<PropertyFilterSpec> fSpecList = new ArrayList<>();
        fSpecList.add(fSpec);

        // get the data from the server
        RetrieveOptions ro = new RetrieveOptions();
        RetrieveResult props = vimPort.retrievePropertiesEx(propColl, fSpecList, ro);

        // go through the returned list and print out the data
        if (props != null) {
            for (ObjectContent oc : props.getObjects()) {
                String value = null;
                String path = null;
                List<DynamicProperty> dps = oc.getPropSet();
                if (dps != null) {
                    for (DynamicProperty dp : dps) {
                        path = dp.getName();
                        if (path.equals("name")) {
                            value = (String) dp.getVal();

                            System.out.println("\n\n####################################################");
                            System.out.println("Val: " + value);

                            ManagedObjectReference vmByVMname =
                                    getVmByVMname(value, serviceContent, vimPort);

                            MMCVm mmcVm = new MMCVm();
                            mmcVm.setName(value);

                            List<MMCAlarm> listAlarm = getAlarmsFromManagedObjectReference(serviceContent, vimPort, vmByVMname);
                            for (MMCAlarm alarm : listAlarm) {
                                // acknowledge each alarm
                                // vimPort.acknowledgeAlarm(alarmManager, alarm.getObj(), datacenterRef);
                                mmcVm.getMmcAlarms().add(alarm);
                            }
                            machineDTOs.add(mmcVm);
                        }
                    }
                }
            }
        }
    }


    private static class TrustAllTrustManager implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] certs,
                                       String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] certs,
                                       String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }

    public void process()  {


        try {
            String url = "https://" + configuration.host + "/sdk/vimService";

            ManagedObjectReference SVC_INST_REF = new ManagedObjectReference();
            VimService vimService;

            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            };

            javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
            javax.net.ssl.TrustManager tm = new TrustAllTrustManager();
            trustAllCerts[0] = tm;

            javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");

            javax.net.ssl.SSLSessionContext sslsc = sc.getServerSessionContext();

            sslsc.setSessionTimeout(0);
            sc.init(null, trustAllCerts, null);

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HttpsURLConnection.setDefaultHostnameVerifier(hv);

            SVC_INST_REF.setType("ServiceInstance");
            SVC_INST_REF.setValue("ServiceInstance");

            vimService = new VimService();
            vimPort = vimService.getVimPort();
            Map<String, Object> ctxt = ((BindingProvider) vimPort).getRequestContext();

            ctxt.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
            ctxt.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);

            // Retrieve the ServiceContent object and login
            serviceContent = vimPort.retrieveServiceContent(SVC_INST_REF);
            vimPort.login(serviceContent.getSessionManager(),configuration.user,configuration.password,null);

            // print out the product name, server type, and product version
            System.out.println(serviceContent.getAbout().getFullName());
            System.out.println("Server type is " +
                    serviceContent.getAbout().getApiType());
            System.out.println("API version is " +
                    serviceContent.getAbout().getVersion());

            collectProperties();

            collectProperties2();


        }catch(Exception ex){

            ex.printStackTrace();
        } finally {
            // close the connection
            try {
                vimPort.logout(serviceContent.getSessionManager());
            } catch (RuntimeFaultFaultMsg runtimeFaultFaultMsg) {
                runtimeFaultFaultMsg.printStackTrace();
            }
        }
    }

    public static SelectionSpec[] createSelectionSpec(String[] names) {
        SelectionSpec[] sss = new SelectionSpec[names.length];
        for (int i = 0; i < names.length; i++) {
            sss[i] = new SelectionSpec();
            sss[i].setName(names[i]);
        }
        return sss;
    }
    public static TraversalSpec createTraversalSpec(String name, String type, String path, String[] selectPath) {
        return createTraversalSpec(name, type, path, createSelectionSpec(selectPath));
    }

    public static TraversalSpec createTraversalSpec(String name, String type, String path, SelectionSpec[] selectSet) {
        TraversalSpec ts = new TraversalSpec();
        ts.setName(name);
        ts.setType(type);
        ts.setPath(path);
        ts.setSkip(Boolean.FALSE);
        for (int i = 0; i < selectSet.length; i++) {
            SelectionSpec selectionSpec = selectSet[i];
            ts.getSelectSet().add(selectionSpec);
        }
        return ts;
    }

    private static List<TraversalSpec> buildFullTraversalV2NoFolder() {
        // Recurse through all ResourcePools
        TraversalSpec rpToRp = createTraversalSpec("rpToRp",
                "ResourcePool", "resourcePool",
                new String[]{"rpToRp", "rpToVm"});

        // Recurse through all ResourcePools
        TraversalSpec rpToVm = createTraversalSpec("rpToVm",
                "ResourcePool", "vm",
                new SelectionSpec[]{});

        // Traversal through ResourcePool branch
        TraversalSpec crToRp = createTraversalSpec("crToRp",
                "ComputeResource", "resourcePool",
                new String[]{"rpToRp", "rpToVm"});

        // Traversal through host branch
        TraversalSpec crToH = createTraversalSpec("crToH",
                "ComputeResource", "host",
                new SelectionSpec[]{});

        // Traversal through hostFolder branch
        TraversalSpec dcToHf = createTraversalSpec("dcToHf",
                "Datacenter", "hostFolder",
                new String[]{"visitFolders"});

        // Traversal through vmFolder branch
        TraversalSpec dcToVmf = createTraversalSpec("dcToVmf",
                "Datacenter", "vmFolder",
                new String[]{"visitFolders"});

        TraversalSpec HToVm = createTraversalSpec("HToVm",
                "HostSystem", "vm",
                new String[]{"visitFolders"});

        return Arrays.asList(dcToVmf, dcToHf, crToH, crToRp, rpToRp, HToVm, rpToVm);
    }



    public static SelectionSpec[] buildFullTraversalV4() {
        List<TraversalSpec> tSpecs = buildFullTraversalV2NoFolder();

        TraversalSpec dcToDs = createTraversalSpec("dcToDs",
                "Datacenter", "datastoreFolder",
                new String[]{"visitFolders"});

        TraversalSpec vAppToRp = createTraversalSpec("vAppToRp",
                "VirtualApp", "resourcePool",
                new String[]{"rpToRp", "vAppToRp"});

        /**
         * Copyright 2009 Altor Networks, contribution by Elsa Bignoli
         * @author Elsa Bignoli (elsa@altornetworks.com)
         */
        // Traversal through netFolder branch
        TraversalSpec dcToNetf = createTraversalSpec("dcToNetf",
                "Datacenter", "networkFolder",
                new String[]{"visitFolders"});

        // Recurse through the folders
        TraversalSpec visitFolders = createTraversalSpec("visitFolders",
                "Folder", "childEntity",
                new String[]{"visitFolders", "dcToHf", "dcToVmf", "dcToDs", "dcToNetf", "crToH", "crToRp", "HToVm", "rpToVm"});

        SelectionSpec[] sSpecs = new SelectionSpec[tSpecs.size() + 4];
        sSpecs[0] = visitFolders;
        sSpecs[1] = dcToDs;
        sSpecs[2] = dcToNetf;
        sSpecs[3] = vAppToRp;
        for (int i = 4; i < sSpecs.length; i++) {
            sSpecs[i] = tSpecs.get(i - 4);
        }

        return sSpecs;
    }


    private  void collectProperties2() throws Exception {

// Get reference to the PropertyCollector
        ManagedObjectReference propColl = serviceContent.getPropertyCollector();

// get the top-level vm folder mor
        ManagedObjectReference rootVmFolder = serviceContent.getRootFolder();

// create an object spec to define the beginning of the traversal;
// root vm folder is the root object for this traversal
        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(rootVmFolder);
        oSpec.setSkip(false);

// folder traversal reference
        SelectionSpec sSpecF = new SelectionSpec();
        sSpecF.setName("traverseFolder");

// create a folder traversal spec to select childEntity
        TraversalSpec tSpecF = new TraversalSpec();
        tSpecF.setType("Folder");
        tSpecF.setPath("childEntity");
        tSpecF.setSkip(false);
        tSpecF.setName("traverseFolder");

// use the SelectionSpec as a reflexive spec for the folder traversal;
// the accessor method (getSelectSet) returns a reference to the
// mapped XML representation of the list; using this reference
// to add the spec will update the list

        SelectionSpec[] selectionSpecs = buildFullTraversalV4();
        for (int i = 0; i < selectionSpecs.length; i++) {
            SelectionSpec selectionSpec = selectionSpecs[i];
            tSpecF.getSelectSet().add(selectionSpec);
        }

// add folder traversal to object spec
        oSpec.getSelectSet().add(tSpecF);

// specify the property for retrieval (folder name)
        PropertySpec pSpec = new PropertySpec();
        pSpec.setType("Folder");
        pSpec.getPathSet().add("name");

// create a PropertyFilterSpec and add the object and
// property specs to it; use the getter method to reference
// the mapped XML representation of the lists and add the specs
// directly to the lists
        PropertyFilterSpec fSpec = new PropertyFilterSpec();
        fSpec.getObjectSet().add(oSpec);
        fSpec.getPropSet().add(pSpec);

// Create a list for the filter and add the spec to it
        List<PropertyFilterSpec> fSpecList = new ArrayList<PropertyFilterSpec>();
        fSpecList.add(fSpec);

        // get the data from the server
        RetrieveOptions ro = new RetrieveOptions();
        RetrieveResult props = vimPort.retrievePropertiesEx(propColl,fSpecList,ro);

        // go through the returned list and print out the data
        if (props != null) {
            for (ObjectContent oc : props.getObjects()) {
                String folderName = null;
                String path = null;
                List<DynamicProperty> dps = oc.getPropSet();
                if (dps != null) {
                    for (DynamicProperty dp : dps) {
                        folderName = (String) dp.getVal();
                        path = dp.getName();
                        System.out.println(path + " = " + folderName);
                    }
                }
            }
        }
    }

    public ObjectContent findObject(ManagedObjectReference mor, String... properties) {
        List<ObjectContent> oCont = null;

        // Create PropertyFilterSpec using the PropertySpec and ObjectPec
        PropertySpec pSpec = new PropertySpec();
        pSpec.setAll(Boolean.FALSE);
        pSpec.setType(mor.getType());
        for (String property : properties) {
            pSpec.getPathSet().add(property);
        }

        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(mor);

        PropertyFilterSpec fSpec = new PropertyFilterSpec();
        fSpec.getObjectSet().add(oSpec);
        fSpec.getPropSet().add(pSpec);
        try {
            oCont = vimPort.retrievePropertiesEx(serviceContent.getPropertyCollector(),
                    Arrays.asList(fSpec), new RetrieveOptions()).getObjects();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return oCont.get(0);
    }

    /**
     *  list of object content
     */
    List<ObjectContent> retrievePropertiesAllObjects(List<PropertyFilterSpec> listpfs) {

        RetrieveOptions propObjectRetrieveOpts = new RetrieveOptions();

        List<ObjectContent> listobjcontent = new ArrayList<ObjectContent>();

        try {
            RetrieveResult rslts =
                    vimPort.retrievePropertiesEx(serviceContent.getPropertyCollector(), listpfs,
                            propObjectRetrieveOpts);
            if (rslts != null && rslts.getObjects() != null && !rslts.getObjects().isEmpty()) {
                listobjcontent.addAll(rslts.getObjects());
            }

            String token = null;
            if (rslts != null && rslts.getToken() != null) {
                token = rslts.getToken();
            }
            while (token != null && !token.isEmpty()) {
                rslts =
                        vimPort.continueRetrievePropertiesEx(serviceContent.getPropertyCollector(), token);
                token = null;
                if (rslts != null) {
                    token = rslts.getToken();
                    if (rslts.getObjects() != null && !rslts.getObjects().isEmpty()) {
                        listobjcontent.addAll(rslts.getObjects());
                    }
                }
            }
        } catch (SOAPFaultException sfe) {
            printSoapFaultException(sfe);
        } catch (Exception e) {
            System.out.println(" : Failed Getting Contents");
            e.printStackTrace();
        }
        return listobjcontent;
    }

    TraversalSpec getVMTraversalSpec(ServiceContent serviceContent) {
        // Create a traversal spec that starts from the 'root' objects
        // and traverses the inventory tree to get to the VirtualMachines.
        // Build the traversal specs bottoms up

        // Traversal to get to the VM in a VApp
        TraversalSpec vAppToVM = new TraversalSpec();
        vAppToVM.setName("vAppToVM");
        vAppToVM.setType("VirtualApp");
        vAppToVM.setPath("vm");

        // Traversal spec for VApp to VApp
        TraversalSpec vAppToVApp = new TraversalSpec();
        vAppToVApp.setName("vAppToVApp");
        vAppToVApp.setType("VirtualApp");
        vAppToVApp.setPath("resourcePool");
        // SelectionSpec for VApp to VApp recursion
        SelectionSpec vAppRecursion = new SelectionSpec();
        vAppRecursion.setName("vAppToVApp");
        // SelectionSpec to get to a VM in the VApp
        SelectionSpec vmInVApp = new SelectionSpec();
        vmInVApp.setName("vAppToVM");
        // SelectionSpec for both VApp to VApp and VApp to VM
        List<SelectionSpec> vAppToVMSS = new ArrayList<SelectionSpec>();
        vAppToVMSS.add(vAppRecursion);
        vAppToVMSS.add(vmInVApp);
        vAppToVApp.getSelectSet().addAll(vAppToVMSS);

        // This SelectionSpec is used for recursion for Folder recursion
        SelectionSpec sSpec = new SelectionSpec();
        sSpec.setName("VisitFolders");

        // Traversal to get to the vmFolder from DataCenter
        TraversalSpec dataCenterToVMFolder = new TraversalSpec();
        dataCenterToVMFolder.setName("DataCenterToVMFolder");
        dataCenterToVMFolder.setType("Datacenter");
        dataCenterToVMFolder.setPath("vmFolder");
        dataCenterToVMFolder.setSkip(false);
        dataCenterToVMFolder.getSelectSet().add(sSpec);

        // TraversalSpec to get to the DataCenter from rootFolder
        TraversalSpec traversalSpec = new TraversalSpec();
        traversalSpec.setName("VisitFolders");
        traversalSpec.setType("Folder");
        traversalSpec.setPath("childEntity");
        traversalSpec.setSkip(false);
        List<SelectionSpec> sSpecArr = new ArrayList<SelectionSpec>();
        sSpecArr.add(sSpec);
        sSpecArr.add(dataCenterToVMFolder);
        sSpecArr.add(vAppToVM);
        sSpecArr.add(vAppToVApp);
        traversalSpec.getSelectSet().addAll(sSpecArr);
        return traversalSpec;
    }

    private ManagedObjectReference getVmByVMname(String vmName, ServiceContent serviceContent, VimPortType vimPort) {
        ManagedObjectReference retVal = null;
        ManagedObjectReference rootFolder = serviceContent.getRootFolder();
        try {
            TraversalSpec tSpec = getVMTraversalSpec(serviceContent);
            // Create Property Spec
            PropertySpec propertySpec = new PropertySpec();
            propertySpec.setAll(Boolean.FALSE);
            propertySpec.getPathSet().add("name");
            propertySpec.setType("VirtualMachine");

            // Now create Object Spec
            ObjectSpec objectSpec = new ObjectSpec();
            objectSpec.setObj(rootFolder);
            objectSpec.setSkip(Boolean.TRUE);
            objectSpec.getSelectSet().add(tSpec);

            // Create PropertyFilterSpec using the PropertySpec and ObjectPec
            // created above.
            PropertyFilterSpec propertyFilterSpec = new PropertyFilterSpec();
            propertyFilterSpec.getPropSet().add(propertySpec);
            propertyFilterSpec.getObjectSet().add(objectSpec);

            List<PropertyFilterSpec> listpfs =
                    new ArrayList<PropertyFilterSpec>(1);
            listpfs.add(propertyFilterSpec);
            List<ObjectContent> listobjcont =
                    retrievePropertiesAllObjects(listpfs);

            if (listobjcont != null) {
                for (ObjectContent oc : listobjcont) {
                    ManagedObjectReference mr = oc.getObj();
                    String vmnm = null;
                    List<DynamicProperty> dps = oc.getPropSet();
                    if (dps != null) {
                        for (DynamicProperty dp : dps) {
                            vmnm = (String) dp.getVal();
                        }
                    }
                    if (vmnm != null && vmnm.equals(vmName)) {
                        retVal = mr;
                        break;
                    }
                }
            }
        } catch (SOAPFaultException sfe) {
            printSoapFaultException(sfe);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    void printSoapFaultException(SOAPFaultException sfe) {
        System.out.println("SOAP Fault -");
        if (sfe.getFault().hasDetail()) {
            System.out.println(sfe.getFault().getDetail().getFirstChild()
                    .getLocalName());
        }
        if (sfe.getFault().getFaultString() != null) {
            System.out.println("\n Message: " + sfe.getFault().getFaultString());
        }
    }

    private List<MMCAlarm> getAlarmsFromManagedObjectReference(
            ServiceContent serviceContent,
            VimPortType vimPort,
            com.vmware.vim25.ManagedObjectReference moref)
            throws Exception {

        List<MMCAlarm> alarmList = new ArrayList<MMCAlarm>();
        ManagedObjectReference alarmManager = serviceContent.getAlarmManager();

        // retrieve list of alarm state associated with the datacenter
        List<com.vmware.vim25.AlarmState> alarmStateList = vimPort.getAlarmState(alarmManager, moref);

        System.out.printf("List alarm(s)%n \n", alarmStateList.size());

        for (AlarmState alarmState : alarmStateList) {
            com.vmware.vim25.ManagedEntityStatus status = alarmState.getOverallStatus();

            MMCAlarm al = new MMCAlarm();
            al.setOverallState(status.value());

            // check if the alarm states need some attention
                ObjectContent alarm = findObject(alarmState.getAlarm(), "info");
                com.vmware.vim25.AlarmInfo alarmInfo = (AlarmInfo) alarm.getPropSet().get(0).getVal();
                al.setKey(alarmInfo.getName());
           //     al.setDate(alarmState.getTime());

                alarmList.add(al);
                System.out.printf("Alarm: %s - Status: %s - Created: %s%n", alarmInfo.getName(),
                        status, alarmState.getTime());
        }
        return alarmList;
    }

}