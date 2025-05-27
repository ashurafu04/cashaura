package service;

import dao.AccountDAO;
import model.Account;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AccountService {
    private final AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public Account createAccount(String rib, String type, int clientId) throws SQLException {
        // Validate RIB uniqueness
        if (accountDAO.existsByRib(rib)) {
            throw new IllegalArgumentException("RIB already exists");
        }

        Account account = new Account();
        account.setRib(rib);
        account.setType(type);
        account.setClientId(clientId);
        
        accountDAO.create(account);
        return account;
    }

    public Optional<Account> getAccount(int accountId) throws SQLException {
        return accountDAO.findById(accountId);
    }

    public List<Account> getClientAccounts(int clientId) throws SQLException {
        return accountDAO.findByClientId(clientId);
    }

    public void deposit(int accountId, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        Optional<Account> accountOpt = accountDAO.findById(accountId);
        if (!accountOpt.isPresent()) {
            throw new IllegalArgumentException("Account not found");
        }

        Account account = accountOpt.get();
        account.deposit(amount);
        accountDAO.updateBalance(accountId, account.getBalance());
    }

    public void withdraw(int accountId, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        Optional<Account> accountOpt = accountDAO.findById(accountId);
        if (!accountOpt.isPresent()) {
            throw new IllegalArgumentException("Account not found");
        }

        Account account = accountOpt.get();
        account.withdraw(amount);
        accountDAO.updateBalance(accountId, account.getBalance());
    }

    public void transfer(int fromAccountId, int toAccountId, BigDecimal amount) throws SQLException {
        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        Optional<Account> fromAccountOpt = accountDAO.findById(fromAccountId);
        Optional<Account> toAccountOpt = accountDAO.findById(toAccountId);

        if (!fromAccountOpt.isPresent() || !toAccountOpt.isPresent()) {
            throw new IllegalArgumentException("One or both accounts not found");
        }

        Account fromAccount = fromAccountOpt.get();
        Account toAccount = toAccountOpt.get();

        // Perform withdrawal first to validate sufficient funds
        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        // Update both accounts in the database
        accountDAO.updateBalance(fromAccountId, fromAccount.getBalance());
        accountDAO.updateBalance(toAccountId, toAccount.getBalance());
    }

    public void closeAccount(int accountId) throws SQLException {
        Optional<Account> accountOpt = accountDAO.findById(accountId);
        if (!accountOpt.isPresent()) {
            throw new IllegalArgumentException("Account not found");
        }

        Account account = accountOpt.get();
        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("Cannot close account with positive balance");
        }

        accountDAO.softDelete(accountId);
    }

    public Account getFirstAccountByClientId(int clientId) {
        try {
            java.util.List<Account> accounts = new AccountDAO().findByClientId(clientId);
            if (!accounts.isEmpty()) {
                return accounts.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Account> getAccountsByClientId(int clientId) throws SQLException {
        return accountDAO.findByClientId(clientId);
    }

    public List<Account> getAllAccounts() throws SQLException {
        return accountDAO.findAll();
    }

    public Account getAccountByRib(String rib) throws SQLException {
        Optional<Account> account = accountDAO.findByRib(rib);
        return account.orElse(null);
    }
} 