package edu.gatech.cs6310.logger;

import edu.gatech.cs6310.dao.DatabaseClient;
import edu.gatech.cs6310.logger.models.LevelType;
import edu.gatech.cs6310.logger.models.Log;

import java.util.Date;

public class Logger {

    private final DatabaseClient databaseClient;

    public Logger(DatabaseClient dbc) {
        this.databaseClient = dbc;
    }

    public void info(String commandType, String message) {
        Log infoLog = new Log(commandType, LevelType.INFO.name(), message, new Date());

        logEvent(infoLog);
    }

    public void error(String commandType, String message) {
        Log errorLog = new Log(commandType, LevelType.ERROR.name(), message, new Date());

        logEvent(errorLog);
    }

    public void warn(String commandType, String message) {
        Log warnLog = new Log(commandType, LevelType.WARN.name(), message, new Date());

        logEvent(warnLog);
    }

    public void debug(String commandType, String message) {
        Log debugLog = new Log(commandType, LevelType.DEBUG.name(), message, new Date());

        logEvent(debugLog);
    }

    private void logEvent(Log log) {
        try {
            databaseClient.save(log);
        } catch (Exception e) {
            System.out.printf("Could not save log due to %s: %s", e.getMessage(), e);
        }
    }
}
