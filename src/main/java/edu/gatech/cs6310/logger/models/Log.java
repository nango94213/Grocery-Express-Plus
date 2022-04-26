package edu.gatech.cs6310.logger.models;

import java.util.Date;
import java.util.UUID;

public class Log {

    private String logId;
    private String commandType;
    private String level;
    private String message;
    private Date creationDate;

    public Log(String commandType, String level, String message, Date creationDate) {
        this.logId = UUID.randomUUID().toString();
        this.commandType = commandType;
        this.level = level;
        this.message = message;
        this.creationDate = creationDate;
    }

    public Log() {}

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
