package com.myschoolprj.employeems.dao;

import com.myschoolprj.employeems.EmployeeSalary;
import com.myschoolprj.employeems.model.Employee;
import com.myschoolprj.employeems.utils.Validator;
import com.myschoolprj.employeems.utils.connectDB;
import javax.swing.JLabel;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

public class EmployeeDAO {

    private final Connection connection;

    public EmployeeDAO() throws SQLException {
        this.connection = connectDB.getConnection();
    }

    // Phương thức để lấy tổng số nhân viên từ cơ sở dữ liệu và hiển thị lên JLabel
    public class count_em {

        private JLabel totalLabel;
        private JLabel staffLabel;  // JLabel cho role_id = 1
        private JLabel leaderLabel; // JLabel cho role_id = 2

        public count_em(JLabel totalLabel, JLabel staffLabel, JLabel leaderLabel) {
            this.totalLabel = totalLabel;
            this.staffLabel = staffLabel;
            this.leaderLabel = leaderLabel;
        }

        // Phương thức để lấy tổng số nhân viên từ cơ sở dữ liệu và cập nhật JLabel
        public void TotalEmployee() {
            String sql = "SELECT COUNT(*) AS totalEmployees FROM Employees";

            try (Connection connect = connectDB.getConnection(); PreparedStatement prepare = connect.prepareStatement(sql); ResultSet result = prepare.executeQuery()) {

                int totalCount = 0;

                if (result.next()) {
                    totalCount = result.getInt("totalEmployees");
                }

                // Cập nhật JLabel cho tổng số nhân viên
                totalLabel.setText("--" + totalCount + "--");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Phương thức để lấy số lượng nhân viên có role_id và cập nhật JLabel
        public void TotalEmployeeWithRole(String roleName) {
            String sql = "SELECT COUNT(*) AS totalStaff FROM Roles WHERE role_name = ?";

            // Sử dụng try-with-resources
            try (Connection connect = connectDB.getConnection(); PreparedStatement prepare = connect.prepareStatement(sql)) {

                prepare.setString(1, roleName);

                try (ResultSet result = prepare.executeQuery()) {
                    if (result.next()) {
                        int totalCount = result.getInt("totalStaff");

                        if (roleName == "Staff") {
                            staffLabel.setText("--" + totalCount + "--");
                        } else if (roleName == "Leader") {
                            leaderLabel.setText("--" + totalCount + "--");
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
    public void updateSalary(ArrayList<EmployeeSalary> salaries) throws Exception {
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

    public void addEmployeeData(Employee em) {
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
                ps2.setString(1, em.getID()); // Corrected index for em_id
                ps2.setString(2, em.getRole()); // Corrected index for role_name
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


// Import dữ liệu từ file excel vào db
//    private void importExcel(File file) throws Exception {
//        String sql = "INSERT INTO Employees (em_id, firstname, lastname, phone, gender, dob, address, role_id, pos_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//
//        try (FileInputStream fis = new FileInputStream(file); Workbook workbook = WorkbookFactory.create(fis); Connection connection = connectDB.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//
//            Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên
//            for (Row row : sheet) {
//                if (row.getRowNum() == 0) {
//                    continue; // Bỏ qua tiêu đề
//                }
//                try {
//                    // Kiểm tra và đọc dữ liệu từng ô
//                    String emId = row.getCell(0).getStringCellValue();
//                    String firstName = row.getCell(1).getStringCellValue();
//                    String lastName = row.getCell(2).getStringCellValue();
//                    String phone = row.getCell(3).getStringCellValue();
//                    String gender = row.getCell(4).getStringCellValue();
//
//                    // Kiểm tra và xử lý ô ngày
//                    Cell dobCell = row.getCell(5);
//                    Date dob = null;
//                    if (dobCell != null && dobCell.getCellType() == CellType.NUMERIC) {
//                        dob = dobCell.getDateCellValue();
//                    }
//
//                    String address = row.getCell(6).getStringCellValue();
//                    int roleId = (int) row.getCell(7).getNumericCellValue();
//                    int posId = (int) row.getCell(8).getNumericCellValue();
//
//                    // Thiết lập các tham số
//                    preparedStatement.setString(1, emId);
//                    preparedStatement.setString(2, firstName);
//                    preparedStatement.setString(3, lastName);
//                    preparedStatement.setString(4, phone);
//                    preparedStatement.setString(5, gender);
//                    if (dob != null) {
//                        preparedStatement.setDate(6, new java.sql.Date(dob.getTime()));
//                    } else {
//                        preparedStatement.setNull(6, java.sql.Types.DATE);
//                    }
//                    preparedStatement.setString(7, address);
//                    preparedStatement.setInt(8, roleId);
//                    preparedStatement.setInt(9, posId);
//
//                    preparedStatement.addBatch(); // Thêm vào batch
//                } catch (Exception e) {
//                    System.err.println("Lỗi tại hàng " + row.getRowNum() + ": " + e.getMessage());
//                }
//            }
//            preparedStatement.executeBatch(); // Thực hiện batch
//        }
//    }

    public void updateEmployeeData(Employee em) {
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
                ps2.setString(1, em.getRole());
                ps2.setString(2, em.getID()); // WHERE condition
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

    public void deleteEmployee(String emID) throws Exception {
        // Since the Roles table likely has a foreign key relationship with the Employees table, we must 
        // delete from Roles first to avoid foreign key constraint violations.
        String query1 = "DELETE FROM Roles WHERE em_id = ?";
        String query2 = "DELETE FROM Employees WHERE em_id = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement ps1 = connection.prepareStatement(query1); 
                 PreparedStatement ps2 = connection.prepareStatement(query2)) {
                // Delete from Roles
                ps1.setString(1, emID);
                ps1.executeUpdate();

                // Delete from Employees
                ps2.setString(1, emID);
                int rowsAffected = ps2.executeUpdate();

                if (rowsAffected == 0) {
                    throw new Exception("No employee found with ID: " + emID);
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
