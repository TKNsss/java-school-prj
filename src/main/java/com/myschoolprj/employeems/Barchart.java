package com.myschoolprj.employeems;

import com.myschoolprj.employeems.utils.connectDB;
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
import java.text.DecimalFormat;
import org.jfree.chart.axis.NumberTickUnit;

public class Barchart extends JPanel {

    // Khai báo biến chart như là thuộc tính của lớp
    private JFreeChart chart;
    private ChartPanel chartPanel;
    private final Connection connection;

    public Barchart() throws SQLException {
        System.out.println("Khởi tạo Barchart...");
        this.connection = connectDB.getConnection();

        // Tạo dataset cho biểu đồ từ cơ sở dữ liệu, lọc theo tháng và năm
        CategoryDataset dataset = createDatasetFromDB();

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
        // Automatically adjust Y-axis range
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRange(true); // Automatically adjusts the range based on data

        // Set a custom tick unit for better readability (optional)
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // Uses integers for tick marks
        rangeAxis.setTickUnit(new NumberTickUnit(100000000)); 
        rangeAxis.setNumberFormatOverride(new DecimalFormat("#,###")); // Format numbers with commas

        // Tạo ChartPanel và thêm vào JPanel
        chartPanel = new ChartPanel(chart);
        this.setLayout(new BorderLayout());
        this.add(chartPanel, BorderLayout.CENTER);

        // Thiết lập timer để tự động làm mới biểu đồ mỗi 10 giây
        Timer updateTimer = new Timer(3000, new ActionListener() {
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
        CategoryDataset newDataset = createDatasetFromDB();

        // Cập nhật dataset cho biểu đồ
        chart.getCategoryPlot().setDataset(newDataset);
        chartPanel.revalidate();  // Làm mới lại giao diện người dùng
        chartPanel.repaint();     // Vẽ lại biểu đồ
    }

    // Cập nhật dữ liệu từ cơ sở dữ liệu
    private CategoryDataset createDatasetFromDB() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Query to get net_salary for all employees
        String query = "SELECT em_id, MAX(net_salary) AS net_salary FROM Salaries GROUP BY em_id";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String employeeId = rs.getString("em_id");
                    double netSalary = rs.getDouble("net_salary");

                    System.out.println("Employee: " + employeeId + ", Net Salary: " + netSalary);
                    dataset.addValue(netSalary, "Net Salary", employeeId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }
}
