package service;

import model.AccTransactions;
import dao.TransactionDAO;
import java.sql.SQLException;
import java.util.List;

public class TransactionService {
    private TransactionDAO transactionDAO;

    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
    }

    public List<AccTransactions> getAllTransactions(int accountId) throws SQLException {
        return transactionDAO.findByAccountId(accountId);
    }

    public List<AccTransactions> getTransactionsByType(int accountId, String type) throws SQLException {
        return transactionDAO.findByAccountIdAndType(accountId, type);
    }
} 