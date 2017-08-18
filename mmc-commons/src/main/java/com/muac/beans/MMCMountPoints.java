package com.muac.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MMCMountPoints implements MMCResponse, Serializable {

    List<MMCMountPoint> mmcMountPoints = new ArrayList<>();

    public List<MMCMountPoint> getMmcMountPoints() {
        return mmcMountPoints;
    }

    public void setMmcMountPoints(List<MMCMountPoint> mmcMountPoints) {
        this.mmcMountPoints = mmcMountPoints;
    }
}
