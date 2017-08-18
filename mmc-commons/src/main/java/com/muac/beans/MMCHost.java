package com.muac.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class MMCHost extends MMCBean implements Serializable {

    private boolean inMaintenanceMode;

    private List<MMCVm> vms = new ArrayList<>();
    private String state;

    public List<MMCVm> getVms() {
        return vms;
    }

    public void setVms(List<MMCVm> vms) {
        this.vms = vms;
    }

    public boolean isInMaintenanceMode() {
        return inMaintenanceMode;
    }

    public void setInMaintenanceMode(boolean inMaintenanceMode) {
        this.inMaintenanceMode = inMaintenanceMode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
