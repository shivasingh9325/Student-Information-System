/**
 * DATA ACCESS OBJECT (DAO) CLASS
 * Purpose: Handles all database operations (CRUD) for Student entity
 * OOP Concepts: Separation of Concerns, Data Abstraction
 *
 * This class abstracts database operations, providing a clean interface
 * for the business logic layer. It encapsulates SQL queries and database
 * interactions, promoting separation of concerns.
 */
package dao;

import config.DatabaseConfig;
import models.Student;
import java.sql.*;
import java.util.*;

public class StudentDAO {
    private Connection connection;
    
    // Constructor initializes database connection using singleton DatabaseConfig
    public StudentDAO() {
        try {
            this.connection = DatabaseConfig.getConnection();
        } catch (SQLException e) {
            System.err.println("Failed to initialize StudentDAO: " + e.getMessage());
            throw new RuntimeException("Database connection error", e);
        }
    }
    
    /**
     * OPERATION 1: CREATE - Add new student to database
     * @param student Student object to be added
     * @return boolean indicating success/failure
     */
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (roll_no, name, course, batch, contact_info, email) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Parameter binding to prevent SQL injection
            stmt.setString(1, student.getRollNo());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getCourse());
            stmt.setString(4, student.getBatch());
            stmt.setString(5, student.getContactInfo());
            stmt.setString(6, student.getEmail());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Add Student Error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * OPERATION 2: UPDATE - Modify existing student record
     * @param student Student object with updated data
     * @return boolean indicating success/failure
     */
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET name=?, course=?, batch=?, contact_info=?, email=? WHERE roll_no=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getCourse());
            stmt.setString(3, student.getBatch());
            stmt.setString(4, student.getContactInfo());
            stmt.setString(5, student.getEmail());
            stmt.setString(6, student.getRollNo());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Update Student Error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * OPERATION 3: DELETE - Remove student from database
     * @param rollNo Roll number of student to delete
     * @return boolean indicating success/failure
     */
    public boolean deleteStudent(String rollNo) {
        String sql = "DELETE FROM students WHERE roll_no=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, rollNo);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Delete Student Error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * OPERATION 4: SEARCH - Find students by roll number or name
     * @param searchTerm Search keyword for roll number or name
     * @return List of matching students
     */
    public List<Student> searchStudents(String searchTerm) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE roll_no LIKE ? OR name LIKE ? ORDER BY name";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String pattern = "%" + searchTerm + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                students.add(extractStudent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Search Students Error: " + e.getMessage());
        }
        return students;
    }
    
    /**
     * OPERATION 5: READ - Get student by roll number
     * @param rollNo Roll number to search for
     * @return Student object if found, null otherwise
     */
    public Student getStudentByRollNo(String rollNo) {
        String sql = "SELECT * FROM students WHERE roll_no = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, rollNo);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractStudent(rs);
            }
        } catch (SQLException e) {
            System.err.println("Get Student Error: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * OPERATION 6: READ - Get all students from database
     * @return List of all students
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY roll_no";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                students.add(extractStudent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Get All Students Error: " + e.getMessage());
        }
        return students;
    }
    
    /**
     * OPERATION 7: REPORT - Generate course-wise student count
     * @return Map with course names and student counts
     */
    public Map<String, Integer> getCourseWiseReport() {
        Map<String, Integer> report = new HashMap<>();
        String sql = "SELECT course, COUNT(*) as count FROM students GROUP BY course ORDER BY course";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                report.put(rs.getString("course"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            System.err.println("Course Report Error: " + e.getMessage());
        }
        return report;
    }
    
    /**
     * HELPER METHOD: Extract Student object from ResultSet
     * Reduces code duplication in multiple methods
     * Maps database columns to Student object properties
     */
    private Student extractStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setRollNo(rs.getString("roll_no"));
        student.setName(rs.getString("name"));
        student.setCourse(rs.getString("course"));
        student.setBatch(rs.getString("batch"));
        student.setContactInfo(rs.getString("contact_info"));
        student.setEmail(rs.getString("email"));
        return student;
    }
}
