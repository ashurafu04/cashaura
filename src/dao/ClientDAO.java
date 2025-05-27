package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Client;
import util.DBConnection;

public class ClientDAO {
    private Connection conn;

    public ClientDAO() {
        this.conn = DBConnection.getConnection();
        if (this.conn == null) {
            throw new RuntimeException("Failed to establish database connection.");
        }
    }

    public void addClient(Client client) throws SQLException {
        String sql = "INSERT INTO client (firstname, lastname, cin, birthdate, email, phone_number, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, client.getFirstname());
            stmt.setString(2, client.getLastname());
            stmt.setString(3, client.getCin());
            stmt.setDate(4, client.getBirthdate());
            stmt.setString(5, client.getEmail());
            stmt.setString(6, client.getPhoneNumber());
            stmt.setString(7, client.getPassword());
            stmt.executeUpdate();
        }
    }

    public Client getClientById(int id) throws SQLException {
        String sql = "SELECT * FROM client WHERE id_client = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Client(
                    rs.getInt("id_client"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("cin"),
                    rs.getDate("birthdate"),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    rs.getString("password")
                );
            }
        }
        return null;
    }

    public List<Client> getAllClients() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                clients.add(new Client(
                    rs.getInt("id_client"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("cin"),
                    rs.getDate("birthdate"),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    rs.getString("password")
                ));
            }
        }
        return clients;
    }

    public void updateClient(Client client) throws SQLException {
        String sql = "UPDATE client SET firstname = ?, lastname = ?, cin = ?, birthdate = ?, email = ?, phone_number = ?, password = ? WHERE id_client = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, client.getFirstname());
            stmt.setString(2, client.getLastname());
            stmt.setString(3, client.getCin());
            stmt.setDate(4, client.getBirthdate());
            stmt.setString(5, client.getEmail());
            stmt.setString(6, client.getPhoneNumber());
            stmt.setString(7, client.getPassword());
            stmt.setInt(8, client.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteClient(int id) throws SQLException {
        String sql = "DELETE FROM client WHERE id_client = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public boolean checkCredentials(String email, String password) {
        String sql = "SELECT COUNT(*) FROM client WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Client getClientByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM client WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Client(
                    rs.getInt("id_client"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("cin"),
                    rs.getDate("birthdate"),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    rs.getString("password")
                );
            }
        }
        return null;
    }
} 

