/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myschoolprj.employeems;

import com.toedter.calendar.JDateChooser; 
import java.awt.*; 
import javax.swing.*;
import java.util.Calendar; 
import java.util.Date; 
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextField;

/**
 *
 * @author ADMIN
 */
public class Validator {
        public static boolean check_Empty1(JTextField field, StringBuilder sb, String msg) {
        if (field.getText().equals("")) {
            sb.append(msg).append("\n");
            field.setBackground(Color.red);
            return false;
        } else {
            field.setBackground(Color.white);
        }
        return true;
    }
        public static boolean check_Empty2(JComboBox field, StringBuilder sb, String msg) {
        if (field.getSelectedIndex() == -1) {
            sb.append(msg).append("\n"); 
            field.setBackground(Color.red);
            return false; 
        } else {
            field.setBackground(Color.white); 
        }
        return true; 
    }
        
        

    public static boolean check_Phone(JTextField field, StringBuilder sb) {
        // Kiểm tra xem trường có rỗng không
        if (!check_Empty1(field, sb, "Do not leave Phone Number blank!")) {
            return false;
        }

        String phoneNumber = field.getText().trim();

        // Kiểm tra độ dài số điện thoại
        if (phoneNumber.length() != 10 && !phoneNumber.matches("^\\+\\d{1,3}[- ]?\\d{10}$")) {
            sb.append("Phone number must be 10 digits long or in the format +[country code]-XXXXXXXXXX!\n");
            field.setBackground(Color.red);
            return false;
        }

        // Biểu thức chính quy cho số điện thoại
        Pattern pattern = Pattern.compile("^(\\+\\d{1,3}[- ]?)?\\d{10}$");
        Matcher matcher = pattern.matcher(phoneNumber);

        // Kiểm tra xem số điện thoại có hợp lệ không
        if (!matcher.find()) {
            sb.append("Phone number not valid! It should be 10 digits or in the format +[country code]-XXXXXXXXXX!\n");
            field.setBackground(Color.red);
            return false;
        }

        // Nếu tất cả các kiểm tra đều hợp lệ
        field.setBackground(Color.white);
        return true;
    }    

    public static boolean check_Salary(JTextField field, StringBuilder sb) {

        if (!check_Empty1(field, sb, "Do not leave Salary blank !")) {
            return false;
        }

        try {
            double salary = Double.parseDouble(field.getText());

            if (salary < 200) {
                sb.append("Salary must be greater than 200 !\n");
                field.setBackground(Color.yellow);
                return false;
            }
        } catch (Exception e) {
            sb.append("Salary value must be numeric !\n");
            field.setBackground(Color.red);
            return false;
        }
        field.setBackground(Color.white);
        return true;
    }
    
    public static boolean check_Age(JDateChooser a, StringBuilder sb) {

    Date selectedDate = a.getDate();
    if (selectedDate == null) {
            sb.append("Please select date of birth !\n");
            a.setBackground(Color.RED);
        return false;
    }

    // Tính toán tuổi
    Calendar birthDate = Calendar.getInstance();
    birthDate.setTime(selectedDate);

    Calendar today = Calendar.getInstance();
    int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

    // Kiểm tra xem ngày sinh đã qua sinh nhật trong năm hiện tại chưa
    if (today.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH) ||
            (today.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH))) {
        age--;
    }

    // Kiểm tra tuổi
    if (age < 18 || age > 55) {
        sb.append("Age must be within range 18 - 55\n");
        a.setBackground(Color.RED);
        return false;
    }
    a.setBackground(Color.WHITE);
    return true;
}
}
