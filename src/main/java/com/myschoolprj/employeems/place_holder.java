/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myschoolprj.employeems;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 *
 * @author ADMIN
 */
public class place_holder {
    
    public void addPlaceholder(javax.swing.JTextField textField, String placeholder) {
        textField.setForeground(Color.GRAY);
        textField.setText(placeholder);

        // Thêm sự kiện focus để xử lý placeholder
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK); // Đặt lại màu chữ khi người dùng bắt đầu nhập
                 }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY); // Đặt lại placeholder khi người dùng không nhập
                    textField.setText(placeholder);
                }
            }
        });
    }
}
