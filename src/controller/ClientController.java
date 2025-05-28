package controller;

import service.ClientService;
import service.AccountService;
import view.LoginView;
import view.RegisterView;
import view.DashboardView;
import model.Client;
import model.Account;
import java.sql.Date;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import view.BeneficiaryView;

public class ClientController {
    private final LoginView loginView;
    private RegisterView registerView;
    private final ClientService clientService;
    private final AccountService accountService = new AccountService();
    private DashboardView dashboardView;
    private Client loggedInClient;

    public ClientController(LoginView loginView) {
        this.loginView = loginView;
        this.clientService = new ClientService();
        initController();
    }

    private void initController() {
        loginView.addLoginListener(e -> handleLogin());
        loginView.addRegisterListener(e -> showRegisterView());
    }

    private void handleLogin() {
        String email = loginView.getEmail();
        String password = loginView.getPassword();
        boolean success = clientService.authenticate(email, password);
        if (success) {
            showDashboard(email);
        } else {
            loginView.showError("Invalid email or password.");
            loginView.clearFields();
        }
    }

    private void showRegisterView() {
        registerView = new RegisterView();
        registerView.setVisible(true);
        registerView.addRegisterListener(e -> handleRegister());
        registerView.addBackListener(e -> {
            registerView.dispose();
            loginView.setVisible(true);
        });
        loginView.setVisible(false);
    }

    private void handleRegister() {
        try {
            String firstname = registerView.getFirstname();
            String lastname = registerView.getLastname();
            String cin = registerView.getCin();
            Date birthdate = Date.valueOf(registerView.getBirthdate());
            String email = registerView.getEmail();
            String phone = registerView.getPhone();
            String password = registerView.getPassword();
            Client client = new Client(0, firstname, lastname, cin, birthdate, email, phone, password);
            boolean success = clientService.register(client);
            if (success) {
                registerView.showMessage("Registration successful! You can now log in.");
                registerView.dispose();
                loginView.setVisible(true);
            } else {
                registerView.showError("Registration failed. Please try again.");
            }
        } catch (Exception ex) {
            registerView.showError("Invalid input: " + ex.getMessage());
        }
    }

    private void showDashboard(String email) {
        loggedInClient = clientService.getClientByEmail(email);
        Account account = null;
        String summary = "No account found. Click the Account button to create one.";
        if (loggedInClient != null) {
            account = accountService.getFirstAccountByClientId(loggedInClient.getId());
            if (account != null) {
                summary = String.format("Account Number: %s\nType: %s\nBalance: %s\nOwner: %s %s",
                    account.getRib(), account.getType(), account.getBalance().toPlainString(),
                    loggedInClient.getFirstname(), loggedInClient.getLastname());
            }
        }
        dashboardView = new DashboardView();
        dashboardView.setWelcomeMessage("Welcome, " + (loggedInClient != null ? loggedInClient.getFirstname() : email) + "!");
        dashboardView.setAccountSummary(summary);
        
        // Add listeners for all buttons
        dashboardView.addAccountListener(e -> handleAccountCreation());
        dashboardView.addTransferListener(e -> handleTransferClick());
        dashboardView.addHistoryListener(e -> handleHistoryClick());
        dashboardView.addBeneficiariesListener(e -> handleManageBeneficiaries());
        dashboardView.addSavingAccountListener(e -> handleSavingAccount());
        dashboardView.addLogoutListener(e -> handleLogout());
        
        // Enable/disable buttons based on account existence
        boolean hasAccount = account != null;
        dashboardView.setTransferEnabled(hasAccount);
        dashboardView.setHistoryEnabled(hasAccount);
        dashboardView.setBeneficiariesEnabled(hasAccount);
        dashboardView.setSavingAccountEnabled(hasAccount);
        
        dashboardView.setVisible(true);
        loginView.setVisible(false);
    }

    private void handleAccountCreation() {
        if (loggedInClient == null) return;
        
        try {
            // Check if client already has an account
            Account existingAccount = accountService.getFirstAccountByClientId(loggedInClient.getId());
            
            if (existingAccount != null) {
                JOptionPane.showMessageDialog(dashboardView,
                    "You already have an account with RIB: " + existingAccount.getRib(),
                    "Account Exists",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Generate a unique RIB (you might want to implement a more sophisticated RIB generation)
            String rib = "MA" + System.currentTimeMillis() + loggedInClient.getId();
            
            // Create a checking account by default
            Account newAccount = accountService.createAccount(rib, "CHECKING", loggedInClient.getId());
            
            if (newAccount != null) {
                JOptionPane.showMessageDialog(dashboardView,
                    "Account created successfully!\nYour account number (RIB) is: " + rib,
                    "Account Created",
                    JOptionPane.INFORMATION_MESSAGE);
                    
                // Refresh the dashboard to show the new account
                showDashboard(loggedInClient.getEmail());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardView,
                "Failed to create account: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleTransferClick() {
        if (loggedInClient != null) {
            TransferController transferController = new TransferController(loggedInClient.getId());
            // Add a listener to refresh the dashboard after successful transfer
            transferController.addTransferCompleteListener(() -> {
                // Refresh the dashboard with updated account information
                showDashboard(loggedInClient.getEmail());
            });
            transferController.showView();
        }
    }

    private void handleHistoryClick() {
        if (loggedInClient != null) {
            TransactionHistoryController historyController = new TransactionHistoryController(loggedInClient.getId());
            historyController.showView();
        }
    }

    private void handleManageBeneficiaries() {
        Account account = accountService.getFirstAccountByClientId(loggedInClient.getId());
        if (account != null) {
            BeneficiaryView beneficiaryView = new BeneficiaryView();
            new BeneficiaryController(beneficiaryView, account.getId());
            beneficiaryView.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(dashboardView, "You need an account to manage beneficiaries.");
        }
    }

    private void handleSavingAccount() {
        if (loggedInClient != null) {
            SavingAccountController savingController = new SavingAccountController(loggedInClient.getId());
            savingController.showView();
        }
    }

    private void handleLogout() {
        // Clear the logged in client
        loggedInClient = null;
        
        // Close the dashboard
        dashboardView.dispose();
        
        // Clear and show login view
        loginView.clearFields();
        loginView.setVisible(true);
    }
} 