package service;

import dao.TransferDAO;
import dao.AccountDAO;
import model.Transfer;
import model.Account;
import java.sql.SQLException;
import java.math.BigDecimal;

public class TransferService {
    private TransferDAO transferDAO;
    private AccountDAO accountDAO;

    public TransferService() {
        this.transferDAO = new TransferDAO();
        this.accountDAO = new AccountDAO();
    }

    public void executeTransfer(Transfer transfer) throws SQLException {
        // Start transaction
        try {
            // 1. Get source and target accounts
            Account sourceAccount = accountDAO.findById(transfer.getSourceAccountId())
                .orElseThrow(() -> new SQLException("Source account not found"));
            Account targetAccount = accountDAO.findById(transfer.getTargetAccountId())
                .orElseThrow(() -> new SQLException("Target account not found"));

            // 2. Validate source account has sufficient funds
            if (sourceAccount.getBalance().compareTo(transfer.getAmount()) < 0) {
                throw new SQLException("Insufficient funds");
            }

            // 3. Update balances
            BigDecimal newSourceBalance = sourceAccount.getBalance().subtract(transfer.getAmount());
            BigDecimal newTargetBalance = targetAccount.getBalance().add(transfer.getAmount());

            // 4. Save the transfer record
            transferDAO.create(transfer);

            // 5. Update account balances
            accountDAO.updateBalance(sourceAccount.getId(), newSourceBalance);
            accountDAO.updateBalance(targetAccount.getId(), newTargetBalance);

        } catch (SQLException e) {
            // If anything goes wrong, throw the exception to rollback
            throw new SQLException("Transfer failed: " + e.getMessage(), e);
        }
    }

    public Transfer getTransferById(int id) throws SQLException {
        return transferDAO.findById(id)
            .orElseThrow(() -> new SQLException("Transfer not found"));
    }
} 