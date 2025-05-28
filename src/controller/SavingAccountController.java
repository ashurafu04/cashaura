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

            if (initialDeposit.compareTo(BigDecimal.ZERO) <= 0) {
                view.showError("Initial deposit must be greater than zero");
                return;
            }

            if (interestRate.compareTo(BigDecimal.ZERO) <= 0) {
                view.showError("Interest rate must be greater than zero");
                return;
            }

            currentAccount = savingAccountService.createSavingAccount(clientId, initialDeposit, interestRate);
            view.showSuccess("Saving account created successfully!");
            view.updateAccountDetails(currentAccount);
            view.clearFields();

        } catch (SQLException e) {
            view.showError("Error creating saving account: " + e.getMessage());
        }
    }

    private void handleCalculateInterest() {
        try {
            if (currentAccount != null) {
                savingAccountService.calculateAndApplyInterest(currentAccount.getIdSavingAccount());
                currentAccount = savingAccountService.getSavingAccount(currentAccount.getIdSavingAccount());
                view.updateAccountDetails(currentAccount);
                view.showSuccess("Interest calculated and applied successfully!");
            }
        } catch (SQLException e) {
            view.showError("Error calculating interest: " + e.getMessage());
        }
    }

    private void handleCloseAccount() {
        try {
            if (currentAccount != null) {
                boolean closed = savingAccountService.closeSavingAccount(currentAccount.getIdSavingAccount());
                if (closed) {
                    view.showSuccess("Saving account closed successfully!");
                    currentAccount = null;
                    view.updateAccountDetails(null);
                } else {
                    view.showError("Failed to close account");
                }
            }
        } catch (SQLException e) {
            view.showError("Error closing account: " + e.getMessage());
        }
    }

    public void showView() {
        view.setVisible(true);
    }
}