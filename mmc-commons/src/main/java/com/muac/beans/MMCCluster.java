package com.muac.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class MMCCluster extends MMCBean implements Serializable {

    List<MMCHost> nodes = new ArrayList<>();


    public List<MMCHost> getNodes() {
        return nodes;
    }

    public void setNodes(List<MMCHost> nodes) {
        this.nodes = nodes;
    }

}
