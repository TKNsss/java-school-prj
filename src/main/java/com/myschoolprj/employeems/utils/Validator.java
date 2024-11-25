package com.myschoolprj.employeems.utils;

import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JTextField;

public class Validator {

    public static boolean checkEmptyFields(JTextField tf, StringBuilder sb, String msg) {
        if (tf.getText().trim().isEmpty()
                || tf.getText().equals("Enter Username")
                || tf.getText().equals("Enter Password")
                || tf.getText().equals("Confirm Password")) {
            sb.append(msg + "\n");
            tf.setBackground(Color.decode("#FFCDD2"));
            return false;
        }
        tf.setBackground(Color.WHITE);
        return true;
    }

    public static boolean checkEmptyComboBoxes(JComboBox tf, StringBuilder sb, String msg) {
        if (tf.getSelectedIndex() == -1) {
            sb.append(msg).append("\n");
            tf.setBackground(Color.decode("#FFCDD2"));
            return false;
        }
        tf.setBackground(Color.WHITE);
        return true;
    }

    public static boolean checkSpecialCharacters(JTextField tf, StringBuilder sb, String msg) {
        // regular expression for checking special characters
        String specialCharsPattern = "[$&+,:;=?@#|'<>.^*()%!\\[\\]-]";

        // Check for special characters in the field text
        if (!checkEmptyFields(tf, sb, msg)) {
            return false;
        }

        if (tf.getText().matches(".*" + specialCharsPattern + ".*")) {
            String errorMessage = "No special characters, please!\n";

            if (!sb.toString().contains(errorMessage)) {
                sb.append(errorMessage);
            }
            tf.setBackground(Color.decode("#FFCDD2"));
            return false;
        }
        tf.setBackground(Color.WHITE);
        return true;
    }

    public static boolean checkValidPhone(JTextField tf, StringBuilder sb) {
        // check emptiness
        if (!checkEmptyFields(tf, sb, "Do not leave Phone Number blank.")) {
            return false;
        }
        String phoneNumber = tf.getText().trim();

        // check valid length and format
        if (phoneNumber.length() != 10 && !phoneNumber.matches("^\\+\\d{1,3}[- ]?\\d{10}$")) {
            sb.append("Phone number must be 10 digits long or in the format +[country code]-XXXXXXXXXX!\n");
            tf.setBackground(Color.decode("#FFCDD2"));
            return false;
        }
        tf.setBackground(Color.WHITE);
        return true;
    }

    public static boolean checkValidDOB(JDateChooser dc, StringBuilder sb) {
        Date selectedDate = dc.getDate();

        if (selectedDate == null) {
            sb.append("Please select date of birth!\n");
            dc.setBackground(Color.decode("#FFCDD2"));
            return false;
        }
        // calculate age
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTime(selectedDate);

        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        // Kiểm tra xem ngày sinh đã qua sinh nhật trong năm hiện tại chưa
        // check if employee's birthday overpass their birthday in the current year
        int currentMonth = today.get(Calendar.MONTH);
        int birthMonth = birthDate.get(Calendar.MONTH);
        int currentDayInMonth = today.get(Calendar.DAY_OF_MONTH);
        int birthDayInMonth = birthDate.get(Calendar.DAY_OF_MONTH);

        if (currentMonth <= birthMonth && currentDayInMonth < birthDayInMonth) {
            age--;
        }
        // Kiểm tra tuổi
        if (age < 18 || age > 55) {
            sb.append("Age must be within range 18 - 55\n");
            dc.setBackground(Color.decode("#FFCDD2"));
            return false;
        }
        dc.setBackground(Color.WHITE);
        return true;
    }

    public static void checkContentSpaces(java.awt.event.KeyEvent evt) {
        char keyChar = evt.getKeyChar();

        if (keyChar == java.awt.event.KeyEvent.VK_SPACE) {
            JOptionPane.showMessageDialog(null, "Spaces are not allowed within password/ID", "Input Error", JOptionPane.ERROR_MESSAGE);
            // Consume the event to prevent the space from being typed into the field
            evt.consume();
        }
    }

    public static void printSQLExceptionMessage(SQLException e) {
        StringBuilder errMsg = new StringBuilder();

        errMsg.append("SQL Exception occurred:\n");
        errMsg.append("Message: ").append(e.getMessage()).append("\n");
        errMsg.append("SQL State: ").append(e.getSQLState()).append("\n");
        errMsg.append("Error Code: ").append(e.getErrorCode()).append("\n");

        // Print to console (optional, for debugging)
        System.err.println(errMsg.toString());
        e.printStackTrace();

        // Show in a JOptionPane
        JOptionPane.showMessageDialog(null, errMsg.toString(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean checkNumericFields(JTextField tf, StringBuilder sb) {
        if (!Validator.checkEmptyFields(tf, sb, "Do not leave fields blank!")) {
            return false;
        }

        try {
            String input = tf.getText().trim();
            // Try parsing as an integer
            try {
                Integer.parseInt(input);
            } catch (NumberFormatException e1) {
                // If not an integer, try parsing as a double
                Double.parseDouble(input);
            }
        } catch (NumberFormatException e) {
            sb.append("Those fields must be numeric (integer or decimal)!\n");
            tf.setBackground(Color.decode("#FFCDD2"));
            return false;
        }
        tf.setBackground(Color.WHITE);
        return true;
    }
}
