package com.muac.beans;

import java.io.Serializable;
import java.util.Date;

public final class MMCAlarm implements Serializable {

    private String key;
    private Date date;
    //private String overallState;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date time) {
        this.date = time;
    }

/*
    public void setOverallState(String overallState) {
        this.overallState = overallState;
    }

    public String getOverallState() {
        return overallState;
    }
*/


}
