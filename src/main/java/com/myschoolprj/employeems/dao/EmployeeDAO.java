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
import javax.swing.JLabel;

public class EmployeeDAO {

    private final Connection connection;

    public EmployeeDAO() throws SQLException {
        this.connection = connectDB.getConnection();
    }

    // Phương thức để lấy tổng số nhân viên từ cơ sở dữ liệu và hiển thị lên JLabel
    public class count_em {

        final private JLabel totalLabel;
        final private JLabel staffLabel;  // JLabel cho role_id = 1
        final private JLabel leaderLabel; // JLabel cho role_id = 2

        public count_em(JLabel totalLabel, JLabel staffLabel, JLabel leaderLabel) {
            this.totalLabel = totalLabel;
            this.staffLabel = staffLabel;
            this.leaderLabel = leaderLabel;
        }

        // Phương thức để lấy tổng số nhân viên từ cơ sở dữ liệu và cập nhật JLabel
        public void TotalEmployee() {
            String sql = "SELECT COUNT(*) AS totalEmployees FROM Employees";

            try (Connection connect = connectDB.getConnection(); PreparedStatement ps = connect.prepareStatement(sql); ResultSet result = ps.executeQuery()) {

                int totalCount = 0;

                if (result.next()) {
                    totalCount = result.getInt("totalEmployees");
                }
                // Cập nhật JLabel cho tổng số nhân viên
                totalLabel.setText(String.valueOf(totalCount));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Phương thức để lấy số lượng nhân viên có role_id và cập nhật JLabel
        public void TotalEmployeeWithRole(String roleName) {
            String query = "SELECT COUNT(*) AS totalStaff FROM Roles WHERE role_name = ?";

            // Sử dụng try-with-resources
            try (Connection connect = connectDB.getConnection(); PreparedStatement prepare = connect.prepareStatement(query)) {

                prepare.setString(1, roleName);

                try (ResultSet result = prepare.executeQuery()) {
                    if (result.next()) {
                        int totalCount = result.getInt("totalStaff");

                        if (roleName.equals("Staff")) {
                            staffLabel.setText(String.valueOf(totalCount));
                        } else if (roleName.equals("Leader")) {
                            leaderLabel.setText(String.valueOf(totalCount));
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
                    em.setBaseSalary(rs.getFloat("base_salary"));
                    em.setNetSalary(rs.getFloat("net_salary"));
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

            try (PreparedStatement ps1 = connection.prepareStatement(query1); PreparedStatement ps2 = connection.prepareStatement(query2)) {
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
        // Các truy vấn để xóa dữ liệu liên quan trong các bảng phụ
        String deleteAllowancesQuery = "DELETE FROM Allowances WHERE role_id IN (SELECT role_id FROM Roles WHERE em_id = ?)";
        String deleteRolesQuery = "DELETE FROM Roles WHERE em_id = ?";
        String deleteSalariesQuery = "DELETE FROM Salaries WHERE em_id = ?";
        String deleteEmployeeQuery = "DELETE FROM Employees WHERE em_id = ?";

        try {
            connection.setAutoCommit(false); // Bắt đầu transaction

            // Xóa dữ liệu từ bảng Allowances
            try (PreparedStatement ps1 = connection.prepareStatement(deleteAllowancesQuery)) {
                ps1.setString(1, emID);
                ps1.executeUpdate();
            }

            // Xóa dữ liệu từ bảng Roles
            try (PreparedStatement ps2 = connection.prepareStatement(deleteRolesQuery)) {
                ps2.setString(1, emID);
                ps2.executeUpdate();
            }

            // Xóa dữ liệu từ bảng Salaries
            try (PreparedStatement ps3 = connection.prepareStatement(deleteSalariesQuery)) {
                ps3.setString(1, emID);
                ps3.executeUpdate();
            }

            // Xóa dữ liệu từ bảng Employees
            try (PreparedStatement ps4 = connection.prepareStatement(deleteEmployeeQuery)) {
                ps4.setString(1, emID);
                int affectedRows = ps4.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("No employee found with ID: " + emID);
                }
            }

            connection.commit(); // Commit transaction nếu mọi thứ thành công
            JOptionPane.showMessageDialog(null, "Employee and related data deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback transaction nếu xảy ra lỗi
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
