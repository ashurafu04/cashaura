package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transfer {
    private int id;
    private BigDecimal amount;
    private String description;
    private Timestamp timestamp;
    private String status;
    private int sourceAccountId;  // maps to id_account
    private int targetAccountId;  // maps to id_recipient

    public Transfer(int id, BigDecimal amount, String description, Timestamp timestamp, int sourceAccountId, int targetAccountId) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.timestamp = timestamp;
        this.status = "COMPLETED";  // Default status for transfers
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
    }

    // Getters
    public int getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public int getSourceAccountId() {
        return sourceAccountId;
    }

    public int getTargetAccountId() {
        return targetAccountId;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSourceAccountId(int sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public void setTargetAccountId(int targetAccountId) {
        this.targetAccountId = targetAccountId;
    }
} 