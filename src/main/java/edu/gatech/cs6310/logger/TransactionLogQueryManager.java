package edu.gatech.cs6310.logger;

import de.vandermeer.asciitable.AsciiTable;
import edu.gatech.cs6310.dao.DatabaseClient;
import edu.gatech.cs6310.logger.models.Log;
import edu.gatech.cs6310.logger.models.TransactionLog;

import java.util.List;

public class TransactionLogQueryManager {

    private final DatabaseClient databaseClient;

    public TransactionLogQueryManager(DatabaseClient dbc) {
        this.databaseClient = dbc;
    }

    public List<TransactionLog> getTransactionLogByUsername(String username) {
        List<TransactionLog> logs = databaseClient.selectWhere("TransactionLog", "customerusername", username);
        return logs;
    }

    public List<TransactionLog> getTransactionLogByStoreName(String storeName) {
        List<TransactionLog> logs = databaseClient.selectWhere("TransactionLog", "storename", storeName);
        return logs;
    }
}
