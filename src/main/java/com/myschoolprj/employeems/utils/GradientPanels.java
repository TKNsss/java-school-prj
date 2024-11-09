/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myschoolprj.employeems.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.geom.Point2D;
import javax.swing.JPanel;

public class GradientPanels extends JPanel {
    private Color startColor;
    private Color endColor;
    
    public GradientPanels(Color _startColor, Color _endColor) {
        this.startColor = _startColor;
        this.endColor = _endColor;
    }
    
    // Override the paintComponent method to customize the rendering of the panel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Create a Graphics2D object for advanced rendering operations
        Graphics2D g2d = (Graphics2D) g.create();
        // Create a gradient paint object from the startColor to the endColor
        GradientPaint gradientPaint = new GradientPaint(
                        // Starting point of the gradient
                        new Point2D.Float(0,0), startColor,
                        // Ending point of the gradient
                        new Point2D.Float(0,getHeight()), endColor
        );
        
        // Set the paint of the Graphics2D object to the gradient paint
        g2d.setPaint(gradientPaint);
        
        // Fill a rectangle with the gradient, covering the entire panel
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}
