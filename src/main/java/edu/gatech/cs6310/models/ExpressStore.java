package edu.gatech.cs6310.models;

import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.logger.Logger;

import java.util.List;

public class ExpressStore {
    private int revenue;
    private String name;
    private String storeOwnerUsername;

    public ExpressStore() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreownerusername() {
        return storeOwnerUsername;
    }

    public void setStoreownerusername(String storeOwnerUsername) {
        this.storeOwnerUsername = storeOwnerUsername;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public ExpressStore(String name, String storeOwnerUsername, int revenue) {
        this.name = name;
        this.storeOwnerUsername = storeOwnerUsername;
        this.revenue = revenue;
    }
}