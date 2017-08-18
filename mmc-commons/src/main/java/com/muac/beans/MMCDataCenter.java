package com.muac.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MMCDataCenter extends MMCBean implements Serializable, MMCResponse {

    private List<MMCCluster> clusters = new ArrayList<>();
    private List<MMCDatastore> datastores = new ArrayList<>();

    public MMCDataCenter() {
        this.clusters = clusters;
    }

    public List<MMCCluster> getClusters() {
        return clusters;
    }

    public void setClusters(List<MMCCluster> clusters) {
        this.clusters = clusters;
    }

    public List<MMCDatastore> getDatastores() {
        return datastores;
    }

    public void setDatastores(List<MMCDatastore> datastores) {
        this.datastores = datastores;
    }
}
