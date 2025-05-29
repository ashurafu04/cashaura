package dao.impl;

import dao.SavingAccountDAO;
import model.SavingAccount;
import model.Account;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class SavingAccountDAOImpl implements SavingAccountDAO {
    
    @Override
    public SavingAccount create(SavingAccount savingAccount) throws SQLException {
        String sql = "INSERT INTO SAVING_ACCOUNT (id_saving_account, id_account, interest_rate, last_interest_calc_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, savingAccount.getIdSavingAccount());
            stmt.setInt(2, savingAccount.getIdAccount());
            stmt.setBigDecimal(3, savingAccount.getInterestRate());
            stmt.setDate(4, new java.sql.Date(savingAccount.getLastInterestCalcDate().getTime()));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating saving account failed, no rows affected.");
            }
            
            return savingAccount;
        }
    }

    @Override
    public SavingAccount findById(int idSavingAccount) throws SQLException {
        String sql = "SELECT sa.*, a.* FROM SAVING_ACCOUNT sa " +
                    "JOIN ACCOUNT a ON sa.id_account = a.id_account " +
                    "WHERE sa.id_saving_account = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idSavingAccount);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    SavingAccount savingAccount = new SavingAccount();
                    savingAccount.setIdSavingAccount(rs.getInt("id_saving_account"));
                    savingAccount.setIdAccount(rs.getInt("id_account"));
                    savingAccount.setInterestRate(rs.getBigDecimal("interest_rate"));
                    savingAccount.setLastInterestCalcDate(rs.getDate("last_interest_calc_date"));
                    
                    // Create and set the base account
                    Account baseAccount = new Account();
                    baseAccount.setId(rs.getInt("id_account"));
                    baseAccount.setClientId(rs.getInt("id_client"));
                    baseAccount.setBalance(rs.getBigDecimal("balance"));
                    baseAccount.setType(rs.getString("type"));
                    baseAccount.setRib(rs.getString("rib"));
                    baseAccount.setDeleted(rs.getBoolean("is_deleted"));
                    
                    // Handle deleted_at timestamp
                    java.sql.Timestamp deletedAt = rs.getTimestamp("deleted_at");
                    if (deletedAt != null) {
                        baseAccount.setDeletedAt(deletedAt);
                    }
                    
                    // Set the base account in the saving account
                    savingAccount.setBaseAccount(baseAccount);
                    
                    return savingAccount;
                }
            }
        }
        return null;
    }

    @Override
    public List<SavingAccount> findByClientId(int idClient) throws SQLException {
        List<SavingAccount> savingAccounts = new ArrayList<>();
        String sql = "SELECT sa.*, a.* FROM SAVING_ACCOUNT sa " +
                    "JOIN ACCOUNT a ON sa.id_account = a.id_account " +
                    "WHERE a.id_client = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idClient);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SavingAccount savingAccount = new SavingAccount();
                    savingAccount.setIdSavingAccount(rs.getInt("id_saving_account"));
                    savingAccount.setIdAccount(rs.getInt("id_account"));
                    savingAccount.setInterestRate(rs.getBigDecimal("interest_rate"));
                    savingAccount.setLastInterestCalcDate(rs.getDate("last_interest_calc_date"));
                    
                    // Create and set the base account
                    Account baseAccount = new Account();
                    baseAccount.setId(rs.getInt("id_account"));
                    baseAccount.setClientId(rs.getInt("id_client"));
                    baseAccount.setBalance(rs.getBigDecimal("balance"));
                    baseAccount.setType(rs.getString("type"));
                    baseAccount.setRib(rs.getString("rib"));
                    baseAccount.setDeleted(rs.getBoolean("is_deleted"));
                    
                    // Handle deleted_at timestamp
                    java.sql.Timestamp deletedAt = rs.getTimestamp("deleted_at");
                    if (deletedAt != null) {
                        baseAccount.setDeletedAt(deletedAt);
                    }
                    
                    // Set the base account in the saving account
                    savingAccount.setBaseAccount(baseAccount);
                    
                    savingAccounts.add(savingAccount);
                }
            }
        }
        return savingAccounts;
    }

    @Override
    public void updateInterestCalculationDate(int idSavingAccount) throws SQLException {
        String sql = "UPDATE SAVING_ACCOUNT SET last_interest_calc_date = ? WHERE id_saving_account = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
            stmt.setInt(2, idSavingAccount);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateInterestRate(int idSavingAccount, BigDecimal newRate) throws SQLException {
        String sql = "UPDATE SAVING_ACCOUNT SET interest_rate = ? WHERE id_saving_account = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, newRate);
            stmt.setInt(2, idSavingAccount);
            
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Failed to update interest rate. Saving account not found.");
            }
        }
    }

    @Override
    public boolean delete(int idSavingAccount) throws SQLException {
        String sql = "DELETE FROM SAVING_ACCOUNT WHERE id_saving_account = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idSavingAccount);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public void update(SavingAccount savingAccount) throws SQLException {
        String sql = "UPDATE SAVING_ACCOUNT SET interest_rate = ?, last_interest_calc_date = ? WHERE id_saving_account = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, savingAccount.getInterestRate());
            stmt.setDate(2, new java.sql.Date(savingAccount.getLastInterestCalcDate().getTime()));
            stmt.setInt(3, savingAccount.getIdSavingAccount());
            
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Failed to update saving account. Account not found.");
            }
        }
    }

    private SavingAccount mapResultSetToSavingAccount(ResultSet rs) throws SQLException {
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setIdSavingAccount(rs.getInt("id_saving_account"));
        savingAccount.setIdAccount(rs.getInt("id_account"));
        savingAccount.setInterestRate(rs.getBigDecimal("interest_rate"));
        savingAccount.setLastInterestCalcDate(rs.getDate("last_interest_calc_date"));
        return savingAccount;
    }
} 