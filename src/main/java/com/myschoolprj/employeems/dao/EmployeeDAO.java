package com.myschoolprj.employeems.dao;

import com.myschoolprj.employeems.EmployeeSalary;
import com.myschoolprj.employeems.model.Employee;
import com.myschoolprj.employeems.utils.connectDB;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class EmployeeDAO {

    private final Connection connection;
    private ArrayList<Employee> emList;

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
        String sql = "UPDATE employees_salaries SET first_name = ?, last_name = ?, salary = ? WHERE id = ?";
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

    public ArrayList<Employee> readEmployees() throws Exception {
        ArrayList<Employee> employees = new ArrayList<>();
        String sql = """
        SELECT 
            e.em_id, 
            e.firstname, 
            e.lastname, 
            e.phone, 
            e.gender, 
            e.dob, 
            e.address, 
            r.role_name, 
            p.pos_name
        FROM 
            Employees e
        LEFT JOIN 
            Roles r ON e.role_id = r.role_id
        LEFT JOIN 
            Positions p ON e.pos_id = p.pos_id
    """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setID(rs.getString("em_id"));
                employee.setFirstName(rs.getString("firstname"));
                employee.setLastName(rs.getString("lastname"));
                employee.setPhone(rs.getString("phone"));
                employee.setGender(rs.getString("gender"));
                employee.setDob(rs.getDate("dob"));
                employee.setAddress(rs.getString("address"));
                employee.setRole(rs.getString("role_name")); // Lấy tên Role
                employee.setPosition(rs.getString("pos_name")); // Lấy tên Position
                employees.add(employee);
            }
            // In ra số lượng nhân viên đã tải
            System.out.println("Employees loaded: " + employees.size());
            for (Employee emp : employees) {
                System.out.println("Employee ID: " + emp.getID());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error reading Employees: " + e);
        }
        return employees;
    }

    public void writeEmployee(ArrayList<Employee> employees) throws Exception {
        String insertQuery = "INSERT INTO Employees(em_id, firstname, lastname, phone, gender, dob, address, role_id, pos_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            for (Employee employee : employees) {
                preparedStatement.setString(1, employee.getID());
                preparedStatement.setString(2, employee.getFirstName());
                preparedStatement.setString(3, employee.getLastName());
                preparedStatement.setString(4, employee.getPhone());
                preparedStatement.setString(5, employee.getGender());
                if (employee.getDob() != null) {
                    preparedStatement.setDate(6, new java.sql.Date(employee.getDob().getTime()));
                } else {
                    preparedStatement.setNull(6, java.sql.Types.DATE);
                }
                preparedStatement.setString(7, employee.getAddress());
                preparedStatement.setString(8, employee.getRole()); // role_id
                preparedStatement.setString(9, employee.getPosition()); // pos_id
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error writing Employees: " + e);
        }
    }

    public void updateEmployee(Employee employee) throws Exception {
        // Câu lệnh SQL UPDATE
        String sql = "UPDATE Employees SET em_id = ?, firstname = ?, lastname = ?, phone = ?, gender = ?, dob = ?, address = ?, role_id = ?, pos_id = ? WHERE em_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Thiết lập các tham số theo thứ tự
            preparedStatement.setString(1, employee.getID()); // id
            preparedStatement.setString(2, employee.getFirstName()); // first_name
            preparedStatement.setString(3, employee.getLastName()); // last_name
            preparedStatement.setString(4, employee.getPhone()); // phone
            preparedStatement.setString(5, employee.getGender()); // gender
            // Kiểm tra và thiết lập ngày tháng (date_of_birth)
            if (employee.getDob() != null) {
                preparedStatement.setDate(6, new java.sql.Date(employee.getDob().getTime())); // date_of_birth
            } else {
                preparedStatement.setNull(6, java.sql.Types.DATE); // NULL nếu không có ngày
            }
            preparedStatement.setString(7, employee.getAddress()); // address
            preparedStatement.setString(8, employee.getRole()); // role_id
            preparedStatement.setString(9, employee.getPosition()); // position_id

            preparedStatement.setString(10, employee.getID()); // Điều kiện WHERE (id)

            // Thực hiện lệnh UPDATE
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new Exception("Employee with ID " + employee.getID() + " not found. Update failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error updating Employees: " + e.getMessage());
        }
    }

    public void deleteEmployee(String employeeId) throws Exception {
        String sql = "DELETE FROM Employees WHERE em_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, employeeId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new Exception("No employee found with ID: " + employeeId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error deleting employee with ID " + employeeId + ": " + e.getMessage());
        }
    }

    // Lấy role_id từ role_name
    public String getRoleIDByName(String roleName) throws Exception {
        String query = "SELECT role_id FROM Roles WHERE role_name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, roleName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("role_id");
                }
            }
        }
        return null; // Nếu không tìm thấy
    }

// Lấy pos_id từ pos_name
    public String getPositionIDByName(String positionName) throws Exception {
        String query = "SELECT pos_id FROM Positions WHERE pos_name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, positionName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("pos_id");
                }
            }
        }
        return null; // Nếu không tìm thấy
    }



}
