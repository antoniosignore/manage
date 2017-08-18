package com.muac.beans;

import java.io.Serializable;
import java.util.List;

public class MMCVms implements MMCResponse, Serializable {

    List<MMCVm> virtualMachines;

    public MMCVms() {
    }

    public MMCVms(List<MMCVm> virtualMachines) {
        this.virtualMachines = virtualMachines;
    }

    public List<MMCVm> getVirtualMachines() {
        return virtualMachines;
    }

    public void setVirtualMachines(List<MMCVm> virtualMachines) {
        this.virtualMachines = virtualMachines;
    }
}
