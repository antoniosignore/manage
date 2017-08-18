package com.muac.beans;


import java.io.Serializable;

public final class MMCVm extends MMCBean implements Serializable {

    private String state;
    private String category;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
