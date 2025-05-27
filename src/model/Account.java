package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Account {
    private int id;
    private String rib;
    private BigDecimal balance;
    private Timestamp createdAt;
    private boolean isDeleted;
    private Timestamp deletedAt;
    private String type;
    private int clientId;

    public Account() {
        this.balance = BigDecimal.ZERO;
        this.isDeleted = false;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public Account(int id, String rib, BigDecimal balance, Timestamp createdAt, 
                  boolean isDeleted, Timestamp deletedAt, String type, int clientId) {
        this.id = id;
        this.rib = rib;
        this.balance = balance;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
        this.type = type;
        this.clientId = clientId;
    }

    // Getters and Setters with validation
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRib() { return rib; }
    public void setRib(String rib) {
        if (rib == null || rib.trim().isEmpty()) {
            throw new IllegalArgumentException("RIB cannot be null or empty");
        }
        this.rib = rib;
    }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) {
        if (balance == null) {
            throw new IllegalArgumentException("Balance cannot be null");
        }
        this.balance = balance;
    }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { 
        this.isDeleted = deleted;
        if (deleted) {
            this.deletedAt = new Timestamp(System.currentTimeMillis());
        }
    }

    public Timestamp getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Timestamp deletedAt) { this.deletedAt = deletedAt; }

    public String getType() { return type; }
    public void setType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Account type cannot be null or empty");
        }
        this.type = type;
    }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    // Business logic methods
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }
        this.balance = this.balance.subtract(amount);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", rib='" + rib + '\'' +
                ", balance=" + balance +
                ", type='" + type + '\'' +
                '}';
    }
}

