package dao;

import model.AccTransactions;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    
    public List<AccTransactions> findByAccountId(int accountId) throws SQLException {
        String sql = "SELECT * FROM ACC_TRANSACTIONS WHERE id_account = ? OR id_recipient = ? " +
                    "ORDER BY transaction_date DESC";
        List<AccTransactions> transactions = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, accountId);
            stmt.setInt(2, accountId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        }
        return transactions;
    }

    public List<AccTransactions> findByAccountIdAndType(int accountId, String type) throws SQLException {
        String sql = "SELECT * FROM ACC_TRANSACTIONS WHERE (id_account = ? OR id_recipient = ?) " +
                    "AND type = ? ORDER BY transaction_date DESC";
        List<AccTransactions> transactions = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, accountId);
            stmt.setInt(2, accountId);
            stmt.setString(3, type);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        }
        return transactions;
    }

    private AccTransactions mapResultSetToTransaction(ResultSet rs) throws SQLException {
        return new AccTransactions(
            rs.getInt("id_transaction"),
            rs.getString("type"),
            rs.getDouble("amount"),
            rs.getTimestamp("transaction_date"),
            rs.getInt("id_account"),
            rs.getInt("id_recipient")
        );
    }
} 