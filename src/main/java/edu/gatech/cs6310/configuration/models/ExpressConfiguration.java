package edu.gatech.cs6310.configuration.models;

import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.logger.Logger;

import java.util.List;

public class ExpressConfiguration {
    // configuration name is unqiue
    private String name;
    private String storeName;
    private String type;
    private String category;
    private int quota;
    private Boolean active;

    public ExpressConfiguration() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStorename() {
        return storeName;
    }

    public void setStorename(String storeName) {
        this.storeName = storeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ExpressConfiguration(String name, String type, String storename, Boolean active, int quota, String category) {
        this.name = name;
        this.type = type;
        this.storeName = storename;
        this.active = active;
        this.quota = quota;
        this.category = category;
    }
}
