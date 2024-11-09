package com.myschoolprj.employeems.dao;

import com.myschoolprj.employeems.utils.ConnectDB;
import com.myschoolprj.employeems.model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    private final Connection connection;

    public AdminDAO() throws SQLException {
        // Get the shared connection from ConnectDB
        this.connection = ConnectDB.getConnection();
    }

    // Check if admin exists with the given username and password
    public Admin getAdminByCredentials(String username, String password) throws SQLException {
        String query = "SELECT * FROM Admins WHERE ad_name = ? AND password = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // If the credentials are correct, return the Admin object
                    return new Admin(
                            rs.getString("ad_id"),
                            rs.getString("ad_name"),
                            rs.getString("password")
                    );
                }
            }
        }
        return null;
    }

    public boolean existedUsername(String username) throws SQLException {
        String query = "SELECT * FROM Admins WHERE ad_name = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                // If username exists, return true, otherwise false
                return rs.next();  // Returns true if a record is found
            }
        }
    }

    public void createAdminCredentials(String username, String password) {
        String query = "INSERT INTO Admins(ad_name, password) VALUES(?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Print specific details about the SQL exception
            System.err.println("SQL Exception occurred:");
            System.err.println("Message: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
    }
}
