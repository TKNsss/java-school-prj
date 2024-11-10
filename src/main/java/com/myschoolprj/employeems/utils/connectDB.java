package com.myschoolprj.employeems.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class connectDB {

    private static Connection connection = null;
    
    /*
        The Singleton pattern or pattern singleton incorporates a private constructor, which serves as a 
        barricade against external attempts to create instances of the Singleton class. This ensures that 
        the class has control over its instantiation process.
    */ 
    private connectDB() {} // Private constructor to prevent instantiation

    // use static to call it without instantiating the class
    public static Connection getConnection() throws SQLException {
        // Check if connection already exists (singleton pattern)
        if (connection == null || connection.isClosed()) {
            try {
                Properties props = new Properties();

                // Load database configuration properties
                try (var inputStream = connectDB.class.getResourceAsStream("/database.properties")) {
                    if (inputStream == null) {
                        throw new SQLException("Database configuration file not found.");
                    }
                    props.load(inputStream);
                }
                String url = props.getProperty("DB_URL");
                String user = props.getProperty("USER");
                String password = props.getProperty("PASSWORD");

                // Attempt to establish a connection
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Database connection established");
            } catch (Exception e) {
                throw new SQLException("Failed to establish a database connection: " + e.getMessage(), e);
            }
        }
        return connection;
    }

    // Close the connection when the application shuts down
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing the database connection: " + e.getMessage());
            }
        }
    }
}
