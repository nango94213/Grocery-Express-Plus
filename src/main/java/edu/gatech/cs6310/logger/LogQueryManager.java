package edu.gatech.cs6310.logger;

import de.vandermeer.asciitable.AsciiTable;
import edu.gatech.cs6310.dao.DatabaseClient;
import edu.gatech.cs6310.logger.models.Log;

import java.util.List;

public class LogQueryManager {

    private final DatabaseClient databaseClient;

    public LogQueryManager(DatabaseClient dbc) {
        this.databaseClient = dbc;
    }

    /**
     * Prints the system logs based on command type, level and log pattern.
     *
     * This API supports the * wildcard when the user decides to fetch all logs.
     *
     * @param commandType either create_store, create_customer, etc.
     * @param level one of the corresponding {@link edu.gatech.cs6310.logger.models.LevelType}
     * @param messagePattern the actual logged message
     */
    public void queryLogs(String commandType,
                          String level,
                          String messagePattern) {
        commandType = commandType.replace('*', '%');
        level = level.replace('*', '%');
        messagePattern = messagePattern.replace('*', '%');

        List<Log> logs = databaseClient.selectLike(
                "Log",
                "commandType", commandType,
                "level", level,
                "message", messagePattern
        );

        AsciiTable asciiTable = asAsciiTable(logs);

        String logTable = asciiTable.render();

        System.out.println(logTable);
    }

    /**
     * Convert the list of logs into a user-friendly ASCII Table.
     *
     * @param logs logs to add to the table
     * @return the generated ascii table
     */
    private AsciiTable asAsciiTable(List<Log> logs) {
        AsciiTable asciiTable = new AsciiTable();

        asciiTable.addRule();
        asciiTable.addRow("Log ID", "Operation Type", "Level", "Message", "Creation Date");
        asciiTable.addRule();

        if (logs != null) {
            for (Log log : logs) {
                asciiTable.addRow(
                        log.getLogId(),
                        log.getCommandType(),
                        log.getLevel(),
                        log.getMessage(),
                        log.getCreationDate().toString()
                );
                asciiTable.addRule();
            }
        }

        return asciiTable;
    }

}
