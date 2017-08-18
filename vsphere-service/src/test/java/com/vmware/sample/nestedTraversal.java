package com.vmware.sample;
import com.muac.vcenter.Constants;
import com.vmware.vim25.*;

import java.util.*;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPFaultException;

// PropertyCollector example
// command line input: server name, user name, password

public class nestedTraversal {
    static String  datacenterName = "DC-MLOG";

    private static void collectProperties(VimPortType methods,
                                          ServiceContent sContent) throws Exception {

// Get reference to the PropertyCollector
        ManagedObjectReference propColl = sContent.getPropertyCollector();

// get the top-level vm folder mor
        ManagedObjectReference sIndex = sContent.getSearchIndex();
        ManagedObjectReference rootVmFolder =
                methods.findByInventoryPath(sIndex,datacenterName);

// create an object spec to define the beginning of the traversal;
// root vm folder is the root object for this traversal
        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(rootVmFolder);
        oSpec.setSkip(true);

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
        tSpecF.getSelectSet().add(sSpecF);

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
        RetrieveResult props = methods.retrievePropertiesEx(propColl,fSpecList,ro);

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
    }//end collectProperties()

    // Authentication is handled by using a TrustManager and supplying
// a host name verifier method. (The host name verifier is declared
// in the main function.)
//
// For the purposes of this example, this TrustManager implementation
// will accept all certificates. This is only appropriate for
// a development environment. Production code should implement certificate support.
    private static class TrustAllTrustManager implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }

    public static void main(String [] args) throws Exception {

// arglist variables
        // handle input info
        String serverName = "172.22.160.1";
        String userName = Constants.USER_NAME;
        String password = Constants.PASSWORD;
        String url = "https://"+serverName+"/sdk/vimService";

// Variables of the following types for access to the API methods
// and to the vSphere inventory.
// -- ManagedObjectReference for the ServiceInstance on the Server
// -- VimService for access to the vSphere Web service
// -- VimPortType for access to methods
// -- ServiceContent for access to managed object services
        ManagedObjectReference SVC_INST_REF = new ManagedObjectReference();
        VimService vimService;
        VimPortType vimPort;
        ServiceContent serviceContent;

// Declare a host name verifier that will automatically enable
// the connection. The host name verifier is invoked during
// the SSL handshake.
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };

// Create the trust manager.
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new TrustAllTrustManager();
        trustAllCerts[0] = tm;
// Create the SSL context
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
// Create the session context
        javax.net.ssl.SSLSessionContext sslsc = sc.getServerSessionContext();
// Initialize the contexts; the session context takes the trust manager.
        sslsc.setSessionTimeout(0);
        sc.init(null, trustAllCerts, null);

// Use the default socket factory to create the socket for the secure connection
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

// Set the default host name verifier to enable the connection.
        HttpsURLConnection.setDefaultHostnameVerifier(hv);

// Set up the manufactured managed object reference for the ServiceInstance
        SVC_INST_REF.setType("ServiceInstance");
        SVC_INST_REF.setValue("ServiceInstance");

// Create a VimService object to obtain a VimPort binding provider.
// The BindingProvider provides access to the protocol fields
// in request/response messages. Retrieve the request context
// which will be used for processing message requests.
        vimService = new VimService();
        vimPort = vimService.getVimPort();
        Map<String, Object> ctxt = ((BindingProvider) vimPort).getRequestContext();

// Store the Server URL in the request context and specify true
// to maintain the connection between the client and server.
// The client API will include the Server's HTTP cookie in its
// requests to maintain the session. If you do not set this to true,
// the Server will start a new session with each request.
        ctxt.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        ctxt.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);

// Retrieve the ServiceContent object and login
        serviceContent = vimPort.retrieveServiceContent(SVC_INST_REF);
        vimPort.login(serviceContent.getSessionManager(),
                userName,
                password,
                null);

// retrieve data
        collectProperties( vimPort, serviceContent );

// close the connection
        vimPort.logout(serviceContent.getSessionManager());

    }
}