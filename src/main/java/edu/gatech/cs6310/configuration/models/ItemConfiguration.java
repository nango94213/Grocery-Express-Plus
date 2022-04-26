package edu.gatech.cs6310.configuration.models;

import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.logger.Logger;

import java.io.Serializable;
import java.util.List;

public class ItemConfiguration implements Serializable {

    String storeName;
    String itemName;
    String configurationName;

    public ItemConfiguration() {}

    public String getStorename() {
        return storeName;
    }

    public void setStorename(String storeName) {
        this.storeName = storeName;
    }

    public String getItemname() {
        return itemName;
    }

    public void setItemname(String itemName) {
        this.itemName = itemName;
    }

    public String getConfigurationname() {
        return configurationName;
    }

    public void setConfigurationname(String configurationName) {
        this.configurationName = configurationName;
    }

    public ItemConfiguration(String storeName, String itemName, String configurationName) {
        this.storeName = storeName;
        this.itemName = itemName;
        this.configurationName = configurationName;
    }
}
