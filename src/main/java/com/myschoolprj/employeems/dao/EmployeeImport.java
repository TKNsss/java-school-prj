package com.myschoolprj.employeems.dao;

import com.myschoolprj.employeems.model.Employee;
import com.myschoolprj.employeems.utils.connectDB;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Date;

import java.io.FileOutputStream;
import java.io.IOException;
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
    
    // Phương thức xuất dữ liệu lương vào file Excel
    public void exportSalaryToExcel(String filePath) throws SQLException, IOException {
        String query = "SELECT s.sal_id, e.em_id, e.firstname, e.lastname, s.work_day, s.month, s.year, s.base_salary, s.net_salary, s.month_salary " +
                       "FROM Salaries s " +
                       "JOIN Employees e ON s.em_id = e.em_id";

        try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Salaries");

            // Tạo hàng tiêu đề
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Salary ID", "Employee ID", "First Name", "Last Name", "Work Days", "Month", "Year", "Base Salary", "Net Salary", "Month Salary"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // Thêm dữ liệu vào các dòng tiếp theo
            int rowNum = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rs.getInt("sal_id"));
                row.createCell(1).setCellValue(rs.getString("em_id"));
                row.createCell(2).setCellValue(rs.getString("firstname"));
                row.createCell(3).setCellValue(rs.getString("lastname"));
                row.createCell(4).setCellValue(rs.getInt("work_day"));
                row.createCell(5).setCellValue(rs.getInt("month"));
                row.createCell(6).setCellValue(rs.getInt("year"));
                row.createCell(7).setCellValue(rs.getDouble("base_salary"));
                row.createCell(8).setCellValue(rs.getDouble("net_salary"));
                row.createCell(9).setCellValue(rs.getDouble("month_salary"));
            }

            // Lưu file Excel
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
            workbook.close();
        }
    }

    // Phương thức export dữ liệu từ cơ sở dữ liệu ra Excel
    public void exportToExcel(String filePath) {
        String query = "SELECT e.em_id, e.firstname, e.lastname, e.phone, e.gender, e.dob, e.address, p.title, r.role_name "
                + "FROM Employees e "
                + "JOIN Roles r ON e.em_id = r.em_id "
                + "JOIN Positions p ON e.pos_id = p.pos_id";

        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery(); XSSFWorkbook workbook = new XSSFWorkbook()) {

            // Tạo sheet và header
            Sheet sheet = workbook.createSheet("Employee Data");
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Employee ID", "First Name", "Last Name", "Phone", "Gender", "Date of Birth", "Address", "Position", "Role"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Thêm dữ liệu vào sheet
            int rowNum = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rs.getString("em_id"));
                row.createCell(1).setCellValue(rs.getString("firstname"));
                row.createCell(2).setCellValue(rs.getString("lastname"));
                row.createCell(3).setCellValue(rs.getString("phone"));
                row.createCell(4).setCellValue(rs.getString("gender"));
                row.createCell(5).setCellValue(rs.getDate("dob"));
                row.createCell(6).setCellValue(rs.getString("address"));
                row.createCell(7).setCellValue(rs.getString("title"));
                row.createCell(8).setCellValue(rs.getString("role_name"));
            }

            // Ghi dữ liệu vào file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                JOptionPane.showMessageDialog(null, "Data exported successfully!", "Export Success", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error exporting data: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
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
                Employee em = new Employee();
                em.setID(emId);
                em.setFirstName(firstName);
                em.setLastName(lastName);
                em.setPhone(phone);
                em.setGender(gender);
                em.setDob(dob);
                em.setAddress(address);
                em.setRole(role);
                em.setPosition(pos);

                employees.add(em);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("error: " + e.getMessage());
        }
        return employees;
    }

    public void saveEmployeeToDatabase(List<Employee> employees) {
        String positionQuery = "SELECT pos_id FROM Positions WHERE title = ?"; // Lấy pos_id từ bảng Positions
        String employeeQuery = "INSERT INTO Employees (em_id, firstname, lastname, phone, gender, dob, address, pos_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String roleQuery = "INSERT INTO Roles (em_id, role_name) VALUES (?, ?)";

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
