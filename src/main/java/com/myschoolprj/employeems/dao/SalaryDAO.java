package com.myschoolprj.employeems.dao;

import com.myschoolprj.employeems.model.Employee;
import com.myschoolprj.employeems.utils.connectDB;
import com.myschoolprj.employeems.model.Salary;
<<<<<<< HEAD
=======
import com.myschoolprj.employeems.model.Role;
>>>>>>> origin/feature2
import com.myschoolprj.employeems.utils.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
<<<<<<< HEAD
import java.util.ArrayList;
=======
>>>>>>> origin/feature2
import javax.swing.JOptionPane;

public class SalaryDAO {

    private final Connection connection;

    public SalaryDAO() throws SQLException {
        this.connection = connectDB.getConnection();
    }
<<<<<<< HEAD

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

    public ArrayList<Salary> getMonSalData() throws SQLException {
        ArrayList<Salary> salaries = new ArrayList<>();
        String query = "SELECT sal.sal_id, sal.em_id, em.firstname, em.lastname, "
                + "sal.work_day, sal.month, sal.year, sal.base_salary, sal.net_salary, sal.month_salary "
                + "FROM Salaries AS sal "
                + "JOIN Employees AS em ON sal.em_id = em.em_id";

        try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Lấy dữ liệu từ ResultSet
                int salId = rs.getInt("sal_id");
                String emId = rs.getString("em_id").trim(); // Loại bỏ khoảng trắng nếu có
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                int workDay = rs.getInt("work_day");
                int month = rs.getInt("month");
                int year = rs.getInt("year");
                float baseSalary = rs.getFloat("base_salary");
                float netSalary = rs.getFloat("net_salary");
                float monthSalary = rs.getFloat("month_salary");

                // Tạo đối tượng Salary
                Salary salary = new Salary(salId, emId, firstName, lastName, workDay, month, year, baseSalary, netSalary, monthSalary);

                // Thêm vào danh sách
                salaries.add(salary);
            }
        }
        return salaries;
=======
    
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
>>>>>>> origin/feature2
    }

}
