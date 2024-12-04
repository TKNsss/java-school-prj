package com.myschoolprj.employeems.dao;

import com.myschoolprj.employeems.utils.connectDB;
import com.myschoolprj.employeems.model.Salary;
import com.myschoolprj.employeems.model.Role;
import com.myschoolprj.employeems.utils.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class SalaryDAO {

    private final Connection connection;

    public SalaryDAO() throws SQLException {
        this.connection = connectDB.getConnection();
    }

    public ArrayList<Salary> getMonthSalData() throws SQLException {
        ArrayList<Salary> salaries = new ArrayList<>();
        String query = "SELECT sal.sal_id, sal.em_id, firstname, lastname, "
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
    }

    public void updateSalary(Salary sal, Role role) {
        // Kiểm tra xem bản ghi lương đã tồn tại chưa
        if (!isEmIDExists(sal.getEmId())) {
            insertSalary(sal, role);  // Nếu chưa tồn tại, chèn mới
            return;
        }

        // Các câu lệnh SQL để cập nhật dữ liệu
        String query1 = "UPDATE Allowances SET al_level = ? WHERE role_id = ?";
        String query2 = "UPDATE Salaries SET base_salary = ?, net_salary = ? WHERE em_id = ? AND month = ? AND year = ?";

        try {
            // Bắt đầu giao dịch
            connection.setAutoCommit(false);

            try (PreparedStatement ps1 = connection.prepareStatement(query1); 
                 PreparedStatement ps2 = connection.prepareStatement(query2)) {

                // Cập nhật Allowances
                ps1.setDouble(1, role.getAllowance());
                ps1.setInt(2, role.getRoleId());
                ps1.executeUpdate();

                // Cập nhật Salaries
                ps2.setFloat(1, sal.getBaseSalary());
                ps2.setFloat(2, sal.getNetSalary());
                ps2.setString(3, sal.getEmId());
                ps2.setInt(4, sal.getMonth());
                ps2.setInt(5, sal.getYear());
                ps2.executeUpdate();
            }

            // Cam kết giao dịch
            connection.commit();
            JOptionPane.showMessageDialog(null, "Salary updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            try {
                // Rollback nếu có lỗi
                connection.rollback();
                JOptionPane.showMessageDialog(null, "Error updating Salary:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException rollbackEx) {
                JOptionPane.showMessageDialog(null, "Error rolling back transaction:\n" + rollbackEx.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
            Validator.printSQLExceptionMessage(e);
        } finally {
            try {
                // Khôi phục chế độ auto-commit
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                JOptionPane.showMessageDialog(null, "Error restoring auto-commit mode:\n" + autoCommitEx.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void insertSalary(Salary sal, Role role) {
        String query1 = "INSERT INTO Allowances(al_level, role_id) VALUES (?, ?)";
        String query2 = "INSERT INTO Salaries(em_id, base_salary, net_salary, work_day, month, year, month_salary) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try {
            // Kiểm tra xem bản ghi đã tồn tại trong bảng Salaries chưa
            if (isSalaryRecordExists(sal.getEmId(), sal.getMonth(), sal.getYear())) {
                JOptionPane.showMessageDialog(null, "Salary record for employee " + sal.getEmId() + " in " + sal.getMonth() + "/" + sal.getYear() + " already exists.", "Warning", JOptionPane.WARNING_MESSAGE);
                return; // Nếu đã tồn tại, không tiếp tục thêm mới
            }

            // Bắt đầu giao dịch
            connection.setAutoCommit(false);

            try (PreparedStatement ps1 = connection.prepareStatement(query1); PreparedStatement ps2 = connection.prepareStatement(query2)) {

                // Chèn vào bảng Allowances
                ps1.setDouble(1, role.getAllowance());
                ps1.setInt(2, role.getRoleId());
                ps1.executeUpdate();

                // Chèn vào bảng Salaries
                ps2.setString(1, sal.getEmId());
                ps2.setFloat(2, sal.getBaseSalary());
                ps2.setFloat(3, sal.getNetSalary());
                ps2.setInt(4, sal.getWorkDay());
                ps2.setInt(5, sal.getMonth());
                ps2.setInt(6, sal.getYear());
                ps2.setFloat(7, sal.getMonSalary());
                ps2.executeUpdate();
            }
            // Cam kết giao dịch
            connection.commit();
            JOptionPane.showMessageDialog(null, "Salary inserted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            try {
                // Rollback nếu có lỗi
                connection.rollback();
                JOptionPane.showMessageDialog(null, "Error inserting Salary:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException rollbackEx) {
                JOptionPane.showMessageDialog(null, "Error rolling back transaction:\n" + rollbackEx.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } finally {
            try {
                // Khôi phục chế độ auto-commit
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                JOptionPane.showMessageDialog(null, "Error restoring auto-commit mode:\n" + autoCommitEx.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean isSalaryRecordExists(String emId, int month, int year) {
        String query = "SELECT COUNT(*) FROM Salaries WHERE em_id = ? AND month = ? AND year = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, emId);
            ps.setInt(2, month);
            ps.setInt(3, year);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu số lượng bản ghi lớn hơn 0, tức là đã có bản ghi tồn tại
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching month salary data:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            Validator.printSQLExceptionMessage(e);
        }
        return false; // Nếu không có bản ghi tồn tại
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
    }

}
