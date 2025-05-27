package dao;

import model.Beneficiaries;
import util.DBConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BeneficiaryDAO {
    
    public void create(int idAccount, int idBeneficiary) throws SQLException {
        String sql = "INSERT INTO BENEFICIARIES (id_account, id_beneficiary) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAccount);
            stmt.setInt(2, idBeneficiary);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Adding beneficiary failed, no rows affected.");
            }
        }
    }

    public List<Beneficiaries> findByAccountId(int idAccount) throws SQLException {
        List<Beneficiaries> beneficiaries = new ArrayList<>();
        String sql = """
            SELECT b.*, a.rib, c.firstname, c.lastname 
            FROM BENEFICIARIES b
            JOIN ACCOUNT a ON b.id_beneficiary = a.id_account
            JOIN CLIENT c ON a.id_client = c.id_client
            WHERE b.id_account = ?
        """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAccount);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Beneficiaries beneficiary = new Beneficiaries(
                        rs.getInt("id_account"),
                        rs.getInt("id_beneficiary"),
                        rs.getTimestamp("added_at").toLocalDateTime()
                    );
                    beneficiaries.add(beneficiary);
                }
            }
        }
        return beneficiaries;
    }

    public void delete(int idAccount, int idBeneficiary) throws SQLException {
        String sql = "DELETE FROM BENEFICIARIES WHERE id_account = ? AND id_beneficiary = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAccount);
            stmt.setInt(2, idBeneficiary);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting beneficiary failed, no rows affected.");
            }
        }
    }

    public boolean exists(int idAccount, int idBeneficiary) throws SQLException {
        String sql = "SELECT COUNT(*) FROM BENEFICIARIES WHERE id_account = ? AND id_beneficiary = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAccount);
            stmt.setInt(2, idBeneficiary);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
} 