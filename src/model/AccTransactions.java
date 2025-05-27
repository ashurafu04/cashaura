package model;

public class AccTransactions {
    private int id;
    private String type;
    private double amount;
    private java.sql.Timestamp transactionDate;
    private int accountId;
    private int recipientId;

    public AccTransactions() {}

    public AccTransactions(int id, String type, double amount, java.sql.Timestamp transactionDate, int accountId, int recipientId) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.accountId = accountId;
        this.recipientId = recipientId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public java.sql.Timestamp getTransactionDate() { return transactionDate; }
    public void setTransactionDate(java.sql.Timestamp transactionDate) { this.transactionDate = transactionDate; }
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }
    public int getRecipientId() { return recipientId; }
    public void setRecipientId(int recipientId) { this.recipientId = recipientId; }

    @Override
    public String toString() {
        return type + " of " + amount + " DH on " + transactionDate.toString();
    }
}
