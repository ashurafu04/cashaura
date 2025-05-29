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
import java.util.ArrayList;
import util.RIBGenerator;

public class SavingAccountService {
    private final SavingAccountDAO savingAccountDAO;
    private final AccountDAO accountDAO;

    public SavingAccountService() {
        this.savingAccountDAO = new SavingAccountDAOImpl();
        this.accountDAO = new AccountDAO();
    }

    public SavingAccountService(SavingAccountDAO savingAccountDAO, AccountDAO accountDAO) {
        this.savingAccountDAO = savingAccountDAO;
        this.accountDAO = accountDAO;
    }

    public SavingAccount createSavingAccount(int idClient, BigDecimal initialDeposit, BigDecimal interestRate) throws SQLException {
        if (initialDeposit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial deposit cannot be negative");
        }
        if (interestRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative");
        }

        // Create base account first
        Account baseAccount = new Account();
        baseAccount.setClientId(idClient);
        baseAccount.setBalance(initialDeposit);
        baseAccount.setType("SAVINGS");
        baseAccount.setRib(RIBGenerator.generateRIB());  // Generate a unique RIB
        accountDAO.create(baseAccount);
        
        // Get the created account's ID
        int accountId = baseAccount.getId();
        if (accountId == 0) {
            throw new SQLException("Failed to create base account");
        }

        // Create saving account
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setIdSavingAccount(accountId);
        savingAccount.setIdAccount(accountId);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setLastInterestCalcDate(new Date());
        savingAccount.setBaseAccount(baseAccount);

        return savingAccountDAO.create(savingAccount);
    }

    public boolean deleteSavingAccount(int savingAccountId) throws SQLException {
        return savingAccountDAO.delete(savingAccountId);
    }

    public boolean deposit(int savingAccountId, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        SavingAccount sa = savingAccountDAO.findById(savingAccountId);
        if (sa == null) return false;

        var accOpt = accountDAO.findById(sa.getIdAccount());
        if (accOpt.isEmpty()) return false;
        Account acc = accOpt.get();

        acc.setBalance(acc.getBalance().add(amount));
        accountDAO.update(acc);
        return true;
    }

    public boolean withdraw(int savingAccountId, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        SavingAccount sa = savingAccountDAO.findById(savingAccountId);
        if (sa == null) return false;

        var accOpt = accountDAO.findById(sa.getIdAccount());
        if (accOpt.isEmpty()) return false;
        Account acc = accOpt.get();

        if (acc.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }

        acc.setBalance(acc.getBalance().subtract(amount));
        accountDAO.update(acc);
        return true;
    }

    public boolean transfer(int fromSavingAccountId, int toAccountId, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        SavingAccount sa = savingAccountDAO.findById(fromSavingAccountId);
        if (sa == null) return false;

        var fromOpt = accountDAO.findById(sa.getIdAccount());
        var toOpt = accountDAO.findById(toAccountId);
        if (fromOpt.isEmpty() || toOpt.isEmpty()) return false;
        
        Account from = fromOpt.get();
        Account to = toOpt.get();

        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds for transfer");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        accountDAO.update(from);
        accountDAO.update(to);
        return true;
    }

    public boolean applyInterest(int savingAccountId) throws SQLException {
        SavingAccount sa = savingAccountDAO.findById(savingAccountId);
        if (sa == null) return false;
        
        var accOpt = accountDAO.findById(sa.getIdAccount());
        if (accOpt.isEmpty()) return false;
        Account acc = accOpt.get();
        
        BigDecimal interest = acc.getBalance().multiply(sa.getInterestRate());
        acc.deposit(interest);
        sa.setLastInterestCalcDate(new Date());
        
        try {
            accountDAO.update(acc);
            savingAccountDAO.update(sa);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public SavingAccount getSavingAccount(int idSavingAccount) throws SQLException {
        return savingAccountDAO.findById(idSavingAccount);
    }

    public List<SavingAccount> getSavingAccountsByClientId(int clientId) throws SQLException {
        return savingAccountDAO.findByClientId(clientId);
    }

    public boolean updateInterestRate(int savingAccountId, BigDecimal newRate) throws SQLException {
        if (newRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative");
        }
        try {
            savingAccountDAO.updateInterestRate(savingAccountId, newRate);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void applyInterestToAll() throws SQLException {
        List<SavingAccount> accounts = savingAccountDAO.findByClientId(-1); // Get all accounts
        for (SavingAccount sa : accounts) {
            applyInterest(sa.getIdSavingAccount());
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

        // Get base account and handle Optional properly
        var baseAccountOpt = accountDAO.findById(savingAccount.getIdAccount());
        if (baseAccountOpt.isEmpty()) {
            throw new IllegalArgumentException("Base account not found");
        }
        Account baseAccount = baseAccountOpt.get();

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

    public void closeSavingAccount(int savingAccountId) throws SQLException {
        SavingAccount savingAccount = savingAccountDAO.findById(savingAccountId);
        if (savingAccount == null) {
            throw new IllegalArgumentException("Saving account not found");
        }

        // Get base account and handle Optional properly
        var baseAccountOpt = accountDAO.findById(savingAccount.getIdAccount());
        if (baseAccountOpt.isEmpty()) {
            throw new IllegalArgumentException("Base account not found");
        }
        Account baseAccount = baseAccountOpt.get();

        // Mark the base account as deleted
        baseAccount.setDeleted(true);
        accountDAO.update(baseAccount);

        // Update the saving account's last calculation date to mark it as inactive
        savingAccount.setLastInterestCalcDate(new Date());
        savingAccountDAO.update(savingAccount);
    }
}