package com.myschoolprj.employeems;

import javax.swing.*;
import java.awt.*;

public class Line extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // Gọi hàm này để đảm bảo các thành phần khác được vẽ trước
        
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        
        // Vẽ gradient
        Color color1 = new Color(39, 43, 63);
        Color color2 = new Color(37, 107, 81);
        GradientPaint gp = new GradientPaint(0, 0, color1, 180, height, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);

        // Vẽ đường thẳng
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  // Chống răng cưa
        g2d.setStroke(new BasicStroke(2));  // Độ dày của đường thẳng
        g2d.setColor(Color.BLACK);  // Màu của đường thẳng
        
        // Vẽ một đường thẳng từ điểm (10, 190) đến điểm (250, 190)
        g2d.drawLine(10, 190, 210, 190);
    }

    // Test hiển thị panel
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Line panel = new Line();
        frame.add(panel);
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
