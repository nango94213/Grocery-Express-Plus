package edu.gatech.cs6310.logger;

import edu.gatech.cs6310.dao.DatabaseClient;
import edu.gatech.cs6310.logger.models.TransactionLog;

import java.util.Date;

public class TransactionLogger {

    private final DatabaseClient databaseClient;

    public TransactionLogger(DatabaseClient dbc) {
        this.databaseClient = dbc;
    }

    public void log(String username, String storeName, String itemName, int price, int quantity) {
        TransactionLog log = new TransactionLog(storeName, itemName, username, price, quantity, new Date());

        this.logEvent(log);
    }

    private void logEvent(TransactionLog log) {
        try {
            databaseClient.save(log);
        } catch (Exception e) {
            System.out.printf("Could not save log due to %s: %s", e.getMessage(), e);
        }
    }
}
