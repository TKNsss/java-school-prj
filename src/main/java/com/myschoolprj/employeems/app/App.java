package com.myschoolprj.employeems.app;

import com.myschoolprj.employeems.UI.LoginForm;
import com.myschoolprj.employeems.utils.connectDB;

import javax.swing.*;

public class App {

    public static void main(String[] args) {
        try {
            // Set Look-and-Feel to Nimbus
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();  
        }

        // Register shutdown hook to close DB connection when application exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            connectDB.closeConnection();
            System.out.println("Database connection closed on application exit.");
        }));

        // Launch the LoginForm on the EDT
        java.awt.EventQueue.invokeLater(() -> {
            new LoginForm().setVisible(true);  // Start the LoginForm
        });
    }
}
