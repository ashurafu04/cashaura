package service;

import dao.SavingAccountDAO;
import dao.AccountDAO;
import dao.impl.SavingAccountDAOImpl;
import model.SavingAccount;
import model.Account;
import java.util.List;
import java.util.Date;
import java.math.BigDecimal;
import java.sql.SQLException;

public class SavingAccountService {
    private final SavingAccountDAO savingAccountDAO;
    private final AccountDAO accountDAO;

    public SavingAccountService(SavingAccountDAO savingAccountDAO, AccountDAO accountDAO) {
        this.savingAccountDAO = savingAccountDAO;
        this.accountDAO = accountDAO;
    }

    public SavingAccount createSavingAccount(int idClient, BigDecimal initialDeposit, BigDecimal interestRate) throws SQLException {
        // Create base account first
        Account baseAccount = new Account();
        baseAccount.setIdClient(idClient);
        baseAccount.setBalance(initialDeposit);
        baseAccount.setType("SAVINGS");
        baseAccount = accountDAO.create(baseAccount);

        // Create saving account
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setIdSavingAccount(baseAccount.getIdAccount());
        savingAccount.setIdAccount(baseAccount.getIdAccount());
        savingAccount.setInterestRate(interestRate);
        savingAccount.setLastInterestCalcDate(new Date());
        savingAccount.setBaseAccount(baseAccount);

        return savingAccountDAO.create(savingAccount);
    }

    public boolean deleteSavingAccount(int savingAccountId) {
        return savingAccountDAO.delete(savingAccountId);
    }

    public boolean deposit(int savingAccountId, double amount) {
        SavingAccount sa = savingAccountDAO.findById(savingAccountId);
        if (sa == null) return false;
        Account acc = accountDAO.findById(sa.getAccountId());
        if (acc == null) return false;
        acc.deposit(amount);
        return accountDAO.update(acc);
    }

    public boolean withdraw(int savingAccountId, double amount) {
        SavingAccount sa = savingAccountDAO.findById(savingAccountId);
        if (sa == null) return false;
        Account acc = accountDAO.findById(sa.getAccountId());
        if (acc == null) return false;
        if (acc.getBalance() < amount) return false;
        acc.withdraw(amount);
        return accountDAO.update(acc);
    }

    public boolean transfer(int fromSavingAccountId, int toAccountId, double amount) {
        SavingAccount sa = savingAccountDAO.findById(fromSavingAccountId);
        if (sa == null) return false;
        Account from = accountDAO.findById(sa.getAccountId());
        Account to = accountDAO.findById(toAccountId);
        if (from == null || to == null) return false;
        if (from.getBalance() < amount) return false;
        from.withdraw(amount);
        to.deposit(amount);
        return accountDAO.update(from) && accountDAO.update(to);
    }

    public boolean applyInterest(int savingAccountId) {
        SavingAccount sa = savingAccountDAO.findById(savingAccountId);
        if (sa == null) return false;
        Account acc = accountDAO.findById(sa.getAccountId());
        if (acc == null) return false;
        double interest = acc.getBalance() * sa.getInterest();
        acc.deposit(interest);
        sa.setLastInterestCalcDate(new Date());
        return accountDAO.update(acc) && savingAccountDAO.update(sa);
    }

    public SavingAccount getSavingAccount(int idSavingAccount) throws SQLException {
        return savingAccountDAO.findById(idSavingAccount);
    }

    public List<SavingAccount> getSavingAccountsByClientId(int clientId) {
        List<Account> accounts = accountDAO.findByClientId(clientId);
        List<SavingAccount> result = new java.util.ArrayList<>();
        for (Account acc : accounts) {
            SavingAccount sa = savingAccountDAO.findByAccountId(acc.getId());
            if (sa != null) result.add(sa);
        }
        return result;
    }

    public boolean updateInterestRate(int savingAccountId, double newInterest) {
        SavingAccount sa = savingAccountDAO.findById(savingAccountId);
        if (sa == null) return false;
        sa.setInterest(newInterest);
        return savingAccountDAO.update(sa);
    }

    public void applyInterestToAll() {
        List<SavingAccount> all = savingAccountDAO.findAll();
        for (SavingAccount sa : all) {
            applyInterest(sa.getSavingAccountId());
        }
    }

    public List<SavingAccount> getClientSavingAccounts(int idClient) throws SQLException {
        return savingAccountDAO.findByClientId(idClient);
    }

    public void calculateAndApplyInterest(int idSavingAccount) throws SQLException {
        SavingAccount savingAccount = savingAccountDAO.findById(idSavingAccount);
        if (savingAccount == null) {
            throw new IllegalArgumentException("Saving account not found");
        }

        Account baseAccount = accountDAO.findById(savingAccount.getIdAccount());
        if (baseAccount == null) {
            throw new IllegalArgumentException("Base account not found");
        }

        // Calculate days since last interest calculation
        Date now = new Date();
        long daysSinceLastCalc = (now.getTime() - savingAccount.getLastInterestCalcDate().getTime()) / (1000 * 60 * 60 * 24);
        
        if (daysSinceLastCalc > 0) {
            // Calculate interest: principal * rate * (days/365)
            BigDecimal principal = baseAccount.getBalance();
            BigDecimal rate = savingAccount.getInterestRate().divide(new BigDecimal("100")); // Convert percentage to decimal
            BigDecimal daysInYear = new BigDecimal("365");
            BigDecimal interest = principal.multiply(rate)
                                         .multiply(new BigDecimal(daysSinceLastCalc))
                                         .divide(daysInYear, 2, BigDecimal.ROUND_HALF_UP);

            // Apply interest to account balance
            baseAccount.setBalance(baseAccount.getBalance().add(interest));
            accountDAO.update(baseAccount);

            // Update last calculation date
            savingAccount.setLastInterestCalcDate(now);
            savingAccountDAO.updateInterestCalculationDate(idSavingAccount);
        }
    }

    public void updateInterestRate(int idSavingAccount, BigDecimal newRate) throws SQLException {
        if (newRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative");
        }
        savingAccountDAO.updateInterestRate(idSavingAccount, newRate.doubleValue());
    }

    public boolean closeSavingAccount(int idSavingAccount) throws SQLException {
        SavingAccount savingAccount = savingAccountDAO.findById(idSavingAccount);
        if (savingAccount == null) {
            return false;
        }

        // Calculate and apply any remaining interest
        calculateAndApplyInterest(idSavingAccount);

        // Delete the saving account
        boolean savingAccountDeleted = savingAccountDAO.delete(idSavingAccount);
        if (!savingAccountDeleted) {
            return false;
        }

        // Mark the base account as deleted
        Account baseAccount = accountDAO.findById(savingAccount.getIdAccount());
        if (baseAccount != null) {
            baseAccount.setDeleted(true);
            baseAccount.setDeletedAt(new Date());
            accountDAO.update(baseAccount);
        }

        return true;
    }
}