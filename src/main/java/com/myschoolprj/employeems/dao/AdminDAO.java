package com.myschoolprj.employeems.dao;

import com.myschoolprj.employeems.utils.connectDB;
import com.myschoolprj.employeems.model.Admin;
import com.myschoolprj.employeems.utils.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    private final Connection connection;

    public AdminDAO() throws SQLException {
        // Get the shared connection from ConnectDB
        this.connection = connectDB.getConnection();
    }

    // Check if admin exists with the given username and password
    public Admin getAdminByCredentials(String username) throws SQLException {
        String query = "SELECT * FROM Admins WHERE username = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // If the credentials are correct, return the Admin object
                    return new Admin(
                            rs.getInt("ad_id"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                }
            }
        }
        return null;
    }

    public boolean isUsernameExist(String username) throws SQLException {
        String query = "SELECT * FROM Admins WHERE username = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                // If username exists, return true, otherwise false
                return rs.next();  // Returns true if a record is found
            }
        }
    }

    public void createAdminCredentials(String username, String password) {
        String query = "INSERT INTO Admins(username, password) VALUES(?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
        } catch (SQLException e) {
            Validator.printSQLExceptionMessage(e);
        }
    }
}
