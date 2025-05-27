package dao;

import model.Account;
import util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDAO {
    
    public void create(Account account) throws SQLException {
        String sql = "INSERT INTO account (rib, balance, type, id_client) VALUES (?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, account.getRib());
            stmt.setBigDecimal(2, account.getBalance());
            stmt.setString(3, account.getType());
            stmt.setInt(4, account.getClientId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating account failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    account.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating account failed, no ID obtained.");
                }
            }
        }
    }

    public Optional<Account> findById(int id) throws SQLException {
        String sql = "SELECT * FROM account WHERE id_account = ? AND is_deleted = FALSE";
        Connection conn = DBConnection.getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAccount(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Account> findByClientId(int clientId) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account WHERE id_client = ? AND is_deleted = FALSE";
        Connection conn = DBConnection.getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToAccount(rs));
                }
            }
        }
        return accounts;
    }

    public void update(Account account) throws SQLException {
        String sql = "UPDATE account SET balance = ?, type = ? WHERE id_account = ? AND is_deleted = FALSE";
        Connection conn = DBConnection.getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, account.getBalance());
            stmt.setString(2, account.getType());
            stmt.setInt(3, account.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating account failed, no rows affected.");
            }
        }
    }

    public void softDelete(int id) throws SQLException {
        String sql = "UPDATE account SET is_deleted = TRUE, deleted_at = CURRENT_TIMESTAMP WHERE id_account = ?";
        Connection conn = DBConnection.getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting account failed, no rows affected.");
            }
        }
    }

    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        return new Account(
            rs.getInt("id_account"),
            rs.getString("rib"),
            rs.getBigDecimal("balance"),
            rs.getTimestamp("created_at"),
            rs.getBoolean("is_deleted"),
            rs.getTimestamp("deleted_at"),
            rs.getString("type"),
            rs.getInt("id_client")
        );
    }

    public boolean existsByRib(String rib) throws SQLException {
        String sql = "SELECT COUNT(*) FROM account WHERE rib = ? AND is_deleted = FALSE";
        Connection conn = DBConnection.getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rib);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public void updateBalance(int accountId, BigDecimal newBalance) throws SQLException {
        String sql = "UPDATE account SET balance = ? WHERE id_account = ? AND is_deleted = FALSE";
        Connection conn = DBConnection.getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, newBalance);
            stmt.setInt(2, accountId);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating account balance failed, no rows affected.");
            }
        }
    }

    public List<Account> findAll() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account WHERE is_deleted = FALSE";
        Connection conn = DBConnection.getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToAccount(rs));
                }
            }
        }
        return accounts;
    }

    public Optional<Account> findByRib(String rib) throws SQLException {
        String sql = "SELECT * FROM account WHERE rib = ? AND is_deleted = FALSE";
        Connection conn = DBConnection.getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rib);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAccount(rs));
                }
            }
        }
        return Optional.empty();
    }
} 