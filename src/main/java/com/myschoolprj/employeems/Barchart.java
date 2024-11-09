/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myschoolprj.employeems;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author ADMIN
 */

public class Barchart extends JPanel {

public Barchart() {
        System.out.println("Khởi tạo Barchart..."); // Debug message

        // Tạo dataset cho biểu đồ
        CategoryDataset dataset = createDataset();

        // Tạo biểu đồ
        JFreeChart chart = ChartFactory.createBarChart(
                "Employees Salaries", // Tiêu đề
                "List", // Trục X
                "Values", // Trục Y
                dataset // Dataset
        );

        // Tạo ChartPanel và thêm vào JPanel
        ChartPanel chartPanel = new ChartPanel(chart);
        this.setLayout(new BorderLayout());
        this.add(chartPanel, BorderLayout.CENTER);
    }

    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.2, "Salary", "ID-01");
        dataset.addValue(4, "Salary", "ID-02");
        dataset.addValue(3, "Salary", "ID-03");
        dataset.addValue(1.9, "Salary", "ID-04");
        dataset.addValue(1.7, "Salary", "ID-05");
        dataset.addValue(2.7, "Salary", "ID-06");
        dataset.addValue(1.5, "Salary", "ID-07");
        dataset.addValue(3.6, "Salary", "ID-08");
        return dataset;
    }
}
