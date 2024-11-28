package com.myschoolprj.employeems.dao;

import com.myschoolprj.employeems.model.Employee;
import com.myschoolprj.employeems.utils.connectDB;
import com.myschoolprj.employeems.model.Salary;
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

    public void updateSalary(Salary sal) throws Exception {
        String query1 = "INSERT INTO Allowances(al_level, role_id) VALUES (?, ?)";
        String query2 = "INSERT INTO Salaries(em_id, base_salary, net_salary) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query1)) {

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error writing Salaries: " + e);
        }
    }

    public ArrayList<Salary> getMonSalData() throws SQLException {
        ArrayList<Salary> salaries = new ArrayList<>();
        String query = "SELECT sal.sal_id, sal.em_id, em.firstname, em.lastname, "
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

}
