/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myschoolprj.employeems.utils;
import java.awt.*;
import javax.swing.border.Border;
/**
 *
 * @author ADMIN
 */
public class border implements Border  {
    private int radius;
    private int shadowSize;

    public border(int radius, int shadowSize) {
        this.radius = radius;
        this.shadowSize = shadowSize;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        // Cạnh dưới có thêm shadowSize
        return new Insets(radius + 1, radius + 1, radius + shadowSize + 1, radius + 1);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Tạo màu đổ bóng mờ chỉ ở cạnh dưới
        Color shadowColor = new Color(0, 0, 0, 100);
        g2d.setColor(shadowColor);
        
        // Vẽ đổ bóng chỉ ở phía dưới
        g2d.fillRoundRect(x + shadowSize, y + height - shadowSize, width - shadowSize * 2, shadowSize, radius, radius);

        // Vẽ viền bo tròn chính
        g2d.setColor(c.getForeground());
        g2d.drawRoundRect(x, y, width - 1 - shadowSize, height - 1 - shadowSize, radius, radius);
    }
}



