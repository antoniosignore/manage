package com.muac.beans;

import java.util.ArrayList;
import java.util.List;

public class MMCBean {

    List<MMCAlarm> mmcAlarms = new ArrayList<>();
    private String name;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MMCAlarm> getMmcAlarms() {
        return mmcAlarms;
    }

    public void setMmcAlarms(List<MMCAlarm> mmcAlarms) {
        this.mmcAlarms = mmcAlarms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
