package com.muac.beans;

import java.io.Serializable;

public class MMCMountPoint extends MMCBean implements Serializable, MMCResponse {

    Double percent;
    String hostname;

    public MMCMountPoint() {
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
