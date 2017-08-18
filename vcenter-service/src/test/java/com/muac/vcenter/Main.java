package com.muac.vcenter;


import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.mo.*;

/**
 * Copyright 2015 Michael Rice <michael@michaelrice.org>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class Main {


    public static void main(String[] args) throws Exception {

        ServiceInstance serviceInstance = SampleUtil.createServiceInstance();
        Folder rootFolder = serviceInstance.getRootFolder();

        ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("Datacenter");
        if (mes == null || mes.length == 0) {
            return;
        }
        for (ManagedEntity dcmes : mes) {

            Datacenter datacenter = (Datacenter) dcmes;
            mes = new InventoryNavigator(datacenter).searchManagedEntities("ClusterComputeResource");
            if (mes == null || mes.length == 0) {
                return;
            }

            for (ManagedEntity clrmes : mes) {
                ClusterComputeResource cluster = (ClusterComputeResource) clrmes;
                System.out.println("\n\n\ncluster.getName() = " + cluster.getName());

/*
                RetrieveOptions options = new RetrieveOptions();
                options.setMaxObjects(100);
                String[] vmProps = new String[2];
                vmProps[0] = "name";
                vmProps[1] = "runtime.host";
                PropertySpec vmSpec = new PropertySpec();
                vmSpec.setAll(false);
                vmSpec.setType("VirtualMachine");
                vmSpec.setPathSet(vmProps);

                String[] hostProps = new String[4];
                hostProps[0] = "name";
                hostProps[1] = "summary.hardware.numCpuCores";
                hostProps[2] = "summary.hardware.cpuModel";
                hostProps[3] = "summary.hardware.memorySize";
                PropertySpec hostSpec = new PropertySpec();
                hostSpec.setAll(false);
                hostSpec.setType("HostSystem");
                hostSpec.setPathSet(hostProps);

                String[] clusterProps = new String[2];
                clusterProps[0] = "name";
                clusterProps[1] = "parent";
                PropertySpec clusterSpec = new PropertySpec();
                clusterSpec.setAll(false);
                clusterSpec.setType("ClusterComputeResource");
                clusterSpec.setPathSet(clusterProps);

                ObjectSpec oSpec = new ObjectSpec();
                oSpec.setObj(cluster.getMOR());
                oSpec.setSelectSet(com.vmware.vim25.mo.util.PropertyCollectorUtil.buildFullTraversalV4());
                PropertyFilterSpec[] pfSpec = new PropertyFilterSpec[1];
                pfSpec[0] = new PropertyFilterSpec();

                ObjectSpec[] oo = new ObjectSpec[1];
                oo[0] = oSpec;

                pfSpec[0].setObjectSet(oo);
                PropertySpec[] pp = new PropertySpec[3];
                pp[0] = vmSpec;
                pp[1] = hostSpec;
                pp[2] = clusterSpec;

                pfSpec[0].setPropSet(pp);
                RetrieveResult ret = serviceInstance.getPropertyCollector().retrievePropertiesEx(pfSpec, options);

                for (ObjectContent aRet : ret.getObjects()) {
                    if(aRet.getObj().type.equalsIgnoreCase("ClusterComputeResource")) {
                        printInfo(aRet);
                    }
                    if(aRet.getObj().type.equalsIgnoreCase("HostSystem")) {
                        System.out.println("Host Info: ");
                        printInfo(aRet);
                        System.out.println("#######################");
                    }
                    if(aRet.getObj().type.equalsIgnoreCase("VirtualMachine")) {
                        System.out.println("VirtualMachine: ");
                        printInfo(aRet);
                        System.out.println("#######################################");
                    }
                }

*/
/*
                Datastore[] dss = cluster.getDatastores();
                if (dss != null) {
                    for (Datastore datastore : dss) {
                        System.out.print("Object:Datastore~Name:" + datastore.getSummary().getName().replaceAll("\\s", "") + "~");
                        System.out.print("Capacity(GB):" + datastore.getSummary().getCapacity() / 1073741824 + "~");
                        System.out.print("Free(GB):" + datastore.getSummary().getFreeSpace() / 1073741824 + "~");
                        System.out.print("Free(%):" + Math.round((float) datastore.getSummary().getFreeSpace() / datastore.getSummary().getCapacity() * 100) + "~");
                        System.out.print("Cluster:" + cluster.getName() + "~");
                        System.out.println("DC:" + datacenter.getName());
                    }
                }*/

                mes = new InventoryNavigator(cluster).searchManagedEntities("HostSystem");
                if (mes == null || mes.length == 0) {
                    return;
                }
                for (ManagedEntity hostmes : mes) {
                    HostSystem hs = (HostSystem) hostmes;
                    System.out.print("Object:Host~Name:" + hs.getName() + "~");
                    System.out.print("Model:" + hs.getHardware().getSystemInfo().getModel().replaceAll("\\s", "") + "~");
                    System.out.print("Cores:" + hs.getHardware().getCpuInfo().getNumCpuCores() + "~");
                    System.out.print("Mem(GB):" + hs.getHardware().getMemorySize() / 1073741824 + "~");
                    System.out.print("ESX:" + hs.getConfig().getProduct().getVersion() + "~");
                    System.out.print("Build:" + hs.getConfig().getProduct().getBuild() + "~");
                    System.out.print("Cluster:" + cluster.getName() + "~");
                    System.out.println("DC:" + datacenter.getName());

                    System.out.println("IS IN MAINTENANCE MODE? " + hs.getRuntime().isInMaintenanceMode());

                    hs.getRuntime().isInMaintenanceMode();

                    VirtualMachine vmm[] = hs.getVms();

                    for (VirtualMachine vm : vmm) {
                        System.out.print("\tObject:VM~Name:" + vm.getName() + "~");
                        System.out.print("\tCores:" + (short) vm.getConfig().getHardware().getNumCPU() + "~");
                        System.out.print("\tMem(GB):" + (long) vm.getConfig().getHardware().getMemoryMB() / 1024 + "~");
                        System.out.print("\tState:" + vm.getRuntime().getPowerState().name() + "~");
                        System.out.print("\tVersion:" + vm.getConfig().getVersion() + "~");
                        System.out.print("\tToolStatus:" + vm.getGuest().getToolsStatus().name() + "~");
                        System.out.print("\tCluster:" + cluster.getName() + "~");
                        System.out.println("\tDC:" + datacenter.getName());


                    }
                }
            }
        }
        serviceInstance.getServerConnection().logout();
    }

    private static void printInfo(ObjectContent objectContent) {
        // This is super generic here... To actually relate the objects so you
        // know which HostSystem a VirtualMachine lives on you need to implement
        // some kind of inventory system and use the MOR from the HostSystem
        // and the MOR from the vm.runtime.host
        for (DynamicProperty props : objectContent.getPropSet()) {
            System.out.println(props.val);
        }
    }
}
