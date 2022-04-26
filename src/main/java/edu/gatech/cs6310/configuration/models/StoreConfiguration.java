package edu.gatech.cs6310.configuration.models;

import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.logger.Logger;

import java.io.Serializable;
import java.util.List;

public class StoreConfiguration implements Serializable  {
    String storeName;
    String configurationName;

    public StoreConfiguration() {}

    public String getStorename() {
        return storeName;
    }

    public void setStorename(String storeName) {
        this.storeName = storeName;
    }

    public String getConfigurationname() {
        return configurationName;
    }

    public void setConfigurationname(String configurationName) {
        this.configurationName = configurationName;
    }

    public StoreConfiguration(String storeName, String configurationName) {
        this.storeName = storeName;
        this.configurationName = configurationName;
    }
}
