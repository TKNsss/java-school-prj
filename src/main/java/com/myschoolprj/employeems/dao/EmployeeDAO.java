package com.myschoolprj.employeems.dao;

import com.myschoolprj.employeems.model.Employee;
import com.myschoolprj.employeems.model.Role;
import com.myschoolprj.employeems.utils.Validator;
import com.myschoolprj.employeems.utils.connectDB;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class EmployeeDAO {

    private final Connection connection;

    public EmployeeDAO() throws SQLException {
        this.connection = connectDB.getConnection();
    }

    public ArrayList<Employee> getEmployeeData() {
        ArrayList<Employee> employees = new ArrayList<>();
        String query = "SELECT DISTINCT em.em_id AS eID, firstname, lastname, phone, gender, dob, address, base_salary, net_salary, title, sal_col_level, role_name, al_level "
                + "FROM Employees AS em "
                + "LEFT JOIN Positions AS pos ON em.pos_id = pos.pos_id "
                + "LEFT JOIN Salaries AS sal ON sal.em_id = em.em_id "
                + "LEFT JOIN Roles AS ro ON em.em_id = ro.em_id "
                + "LEFT JOIN Allowances AS al ON ro.role_id = al.role_id";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee em = new Employee();

                    em.setID(rs.getString("eID"));
                    em.setFirstName(rs.getString("firstname"));
                    em.setLastName(rs.getString("lastname"));
                    em.setPhone(rs.getString("phone"));
                    em.setGender(rs.getString("gender"));
                    em.setDob(rs.getDate("dob"));
                    em.setAddress(rs.getString("address"));
                    em.setRole(rs.getString("role_name"));
                    em.setPosition(rs.getString("title"));
                    em.setBaseSalary(rs.getInt("base_salary"));
                    em.setNetSalary(rs.getInt("net_salary"));
                    em.setAllowanceLevel(rs.getFloat("al_level"));
                    em.setCoefLevel(rs.getFloat("sal_col_level"));

                    employees.add(em);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching employee's data:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            Validator.printSQLExceptionMessage(e);
        }
        return employees;
    }

    public void addEmployeeData(Employee em, Role roleObj) {
        if (isPhoneExists(em.getPhone(), null) || isIDExists(em.getID())) {
            JOptionPane.showMessageDialog(null, "Phone number/ID has existed, please choose another one!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String query1 = "INSERT INTO Employees(em_id, firstname, lastname, phone, gender, dob, address, pos_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String query2 = "INSERT INTO Roles(em_id, role_name) VALUES(?, ?)";

        try {
            // Disable auto-commit to start a transaction
            connection.setAutoCommit(false);

            try (PreparedStatement ps1 = connection.prepareStatement(query1); 
                 PreparedStatement ps2 = connection.prepareStatement(query2)) {
                // Insert into Employees
                ps1.setString(1, em.getID());
                ps1.setString(2, em.getFirstName());
                ps1.setString(3, em.getLastName());
                ps1.setString(4, em.getPhone());
                ps1.setString(5, em.getGender());
                ps1.setDate(6, new java.sql.Date(em.getDob().getTime()));
                ps1.setString(7, em.getAddress());
                ps1.setInt(8, em.getPositionID());
                ps1.executeUpdate();

                // Insert into Roles
                ps2.setString(1, roleObj.getEmID()); 
                ps2.setString(2, roleObj.getRoleName()); 
                ps2.executeUpdate();
            }
            // Commit the transaction
            connection.commit();
            JOptionPane.showMessageDialog(null, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            try {
                // Roll back the transaction in case of error
                connection.rollback();
                JOptionPane.showMessageDialog(null, "Error adding Employees:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException rollbackEx) {
                JOptionPane.showMessageDialog(null, "Error rolling back transaction:\n" + rollbackEx.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
            Validator.printSQLExceptionMessage(e);
        } finally {
            try {
                // Restore auto-commit mode
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                JOptionPane.showMessageDialog(null, "Error restoring auto-commit mode:\n" + autoCommitEx.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void updateEmployeeData(Employee em, Role roleObj) {
        if (isPhoneExists(em.getPhone(), em.getID())) {
            JOptionPane.showMessageDialog(null, "Phone number has existed, please choose another one!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String query1 = "UPDATE Employees SET firstname = ?, lastname = ?, phone = ?, gender = ?, dob = ?, address = ?, pos_id = ? WHERE em_id = ?";
        String query2 = "UPDATE Roles SET role_name = ? WHERE em_id = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement ps1 = connection.prepareStatement(query1); PreparedStatement ps2 = connection.prepareStatement(query2)) {

                ps1.setString(1, em.getFirstName());
                ps1.setString(2, em.getLastName());
                ps1.setString(3, em.getPhone());
                ps1.setString(4, em.getGender());

                // Convert DOB to java.sql.Date
                if (em.getDob() != null) {
                    ps1.setDate(5, new java.sql.Date(em.getDob().getTime()));
                } else {
                    JOptionPane.showMessageDialog(null, "Date of birth cannot be null.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    connection.rollback(); // Roll back transaction due to invalid input
                    return; // Abort the update
                }
                ps1.setString(6, em.getAddress());
                ps1.setInt(7, em.getPositionID());
                ps1.setString(8, em.getID()); // WHERE condition
                ps1.executeUpdate();

                // Update Roles table
                ps2.setString(1, roleObj.getRoleName());
                ps2.setString(2, roleObj.getEmID()); // WHERE condition
                ps2.executeUpdate();
            }
            connection.commit();
            JOptionPane.showMessageDialog(null, "Employee updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            try {
                connection.rollback();
                JOptionPane.showMessageDialog(null, "Error updating Employees:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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

    public void deleteEmployee(String emID, int roleID) throws Exception {
        // Since the Roles table likely has a foreign key relationship with the Employees table, we must 
        // delete from Roles first to avoid foreign key constraint violations.
        String query1 = "DELETE FROM Allowances WHERE role_id = ?";
        String query2 = "DELETE FROM Roles WHERE em_id = ?";
        String query3 = "DELETE FROM Salaries WHERE em_id = ?";
        String query4 = "DELETE FROM Employees WHERE em_id = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement ps1 = connection.prepareStatement(query1); 
                 PreparedStatement ps2 = connection.prepareStatement(query2); 
                 PreparedStatement ps3 = connection.prepareStatement(query3); 
                 PreparedStatement ps4 = connection.prepareStatement(query4)) {

                // Delete from Allowances
                ps1.setInt(1, roleID);
                int affectedRows1 = ps1.executeUpdate();
                if (affectedRows1 == 0) {
                    throw new SQLException("No allowances found for role_id: " + roleID);
                }

                // Delete from Roles
                ps2.setString(1, emID);
                int affectedRows2 = ps2.executeUpdate();
                if (affectedRows2 == 0) {
                    throw new SQLException("No role found for employee ID: " + emID);
                }

                // Delete from Salaries
                ps3.setString(1, emID);
                int affectedRows3 = ps3.executeUpdate();
                if (affectedRows3 == 0) {
                    throw new SQLException("No salary found for employee ID: " + emID);
                }

                // Delete from Employees
                ps4.setString(1, emID);
                int affectedRows4 = ps4.executeUpdate();
                if (affectedRows4 == 0) {
                    throw new SQLException("No employee found with ID: " + emID);
                }
            }
            connection.commit();
            JOptionPane.showMessageDialog(null, "Employee deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            try {
                connection.rollback();
                JOptionPane.showMessageDialog(null, "Error deleting employee:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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

    public int getPositionIDByName(String position) throws SQLException {
        String query = "SELECT pos_id FROM Positions WHERE title = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, position);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("pos_id");
                }
            }
        }
        return -1;
    }

    public boolean isPhoneExists(String phone, String emID) {
        String query = "SELECT COUNT(*) AS count "
                + "FROM Employees "
                + "WHERE phone = ?";

        // If updating, exclude the current employee ID
        if (emID != null) {
            query += " AND em_id != ?";
        }

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, phone);

            if (emID != null) {
                ps.setString(2, emID); // Set employeeID parameter
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0; // Return true if phone exists
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error checking phone number existence:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            Validator.printSQLExceptionMessage(e);
        }
        return false;
    }

    public boolean isIDExists(String ID) {
        String query = "SELECT COUNT(*) AS count "
                + "FROM Employees "
                + "WHERE em_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, ID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error checking ID existence:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            Validator.printSQLExceptionMessage(e);
        }
        return false;
    }
}
