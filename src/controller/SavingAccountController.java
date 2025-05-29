package controller;

import model.SavingAccount;
import service.SavingAccountService;
import service.AccountService;
import view.SavingAccountView;
import dao.impl.SavingAccountDAOImpl;
import dao.AccountDAO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SavingAccountController {
    private SavingAccountView view;
    private SavingAccountService savingAccountService;
    private AccountService accountService;
    private int clientId;
    private SavingAccount currentAccount;

    public SavingAccountController(int clientId) {
        this.clientId = clientId;
        this.view = new SavingAccountView();
        this.savingAccountService = new SavingAccountService(new SavingAccountDAOImpl(), new AccountDAO());
        this.accountService = new AccountService();
        initializeController();
    }

    private void initializeController() {
        // Load existing saving account if any
        try {
            java.util.List<SavingAccount> accounts = savingAccountService.getClientSavingAccounts(clientId);
            if (!accounts.isEmpty()) {
                currentAccount = accounts.get(0);
                view.updateAccountDetails(currentAccount);
            }
        } catch (SQLException e) {
            view.showError("Error loading saving account: " + e.getMessage());
        }

        // Add listeners
        view.addCreateListener(e -> handleCreateAccount());
        view.addCalculateInterestListener(e -> handleCalculateInterest());
        view.addCloseAccountListener(e -> handleCloseAccount());
        view.addBackListener(e -> view.dispose());
    }

    private void handleCreateAccount() {
        try {
            BigDecimal initialDeposit = view.getInitialDeposit();
            BigDecimal interestRate = view.getInterestRate();

            // Validate initial deposit
            if (initialDeposit.compareTo(BigDecimal.ZERO) <= 0) {
                view.showError("Initial deposit must be greater than zero");
                return;
            }

            // Validate interest rate (between 0 and 100)
            if (interestRate.compareTo(BigDecimal.ZERO) <= 0 || interestRate.compareTo(new BigDecimal("100")) > 0) {
                view.showError("Interest rate must be between 0 and 100");
                return;
            }

            // Check if client already has a saving account
            List<SavingAccount> existingAccounts = savingAccountService.getClientSavingAccounts(clientId);
            if (!existingAccounts.isEmpty()) {
                view.showError("You already have a saving account");
                return;
            }

            // Create the saving account
            currentAccount = savingAccountService.createSavingAccount(clientId, initialDeposit, interestRate);
            
            if (currentAccount != null && currentAccount.getBaseAccount() != null) {
                view.showSuccess("Saving account created successfully!");
                view.updateAccountDetails(currentAccount);
                view.clearFields();
            } else {
                view.showError("Failed to create saving account");
            }

        } catch (NumberFormatException e) {
            view.showError("Please enter valid numbers for deposit and interest rate");
        } catch (IllegalArgumentException e) {
            view.showError(e.getMessage());
        } catch (SQLException e) {
            view.showError("Error creating saving account: " + e.getMessage());
        }
    }

    private void handleCalculateInterest() {
        try {
            if (currentAccount == null) {
                view.showError("No saving account found");
                return;
            }

            // Calculate and apply interest
            savingAccountService.calculateAndApplyInterest(currentAccount.getIdSavingAccount());
            
            // Refresh account details
            currentAccount = savingAccountService.getSavingAccount(currentAccount.getIdSavingAccount());
            if (currentAccount != null && currentAccount.getBaseAccount() != null) {
                view.updateAccountDetails(currentAccount);
                view.showSuccess("Interest calculated and applied successfully!");
            } else {
                view.showError("Failed to refresh account details");
            }
        } catch (SQLException e) {
            view.showError("Error calculating interest: " + e.getMessage());
        } catch (Exception e) {
            view.showError("Unexpected error: " + e.getMessage());
        }
    }

    private void handleCloseAccount() {
        try {
            if (currentAccount != null) {
                savingAccountService.closeSavingAccount(currentAccount.getIdSavingAccount());
                view.showSuccess("Saving account closed successfully!");
                currentAccount = null;
                view.updateAccountDetails(null);
            }
        } catch (SQLException e) {
            view.showError("Error closing account: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            view.showError(e.getMessage());
        }
    }

    public void showView() {
        view.setVisible(true);
    }
}