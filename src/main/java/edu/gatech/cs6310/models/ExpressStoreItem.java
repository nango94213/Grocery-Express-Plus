package edu.gatech.cs6310.models;

public class ExpressStoreItem {
    int weight;
    String name;
    String storeName;
    String category;

    public ExpressStoreItem() {}

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public ExpressStoreItem(String name, String storeName, int weight, String category) {
        this.name = name;
        this.storeName = storeName;
        this.weight = weight;
        this.category = category;
    }
}

