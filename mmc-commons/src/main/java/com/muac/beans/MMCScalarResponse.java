package com.muac.beans;

import java.io.Serializable;

public class MMCScalarResponse implements MMCResponse, Serializable {

    private String key;
    private String value;

    public MMCScalarResponse() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}