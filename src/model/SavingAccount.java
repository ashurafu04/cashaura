package model;

public class SavingAccount {
    private int savingAccountId;
    private int accountId;
    private double interest;

    public SavingAccount() {}

    public SavingAccount(int savingAccountId, int accountId, double interest) {
        this.savingAccountId = savingAccountId;
        this.accountId = accountId;
        this.interest = interest;
    }

    public int getSavingAccountId() {
        return savingAccountId;
    }

    public void setSavingAccountId(int savingAccountId) {
        this.savingAccountId = savingAccountId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    @Override
    public String toString() {
        return "Saving Account (ID: " + savingAccountId + ", Interest: " + interest + "%)";
    }
}
