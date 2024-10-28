/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myschoolprj.employeems;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 *
 * @author ADMIN
 */
public class jPanelGradient extends JPanel{
    @Override
        protected void paintComponent(Graphics h){
            Graphics2D g2d = (Graphics2D) h;
            int width =  getWidth();
            int height =  getHeight();
            
            Color color1 = new Color(39, 43, 63);
            Color color2 = new Color(37, 107, 81);
            
            GradientPaint gp = new GradientPaint(0,0,color1,180,height,color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
        }
       
    }