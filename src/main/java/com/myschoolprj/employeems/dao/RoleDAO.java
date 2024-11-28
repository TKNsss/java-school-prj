package com.myschoolprj.employeems.dao;

import com.myschoolprj.employeems.model.Role;
import com.myschoolprj.employeems.utils.connectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleDAO {

    private final Connection connection;

    public RoleDAO() throws SQLException {
        this.connection = connectDB.getConnection();
    }

    public Role getSingleRoleData(String emID) throws SQLException {
        String query = "SELECT ro.role_id AS roleID, em_id, role_name, al_level "
                     + "FROM Roles AS ro "
                     + "LEFT JOIN Allowances AS al ON al.role_id = ro.role_id "
                     + "WHERE em_id = ?";
        Role role = null; 

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, emID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Create and populate the Role object
                    role = new Role(
                            rs.getInt("roleID"),
                            rs.getString("em_id"),
                            rs.getString("role_name"),
                            rs.getFloat("al_level")
                    );
                }
            }
        }
        return role; 
    }
}
