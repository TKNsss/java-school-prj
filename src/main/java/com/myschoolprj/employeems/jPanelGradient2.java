package com.myschoolprj.employeems;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

public class jPanelGradient2 extends JPanel {
    private int cornerRadius; // Bán kính bo góc
    private Color color1;
    private Color color2;
    private Color color3;

    // Constructor
    public jPanelGradient2(int cornerRadius, Color color1, Color color2, Color color3) {
        this.cornerRadius = cornerRadius;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        setOpaque(false); // Đảm bảo JPanel trong suốt ngoài phần nền được vẽ
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Bật tính năng khử răng cưa để vẽ mượt mà hơn
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Lấy kích thước của JPanel
        int width = getWidth();
        int height = getHeight();
        
        // Vẽ gradient chéo với 3 màu
        GradientPaint gp1 = new GradientPaint(0, 0, color1, width / 2, height / 2, color2);
        g2.setPaint(gp1);
        g2.fill(new RoundRectangle2D.Double(0, 0, width, height, cornerRadius, cornerRadius));

        GradientPaint gp2 = new GradientPaint(width / 2, height / 2, color2, width, height, color3);
        g2.setPaint(gp2);
        g2.fill(new RoundRectangle2D.Double(0, 0, width, height, cornerRadius, cornerRadius));
        
        // Vẽ hình chữ nhật bo góc tròn với gradient
        g2.setPaint(new GradientPaint(0, 0, color1, width, height, color3));
        g2.fill(new RoundRectangle2D.Double(0, 0, width, height, cornerRadius, cornerRadius));
    }
}
