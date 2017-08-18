package com.vmware.vim25.mo.samples;

import com.muac.vcenter.Constants;
import com.vmware.vim25.mo.ServiceInstance;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

public class SampleUtil {

    public static ServiceInstance createServiceInstance() throws RemoteException, MalformedURLException {
        ServiceInstance si = new ServiceInstance(new URL(Constants.URL), Constants.USER_NAME, Constants.PASSWORD, true);
        return si;
    }

}
