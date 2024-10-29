package com.myschoolprj.employeems.dao;

import com.myschoolprj.employeems.utils.connectDB;
import com.myschoolprj.employeems.model.Admin;

import java.sql.*;

public class AdminDAO implements AutoCloseable {
    private Connection connection;
    
    public AdminDAO() {
        connection = connectDB.getConnection();
    }
    
    // check if admin exists with the given username and password
    public Admin getAdminByCredentials(String userName, String password) throws SQLException {
        String query = "SELECT * FROM Admins WHERE ad_name = ? AND password = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, userName);
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
    
    @Override
    public void close() {
        connectDB.closeConnection(this.connection);
    }
}