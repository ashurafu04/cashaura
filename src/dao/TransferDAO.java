package dao;

import model.Transfer;
import util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransferDAO {
    
    public void create(Transfer transfer) throws SQLException {
        String sql = "INSERT INTO ACC_TRANSACTIONS (type, amount, description, transaction_date, status, id_account, id_recipient) " +
                    "VALUES ('TRANSFER', ?, ?, ?, 'COMPLETED', ?, ?)";
        Connection conn = DBConnection.getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setBigDecimal(1, transfer.getAmount());
            stmt.setString(2, transfer.getDescription());
            stmt.setTimestamp(3, transfer.getTimestamp());
            stmt.setInt(4, transfer.getSourceAccountId());
            stmt.setInt(5, transfer.getTargetAccountId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating transfer failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transfer.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating transfer failed, no ID obtained.");
                }
            }
        }
    }

    public Optional<Transfer> findById(int id) throws SQLException {
        String sql = "SELECT * FROM ACC_TRANSACTIONS WHERE id_transaction = ? AND type = 'TRANSFER'";
        Connection conn = DBConnection.getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToTransfer(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Transfer> findByAccountId(int accountId) throws SQLException {
        String sql = "SELECT * FROM ACC_TRANSACTIONS WHERE (id_account = ? OR id_recipient = ?) " +
                    "AND type = 'TRANSFER' ORDER BY transaction_date DESC";
        List<Transfer> transfers = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            stmt.setInt(2, accountId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transfers.add(mapResultSetToTransfer(rs));
                }
            }
        }
        return transfers;
    }

    private Transfer mapResultSetToTransfer(ResultSet rs) throws SQLException {
        return new Transfer(
            rs.getInt("id_transaction"),
            rs.getBigDecimal("amount"),
            rs.getString("description"),
            rs.getTimestamp("transaction_date"),
            rs.getInt("id_account"),
            rs.getInt("id_recipient")
        );
    }
} 