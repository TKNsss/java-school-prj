package com.myschoolprj.employeems.dao;

import com.myschoolprj.employeems.model.Employee;
import com.myschoolprj.employeems.utils.connectDB;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Date;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class EmployeeImport {

    private final Connection connection;

    public EmployeeImport() throws SQLException {
        this.connection = connectDB.getConnection();
    }

    // Đọc dữ liệu từ file Excel
    public List<Employee> readEmployeesFromExcel(String filePath) throws Exception {
        List<Employee> employees = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(filePath)); Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Bỏ qua dòng tiêu đề
                }

                // Đọc từng ô trong dòng
                Cell emIdCell = row.getCell(0);
                Cell firstNameCell = row.getCell(1);
                Cell lastNameCell = row.getCell(2);
                Cell phoneCell = row.getCell(3);
                Cell genderCell = row.getCell(4);
                Cell dobCell = row.getCell(5);
                Cell addressCell = row.getCell(6);
                Cell roleCell = row.getCell(7);
                Cell posCell = row.getCell(8);

                // Xử lý kiểu dữ liệu của từng ô
                String emId = emIdCell != null ? emIdCell.getStringCellValue().trim() : "";
                String firstName = firstNameCell != null ? firstNameCell.getStringCellValue().trim() : "";
                String lastName = lastNameCell != null ? lastNameCell.getStringCellValue().trim() : "";
                String phone = phoneCell != null ? phoneCell.getStringCellValue().trim() : "";
                String gender = genderCell != null ? genderCell.getStringCellValue().trim() : "";
                Date dob = dobCell != null ? dobCell.getDateCellValue() : null;
                String address = addressCell != null ? addressCell.getStringCellValue().trim() : "";
                String role = roleCell != null ? roleCell.getStringCellValue().trim() : "";
                String pos = posCell != null ? posCell.getStringCellValue().trim() : "";

                // Tạo đối tượng Employee và thêm vào danh sách
                //employees.add(new Employee(emId, firstName, lastName, phone, gender, dob, address, role, pos));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("error: " + e.getMessage());
        }

        return employees;
    }

    public void saveEmployeeToDatabase(List<Employee> employees) {
        String employeeQuery = "INSERT INTO Employees (em_id, firstname, lastname, phone, gender, dob, address, pos_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String roleQuery = "INSERT INTO Roles (em_id, role_name) VALUES (?, ?)";
        String positionQuery = "SELECT pos_id FROM Positions WHERE title = ?"; // Lấy pos_id từ bảng Positions

        try (PreparedStatement employeePs = connection.prepareStatement(employeeQuery); PreparedStatement rolePs = connection.prepareStatement(roleQuery); PreparedStatement posPs = connection.prepareStatement(positionQuery)) {

            // Thêm nhân viên vào bảng Employees và vai trò vào bảng Roles
            for (Employee employee : employees) {
                // Lấy pos_id từ bảng Positions
                posPs.setString(1, employee.getPosition());
                ResultSet posRs = posPs.executeQuery();
                int posId = 0;
                if (posRs.next()) {
                    posId = posRs.getInt("pos_id");
                }

                // Thêm nhân viên vào bảng Employees
                employeePs.setString(1, employee.getID());
                employeePs.setString(2, employee.getFirstName());
                employeePs.setString(3, employee.getLastName());
                employeePs.setString(4, employee.getPhone());
                employeePs.setString(5, employee.getGender());
                employeePs.setDate(6, new java.sql.Date(employee.getDob().getTime()));
                employeePs.setString(7, employee.getAddress());
                employeePs.setInt(8, posId); // Sử dụng pos_id từ bảng Positions
                employeePs.addBatch();

                // Thêm vai trò vào bảng Roles
                rolePs.setString(1, employee.getID());
                rolePs.setString(2, employee.getRole()); // Lấy role_name từ Excel hoặc từ đối tượng Employee
                rolePs.addBatch();
            }

            // Thực thi các câu lệnh thêm nhân viên và vai trò
            employeePs.executeBatch();
            rolePs.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
