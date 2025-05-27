package controller;

import model.AccTransactions;
import model.Account;
import service.AccountService;
import service.TransactionService;
import view.TransactionHistoryView;

import java.sql.SQLException;
import java.util.List;

public class TransactionHistoryController {
    private TransactionHistoryView view;
    private TransactionService transactionService;
    private AccountService accountService;
    private int clientId;
    private Account currentAccount;

    public TransactionHistoryController(int clientId) {
        this.clientId = clientId;
        this.view = new TransactionHistoryView();
        this.transactionService = new TransactionService();
        this.accountService = new AccountService();
        initializeController();
    }

    private void initializeController() {
        // Get the client's account
        currentAccount = accountService.getFirstAccountByClientId(clientId);
        if (currentAccount == null) {
            view.showError("No account found for this client.");
            return;
        }

        // Load initial transactions
        loadTransactions("All Transactions");

        // Add listeners
        view.addBackListener(e -> view.dispose());
        view.addFilterListener(e -> loadTransactions(view.getSelectedFilter()));
    }

    private void loadTransactions(String filter) {
        try {
            view.clearTransactions();
            List<AccTransactions> transactions;

            // Apply filter
            switch (filter) {
                case "Transfers Only":
                    transactions = transactionService.getTransactionsByType(currentAccount.getId(), "TRANSFER");
                    break;
                case "Deposits Only":
                    transactions = transactionService.getTransactionsByType(currentAccount.getId(), "DEPOSIT");
                    break;
                case "Withdrawals Only":
                    transactions = transactionService.getTransactionsByType(currentAccount.getId(), "WITHDRAWAL");
                    break;
                default:
                    transactions = transactionService.getAllTransactions(currentAccount.getId());
                    break;
            }

            // Add transactions to view
            for (AccTransactions transaction : transactions) {
                double displayAmount = transaction.getAmount();
                // For outgoing transfers and withdrawals, show negative amount
                if (transaction.getType().equals("WITHDRAWAL") || 
                    (transaction.getType().equals("TRANSFER") && transaction.getAccountId() == currentAccount.getId())) {
                    displayAmount = -displayAmount;
                }

                view.addTransaction(
                    transaction.getTransactionDate(),
                    transaction.getType(),
                    transaction.toString(),
                    displayAmount,
                    "COMPLETED"  // Default status for now
                );
            }

        } catch (SQLException e) {
            view.showError("Error loading transactions: " + e.getMessage());
        }
    }

    public void showView() {
        view.setVisible(true);
    }
} 