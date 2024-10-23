package com.myschoolprj.employeems.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class connectDB {
    private static Connection connection;
    
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Load the properties from the database configuration file
                Properties props = new Properties();
                
                props.load(connectDB.class.getResourceAsStream("/database.properties"));
                
                String url = props.getProperty("URL");
                String user = props.getProperty("USER_NAME");
                String password = props.getProperty("PASSWORD");
                
                // Create the database connection using the properties
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Database connection established.");
            } catch (SQLException e) {
                System.out.println("SQL Exception: " + e.getMessage()); 
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
    
    //  close the connection when the application shuts down
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}