package model;

import java.math.BigDecimal;
import java.util.Date;

public class SavingAccount {
    private int idSavingAccount;
    private int idAccount;
    private BigDecimal interestRate;
    private Date lastInterestCalcDate;
    private Account baseAccount;  // Reference to the base account
    private java.sql.Timestamp deletedAt;  // Add deletion timestamp field

    // Default constructor
    public SavingAccount() {}

    // Parameterized constructor
    public SavingAccount(int idSavingAccount, int idAccount, BigDecimal interestRate, Date lastInterestCalcDate) {
        this.idSavingAccount = idSavingAccount;
        this.idAccount = idAccount;
        this.interestRate = interestRate;
        this.lastInterestCalcDate = lastInterestCalcDate;
    }

    // Getters and Setters
    public int getIdSavingAccount() {
        return idSavingAccount;
    }

    public void setIdSavingAccount(int idSavingAccount) {
        this.idSavingAccount = idSavingAccount;
    }

    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Date getLastInterestCalcDate() {
        return lastInterestCalcDate;
    }

    public void setLastInterestCalcDate(Date lastInterestCalcDate) {
        this.lastInterestCalcDate = lastInterestCalcDate;
    }

    public Account getBaseAccount() {
        return baseAccount;
    }

    public void setBaseAccount(Account baseAccount) {
        this.baseAccount = baseAccount;
    }

    public java.sql.Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(java.sql.Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public String toString() {
        return "Saving Account (ID: " + idSavingAccount + ", Interest: " + interestRate + "% Last Calc: " + lastInterestCalcDate + ")";
    }
}
