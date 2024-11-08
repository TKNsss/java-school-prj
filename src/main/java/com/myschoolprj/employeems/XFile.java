/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myschoolprj.employeems;

import com.myschoolprj.employeems.utils.connectDB;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author ADMIN
 */
public class XFile {

    private Connection connection;

    public XFile() {
        connection = connectDB.getConnection();
    }

    public ArrayList<EmployeeSalary> readSalary() throws Exception {
        ArrayList<EmployeeSalary> salaries = new ArrayList<>();
        String sql = "SELECT * FROM employees_salaries";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                EmployeeSalary salary = new EmployeeSalary();
                salary.setID(resultSet.getString("id"));
                salary.setFirstName(resultSet.getString("first_name"));
                salary.setLastName(resultSet.getString("last_name"));
                salary.setSalary(resultSet.getFloat("salary"));
                salaries.add(salary);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error reading Salaries: " + e);
        }
        return salaries;
    }

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

    public ArrayList<EmployeeDataType> readEmployees() throws Exception {
        ArrayList<EmployeeDataType> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees_infor";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                EmployeeDataType employee = new EmployeeDataType();
                employee.setID(resultSet.getString("id")); // Thay thế 'id' bằng tên cột của bạn4
                employee.setStatus(resultSet.getString("status"));
                employee.setFirstName(resultSet.getString("first_name")); // Thay thế 'first_name'
                employee.setLastName(resultSet.getString("last_name")); // Thay thế 'last_name'
                employee.setGender(resultSet.getString("gender")); // Thay thế 'gender'
                employee.setPhone(resultSet.getString("phone")); // Thay thế 'gender'
                employee.setPosition(resultSet.getString("position"));
                employee.setAddress(resultSet.getString("address")); // Thay thế 'address'
                employee.setDateOfBirth(resultSet.getDate("date_of_birth"));
                employees.add(employee);
            }
            // In ra số lượng nhân viên đã tải
            System.out.println("Employees loaded: " + employees.size());
            for (EmployeeDataType emp : employees) {
                System.out.println("Employee ID: " + emp.getID());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error reading Employees: " + e);
        }
        return employees;
    }

    public void writeEmployees(ArrayList<EmployeeDataType> employees) throws Exception {
        String sql = "INSERT INTO employees_infor (id, status, first_name, last_name, gender, phone, position, address, date_of_birth) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (EmployeeDataType employee : employees) {
                preparedStatement.setString(1, employee.getID());
                preparedStatement.setString(2, employee.getStatus());
                preparedStatement.setString(3, employee.getFirstName());
                preparedStatement.setString(4, employee.getLastName());
                preparedStatement.setString(5, employee.getGender());
                preparedStatement.setString(6, employee.getPhone());
                preparedStatement.setString(7, employee.getPosition());
                preparedStatement.setString(8, employee.getAddress());
                if (employee.getDateOfBirth() != null) {
                    preparedStatement.setDate(9, new java.sql.Date(employee.getDateOfBirth().getTime()));
                } else {
                    preparedStatement.setNull(9, java.sql.Types.DATE);
                }

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error writing Employees: " + e);
        }
    }

    public void updateEmployee(EmployeeDataType employee) throws Exception {
        String sql = "UPDATE employees_infor SET status = ?, first_name = ?, last_name = ?, gender = ?, phone = ?, position = ?, address = ?, date_of_birth = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Thiết lập các tham số theo đúng thứ tự
            preparedStatement.setString(1, employee.getStatus());
            preparedStatement.setString(2, employee.getFirstName());
            preparedStatement.setString(3, employee.getLastName());
            preparedStatement.setString(4, employee.getGender());
            preparedStatement.setString(5, employee.getPhone());
            preparedStatement.setString(6, employee.getPosition());
            preparedStatement.setString(7, employee.getAddress());

            // Chuyển đổi ngày sang java.sql.Date
            if (employee.getDateOfBirth() != null) {
                preparedStatement.setDate(8, new java.sql.Date(employee.getDateOfBirth().getTime())); // date_of_birth
            } else {
                throw new Exception("Date of birth cannot be null."); // Ném lỗi nếu ngày là null
            }

            preparedStatement.setString(9, employee.getID()); // Điều kiện WHERE

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error updating Employees: " + e);
        }
    }

    public void deleteEmployee(String employeeId) throws Exception {
        String sql = "DELETE FROM employees_infor WHERE id = ?";
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
}
