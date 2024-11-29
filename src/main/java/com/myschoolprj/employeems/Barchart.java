package com.myschoolprj.employeems;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Barchart extends JPanel {

    // Khai báo biến chart như là thuộc tính của lớp
    private JFreeChart chart;
    private ChartPanel chartPanel;

    public Barchart() {
        System.out.println("Khởi tạo Barchart...");

        // Tạo dataset cho biểu đồ từ cơ sở dữ liệu, lọc theo tháng và năm
        CategoryDataset dataset = createDatasetFromDB(11, 2024); // Lọc theo tháng 11 năm 2024

        // Tạo biểu đồ
        chart = ChartFactory.createBarChart(
                "Employees Net Salaries", // Tiêu đề
                "Employee ID", // Trục X
                "Net Salary", // Trục Y
                dataset, // Dataset
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true, // Hiển thị chú thích
                true, // Hiển thị tooltip
                false // Không hiển thị URL
        );

        // Thiết lập hiển thị giá trị lên trên cột
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setDomainGridlinesVisible(true);  // Hiển thị đường lưới cho trục X

        // Lấy BarRenderer từ CategoryPlot
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        
        // Cài đặt nhãn cho mỗi cột
        renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setItemLabelsVisible(true);  // Hiển thị nhãn trên mỗi cột

        // Tự động điều chỉnh phạm vi trục Y
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0, 200000000);  // Ví dụ: đặt phạm vi từ 0 đến 1000

        // Tạo ChartPanel và thêm vào JPanel
        chartPanel = new ChartPanel(chart);
        this.setLayout(new BorderLayout());
        this.add(chartPanel, BorderLayout.CENTER);

        // Thiết lập timer để tự động làm mới biểu đồ mỗi 10 giây
        Timer updateTimer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cập nhật lại dữ liệu từ cơ sở dữ liệu và làm mới biểu đồ
                updateChart(11, 2024);  // Cập nhật theo tháng và năm mong muốn
            }
        });
        updateTimer.start();  // Bắt đầu timer
    }

    // Phương thức cập nhật dataset và biểu đồ
    public void updateChart(int month, int year) {
        // Lấy dữ liệu mới từ cơ sở dữ liệu
        CategoryDataset newDataset = createDatasetFromDB(month, year);

        // Cập nhật dataset cho biểu đồ
        chart.getCategoryPlot().setDataset(newDataset);
        chartPanel.revalidate();  // Làm mới lại giao diện người dùng
        chartPanel.repaint();     // Vẽ lại biểu đồ
    }

    // Cập nhật dữ liệu từ cơ sở dữ liệu
    private CategoryDataset createDatasetFromDB(int month, int year) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Cấu hình kết nối cơ sở dữ liệu
        String url = "jdbc:sqlserver://localhost:1433;databaseName=EmployeeMS;encrypt=true;trustServerCertificate=true;";
        String user = "sa";  // Thay bằng username của bạn
        String password = "matkhaudongian"; // Thay bằng password của bạn

        // Câu lệnh SQL để lấy dữ liệu của các nhân viên trong tháng và năm cụ thể
        String query = "SELECT em_id, month_salary FROM Salaries WHERE month = ? AND year = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, month); // Gắn giá trị tháng vào câu truy vấn
            stmt.setInt(2, year);  // Gắn giá trị năm vào câu truy vấn

            try (ResultSet rs = stmt.executeQuery()) {
                // Lấy dữ liệu từ ResultSet và thêm vào dataset
                while (rs.next()) {
                    String employeeId = rs.getString("em_id"); // Mã nhân viên
                    double monthSalary = rs.getDouble("month_salary"); // Lương tháng
                    System.out.println("Employee: " + employeeId + ", Net Salary: " + monthSalary); // Debug line

                    // Thêm dữ liệu vào dataset
                    dataset.addValue(monthSalary, "Month Salary", employeeId);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // In ra thông báo lỗi nếu có sự cố
        }

        return dataset;
    }
}