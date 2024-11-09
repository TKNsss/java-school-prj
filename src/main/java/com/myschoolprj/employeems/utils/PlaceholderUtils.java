package com.myschoolprj.employeems.utils;

import java.awt.Color;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class PlaceholderUtils {
    public static void handleFocusedPlaceholder(JTextField tf, String placeholderText) {
        if (tf instanceof JPasswordField) {
            JPasswordField pf = (JPasswordField) tf;

            // Handle Focus Gained (when field contains placeholder text)
            if (new String(pf.getPassword()).equals(placeholderText)) {
                pf.setText(null);
                pf.requestFocus();
                pf.setForeground(Color.BLACK);
                pf.setEchoChar('*');
            } else if (pf.getPassword().length == 0) {
                // Handle Focus Lost (when field is empty)
                pf.setText(placeholderText);
                pf.setForeground(new Color(153, 153, 153));
                pf.setEchoChar('\u0000');  // Clear echo character
            }
            return;
        }

        // Handle JTextField Focus Gained and Lost
        if (tf.getText().equals(placeholderText)) {
            tf.setText(null);
            tf.requestFocus();
            tf.setForeground(Color.BLACK);
        } else if (tf.getText().length() == 0) {
            tf.setText(placeholderText);
            tf.setForeground(new Color(153, 153, 153));
        }
    }
}