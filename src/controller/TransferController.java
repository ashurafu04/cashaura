package controller;

import model.Account;
import model.Transfer;
import model.Beneficiaries;
import service.AccountService;
import service.TransferService;
import service.BeneficiaryService;
import view.TransferView;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class TransferController {
    private TransferView view;
    private AccountService accountService;
    private TransferService transferService;
    private BeneficiaryService beneficiaryService;
    private int clientId;
    private Account currentAccount;
    private Runnable transferCompleteListener;

    public TransferController(int clientId) {
        this.clientId = clientId;
        this.accountService = new AccountService();
        this.transferService = new TransferService();
        this.beneficiaryService = new BeneficiaryService();
        this.view = new TransferView();
        initializeView();
        initializeListeners();
    }

    public void addTransferCompleteListener(Runnable listener) {
        this.transferCompleteListener = listener;
    }

    private void initializeView() {
        try {
            // Load client's accounts
            List<Account> accounts = accountService.getAccountsByClientId(clientId);
            if (accounts.isEmpty()) {
                view.showError("No accounts found for this client.");
                return;
            }
            
            // Store the current account (first account)
            currentAccount = accounts.get(0);
            
            String[] accountStrings = accounts.stream()
                .map(account -> String.format("%s - %s", account.getRib(), account.getBalance()))
                .toArray(String[]::new);
            view.setAccounts(accountStrings);

            // Load only added beneficiaries
            List<Beneficiaries> beneficiaries = beneficiaryService.getAccountBeneficiaries(currentAccount.getId());
            List<String> beneficiaryStrings = new ArrayList<>();
            
            for (Beneficiaries beneficiary : beneficiaries) {
                Optional<Account> beneficiaryAccount = accountService.getAccount(beneficiary.getIdBeneficiary());
                if (beneficiaryAccount.isPresent()) {
                    beneficiaryStrings.add(String.format("%s - %s", beneficiaryAccount.get().getRib(), "****"));
                }
            }
            
            view.setBeneficiaries(beneficiaryStrings.toArray(new String[0]));
            
            if (beneficiaryStrings.isEmpty()) {
                view.showError("No beneficiaries found. Please add beneficiaries first.");
                view.setConfirmEnabled(false);
            }

        } catch (SQLException e) {
            view.showError("Failed to load accounts: " + e.getMessage());
        }
    }

    private void initializeListeners() {
        view.addConfirmListener(e -> handleTransfer());
        view.addCancelListener(e -> view.dispose());
    }

    private void handleTransfer() {
        try {
            // Get selected accounts
            String sourceRib = view.getSelectedAccount().split(" - ")[0];
            String targetRib = view.getSelectedBeneficiary().split(" - ")[0];
            
            // Validate amount
            String amountStr = view.getAmount().trim();
            if (amountStr.isEmpty()) {
                view.showError("Please enter an amount.");
                return;
            }
            
            // Validate amount format
            if (!amountStr.matches("^\\d+(\\.\\d{0,2})?$")) {
                view.showError("Invalid amount format. Please enter a valid number with up to 2 decimal places.");
                return;
            }
            
            BigDecimal amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                view.showError("Amount must be greater than zero.");
                return;
            }
            
            // Get source account
            Account sourceAccount = accountService.getAccountByRib(sourceRib);
            if (sourceAccount == null) {
                view.showError("Source account not found.");
                return;
            }
            
            // Validate balance
            if (sourceAccount.getBalance().compareTo(amount) < 0) {
                view.showError("Insufficient funds. Available balance: " + sourceAccount.getBalance().toPlainString());
                return;
            }
            
            // Get target account
            Account targetAccount = accountService.getAccountByRib(targetRib);
            if (targetAccount == null) {
                view.showError("Target account not found.");
                return;
            }
            
            // Validate that target account is still a beneficiary
            if (!beneficiaryService.validateBeneficiary(sourceAccount.getId(), targetAccount.getId())) {
                view.showError("Selected account is no longer in your beneficiaries list.");
                return;
            }
            
            // Validate description
            String description = view.getDescription().trim();
            if (description.isEmpty() || description.equals("Enter transfer description here...")) {
                view.showError("Please enter a description for the transfer.");
                return;
            }
            
            // Create and execute transfer
            Transfer transfer = new Transfer(
                0, // ID will be set by database
                amount,
                description,
                new Timestamp(System.currentTimeMillis()),
                sourceAccount.getId(),
                targetAccount.getId()
            );
            
            transferService.executeTransfer(transfer);
            
            view.showSuccess(String.format("Transfer of %.2f to %s completed successfully!", 
                amount.doubleValue(), targetRib));
            
            // Notify listener of successful transfer
            if (transferCompleteListener != null) {
                transferCompleteListener.run();
            }
            
            view.dispose();
            
        } catch (SQLException e) {
            view.showError("Transfer failed: " + e.getMessage());
        } catch (NumberFormatException e) {
            view.showError("Invalid amount format.");
        } catch (Exception e) {
            view.showError("An unexpected error occurred: " + e.getMessage());
        }
    }

    public void showView() {
        view.setVisible(true);
    }
} 