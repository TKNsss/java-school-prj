package com.myschoolprj.employeems.dao;

import com.myschoolprj.employeems.EmployeeSalary;
import com.myschoolprj.employeems.model.Employee;
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

//    public ArrayList<EmployeeSalary> readSalary() throws SQLException {
//        ArrayList<EmployeeSalary> salaries = new ArrayList<>();
//        String sql = "SELECT * FROM employees_salaries";
//        
//        try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
//            while (resultSet.next()) {
//                EmployeeSalary salary = new EmployeeSalary();
//                salary.setID(resultSet.getString("id"));
//                salary.setFirstName(resultSet.getString("first_name"));
//                salary.setLastName(resultSet.getString("last_name"));
//                salary.setSalary(resultSet.getFloat("salary"));
//                salaries.add(salary);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Exception("Error reading Salaries: " + e);
//        }
//        return salaries;
//    }
    public void writeSalary(ArrayList<EmployeeSalary> salaries) throws Exception {
        String sql = "INSERT INTO employees_salaries (id, first_name, last_name, salary) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (EmployeeSalary salary : salaries) {
                preparedStatement.setString(1, salary.getID());
                preparedStatement.setString(2, salary.getFirstName());
                preparedStatement.setString(3, salary.getLastName());
                preparedStatement.setFloat(4, salary.getSalary());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error writing Salaries: " + e);
        }
    }

    public void updateSalary(EmployeeSalary salary) throws Exception {
        String sql = "UPDATE employees_salaries SET first_name = ?, last_name = ?, phone = ?, gender = ?, dob = ?, address = ?, role_id = ?, pos_id = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Thiết lập các tham số theo đúng thứ tự
            preparedStatement.setString(1, salary.getFirstName());
            preparedStatement.setString(2, salary.getLastName());
            preparedStatement.setFloat(3, salary.getSalary()); // Giả sử salary là kiểu double
            preparedStatement.setString(4, salary.getID()); // ID để xác định bản ghi cần cập nhật

            // Thực thi câu lệnh cập nhật
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error updating salary: " + e);
        }
    }

    public ArrayList<Employee> getEmployeeData() {
        ArrayList<Employee> employees = new ArrayList<>();
        String query = "SELECT DISTINCT em.em_id AS eID, firstname, lastname, phone, gender, dob, address, base_salary, net_salary, title, sal_col_level, role_name, al_level "
                + "FROM Employees AS em "
                + "LEFT JOIN Positions AS pos ON em.pos_id = pos.pos_id "
                + "LEFT JOIN Salaries AS sal ON sal.em_id = em.em_id "
                + "LEFT JOIN Roles AS ro ON em.role_id = ro.role_id "
                + "LEFT JOIN Allowances AS al ON ro.al_id = al.al_id";

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

    public void addEmployeeData(Employee em) {
        if (isPhoneExists(em.getPhone(), null) || isIDExists(em.getID())) {
            JOptionPane.showMessageDialog(null, "Phone number/ID has existed, please choose another one!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String query = "INSERT INTO Employees(em_id, firstname, lastname, phone, gender, dob, address, role_id, pos_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps1 = connection.prepareStatement(query)) {
            // insert into Employees
            ps1.setString(1, em.getID());
            ps1.setString(2, em.getFirstName());
            ps1.setString(3, em.getLastName());
            ps1.setString(4, em.getPhone());
            ps1.setString(5, em.getGender());
            ps1.setDate(6, new java.sql.Date(em.getDob().getTime()));
            ps1.setString(7, em.getAddress());
            ps1.setInt(8, em.getRoleID());
            ps1.setInt(9, em.getPositionID());
            ps1.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding Employees:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            Validator.printSQLExceptionMessage(e);
        }
    }

    public void updateEmployeeData(Employee em) {
        if (isPhoneExists(em.getPhone(), em.getID())) {
            JOptionPane.showMessageDialog(null, "Phone number has existed, please choose another one!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String sql = "UPDATE Employees SET firstname = ?, lastname = ?, phone = ?, gender = ?, dob = ?, address = ?, role_id = ?, pos_id = ? WHERE em_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, em.getFirstName());
            ps.setString(2, em.getLastName());
            ps.setString(3, em.getPhone());
            ps.setString(4, em.getGender());

            // Chuyển đổi ngày sang java.sql.Date
            if (em.getDob() != null) {
                ps.setDate(5, new java.sql.Date(em.getDob().getTime()));
            } else {
                JOptionPane.showMessageDialog(null, "Date of birth cannot be null.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return; // Abort the update 
            }
            ps.setString(6, em.getAddress());
            ps.setInt(7, em.getRoleID());
            ps.setInt(8, em.getPositionID());
            ps.setString(9, em.getID()); // Điều kiện WHERE

            ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating Employees:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            Validator.printSQLExceptionMessage(e);
        }
    }

    public void deleteEmployee(String emID) throws Exception {
        String sql = "DELETE FROM Employees WHERE em_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, emID);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new Exception("No employee found with ID: " + emID);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting Employees:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            Validator.printSQLExceptionMessage(e);
        }
    }

    public int getRoleIDByName(String roleName) throws SQLException {
        String query = "SELECT role_id FROM Roles WHERE role_name = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, roleName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("role_id");
                }
            }
        }
        return -1;
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
