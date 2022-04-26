package edu.gatech.cs6310.logger.models;

import java.util.Date;
import java.util.UUID;

public class TransactionLog {

    private String logId;
    private String storeName;
    private String itemName;
    private String customerUsername;
    private int unitPrice;
    private int quantity;
    private Date creationDate;

    public TransactionLog(String storename, String itemname, String customerusername, int unitprice, int quantity, Date creationDate) {
        this.logId = UUID.randomUUID().toString();
        this.storeName = storename;
        this.itemName = itemname;
        this.customerUsername = customerusername;
        this.unitPrice = unitprice;
        this.quantity = quantity;
        this.creationDate = creationDate;
    }

    public TransactionLog() {}

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getStorename() { return storeName; }

    public void setStorename(String storeName) { this.storeName = storeName; }

    public String getItemname() { return itemName; }

    public void setItemname(String itemName) { this.itemName = itemName; }

    public String getCustomerusername() { return customerUsername; }

    public void setCustomerusername(String customerUsername) { this.customerUsername = customerUsername; }

    public int getUnitprice() { return unitPrice; }

    public void setUnitprice(int unitPrice) { this.unitPrice = unitPrice; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
