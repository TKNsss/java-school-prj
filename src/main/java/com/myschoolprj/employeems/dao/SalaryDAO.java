package com.myschoolprj.employeems.dao;

import com.myschoolprj.employeems.utils.connectDB;
import com.myschoolprj.employeems.model.Salary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SalaryDAO {
    private final Connection connection;
    
    public SalaryDAO() throws SQLException {
        this.connection = connectDB.getConnection();
    }
    
    public void updateSalary(Salary sal) throws Exception {
        String query1 = "INSERT INTO Allowances(al_level, role_id) VALUES (?, ?)";
        String query2 = "INSERT INTO Salaries(em_id, base_salary, net_salary) VALUES (?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(query1)) {
            
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error writing Salaries: " + e);
        }
    }
}
