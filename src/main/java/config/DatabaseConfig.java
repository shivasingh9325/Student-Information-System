/**
 * DATABASE CONFIGURATION CLASS
 * Purpose: Manages database connection parameters and connection pooling
 * OOP Concept: Singleton pattern for single database instance
 * 
 * This class provides a centralized way to manage MySQL database connections.
 * It uses lazy initialization to create the connection only when first requested.
 * Automatically creates the required 'students' table on first connection.
 */
package config;

import java.sql.*;

public class DatabaseConfig {
    // Database connection parameters - H2 in-memory/file database
    private static final String URL = "jdbc:h2:~/student_db;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";
    private static Connection connection = null;
    
    /**
     * Singleton pattern to ensure single database connection instance
     * @return Database Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        // Lazy initialization - create connection only when needed
        if (connection == null || connection.isClosed()) {
            try {
                // Load H2 JDBC driver
                Class.forName("org.h2.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Database connected successfully!");
                createTables(); // Ensure tables exist
            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
                throw new SQLException("Driver not found", e);
            } catch (SQLException e) {
                System.err.println("Database connection failed: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }
    
    /**
     * Creates required tables if they don't exist
     * Ensures application works without manual database setup
     * Table: students - Stores student information with auto-increment ID
     */
    private static void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS students (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "roll_no VARCHAR(20) UNIQUE NOT NULL, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "course VARCHAR(50) NOT NULL, " +
                    "batch VARCHAR(20) NOT NULL, " +
                    "contact_info VARCHAR(15), " +
                    "email VARCHAR(100)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Students table ready!");
        } catch (SQLException e) {
            System.err.println("Table creation error: " + e.getMessage());
        }
    }
    
    /**
     * Closes the database connection (called on application shutdown if needed)
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
