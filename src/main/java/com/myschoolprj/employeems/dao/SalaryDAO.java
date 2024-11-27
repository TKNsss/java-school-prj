package com.myschoolprj.employeems.dao;

import com.myschoolprj.employeems.utils.connectDB;
import com.myschoolprj.employeems.model.Salary;
import com.myschoolprj.employeems.model.Role;
import com.myschoolprj.employeems.utils.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class SalaryDAO {

    private final Connection connection;

    public SalaryDAO() throws SQLException {
        this.connection = connectDB.getConnection();
    }
    
    public void updateSalary(Salary sal, Role role) {
        if (!isEmIDExists(sal.getEmId())) {
            insertSalary(sal, role);
            return;
        }
        String query1 = "UPDATE Allowances SET al_level = ? WHERE role_id = ?";
        String query2 = "UPDATE Salaries SET base_salary = ?, net_salary = ? WHERE em_id = ?";
        
        try {
            connection.setAutoCommit(false);
            
            try (PreparedStatement ps1 = connection.prepareStatement(query1);
                 PreparedStatement ps2 = connection.prepareStatement(query2)) {
                
                // update Allowances
                ps1.setDouble(1, role.getAllowance());
                ps1.setInt(2, role.getRoleId());
                ps1.executeUpdate();
                
                // update Salaries
                ps2.setFloat(1, sal.getBaseSalary());
                ps2.setFloat(2, sal.getNetSalary());
                ps2.setString(3, sal.getEmId());
                ps2.executeUpdate();
            }
            connection.commit();
            JOptionPane.showMessageDialog(null, "Salary updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            try {
                connection.rollback();
                JOptionPane.showMessageDialog(null, "Error updating Salary:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException rollbackEx) {
                JOptionPane.showMessageDialog(null, "Error rolling back transaction:\n" + rollbackEx.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
            Validator.printSQLExceptionMessage(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                JOptionPane.showMessageDialog(null, "Error restoring auto-commit mode:\n" + autoCommitEx.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void insertSalary(Salary sal, Role role) {
        String query1 = "INSERT INTO Allowances(al_level, role_id) VALUES (?, ?)";
        String query2 = "INSERT INTO Salaries(em_id, base_salary, net_salary) VALUES (?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement ps1 = connection.prepareStatement(query1); PreparedStatement ps2 = connection.prepareStatement(query2)) {
                // insert into Allowances
                ps1.setDouble(1, role.getAllowance());
                ps1.setInt(2, role.getRoleId());
                ps1.executeUpdate();

                // insert into Salaries
                ps2.setString(1, sal.getEmId());
                ps2.setFloat(2, sal.getBaseSalary());
                ps2.setFloat(3, sal.getNetSalary());
                ps2.executeUpdate();
            }
            connection.commit();
            JOptionPane.showMessageDialog(null, "Salary inserted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            try {
                connection.rollback();
                JOptionPane.showMessageDialog(null, "Error inserting Salary:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException rollbackEx) {
                JOptionPane.showMessageDialog(null, "Error rolling back transaction:\n" + rollbackEx.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
            Validator.printSQLExceptionMessage(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                JOptionPane.showMessageDialog(null, "Error restoring auto-commit mode:\n" + autoCommitEx.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean isEmIDExists(String emID) {
        String query = "SELECT COUNT(*) FROM Salaries WHERE em_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, emID); 

            try (ResultSet rs = ps.executeQuery()) { 
                if (rs.next()) { 
                    return rs.getInt(1) > 0;     
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching employee's data:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            Validator.printSQLExceptionMessage(e);     
        }
        return false; 
    }

}
