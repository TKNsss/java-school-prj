/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myschoolprj.employeems;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


/**
 *
 * @author ADMIN
 */
public class XFile {

    private String jdbcURL = "jdbc:mysql://localhost:1433/quanlynhanvien";
    private String jdbcUsername = "sa"; // Thay thế bằng tên người dùng của bạn
    private String jdbcPassword = "matkhaudongian"; // Thay thế bằng mật khẩu của bạn

    public ArrayList<EmployeeDataType> readEmployees() throws Exception {
        ArrayList<EmployeeDataType> employees = new ArrayList<>();
        String sql = "SELECT * FROM Quan_ly_nhan_vien"; 

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                EmployeeDataType employee = new EmployeeDataType();
                employee.setID(resultSet.getString("id")); // Thay thế 'id' bằng tên cột của bạn
                employee.setFirstName(resultSet.getString("first_name")); // Thay thế 'first_name'
                employee.setLastName(resultSet.getString("last_name")); // Thay thế 'last_name'
                employee.setAge(resultSet.getInt("age")); // Thay thế 'age'
                employee.setAddress(resultSet.getString("address")); // Thay thế 'address'
                employee.setGender(resultSet.getString("gender")); // Thay thế 'gender'
                employee.setPhone(resultSet.getString("phone")); // Thay thế 'phone'
                employees.add(employee);
            }
        }
        return employees;
    }

    public void writeEmployees(ArrayList<EmployeeDataType> employees) throws Exception {
        String sql = "INSERT INTO employees (id, first_name, last_name, age, address, gender, phone) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (EmployeeDataType employee : employees) {
                preparedStatement.setString(1, employee.getID());
                preparedStatement.setString(2, employee.getFirstName());
                preparedStatement.setString(3, employee.getLastName());
                preparedStatement.setInt(4, employee.getAge());
                preparedStatement.setString(5, employee.getAddress());
                preparedStatement.setString(6, employee.getGender());
                preparedStatement.setString(7, employee.getPhone());
                preparedStatement.addBatch(); // Thêm vào batch
            }
            preparedStatement.executeBatch(); // Thực hiện batch
        }
    }
}
