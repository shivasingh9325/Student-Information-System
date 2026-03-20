/**
 * STUDENT SERVLET CLASS (CONTROLLER)
 * Purpose: Handles HTTP requests and responses, acts as controller in MVC
 * OOP Concepts: Servlet lifecycle, request handling, JSON response generation
 *
 * This servlet provides RESTful API endpoints for student management.
 * It handles CRUD operations via HTTP methods and returns JSON responses.
 * Uses Jackson for JSON serialization/deserialization.
 */
package controllers;

import dao.StudentDAO;
import models.Student;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/students/*") // URL mapping for all student operations
public class StudentServlet extends HttpServlet {
    private StudentDAO studentDAO;
    private ObjectMapper objectMapper;
    
    /**
     * SERVLET INIT METHOD - Called once when servlet is first loaded
     * Purpose: Initialize resources (DAO, ObjectMapper)
     */
    @Override
    public void init() throws ServletException {
        try {
            this.studentDAO = new StudentDAO();
            this.objectMapper = new ObjectMapper();
            System.out.println("StudentServlet initialized successfully!");
        } catch (Exception e) {
            System.err.println("StudentServlet initialization failed: " + e.getMessage());
            throw new ServletException("Initialization error", e);
        }
    }
    
    /**
     * HANDLE GET REQUESTS - For retrieval operations
     * Supports: Get all students, search, get by roll number, reports
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String pathInfo = request.getPathInfo(); // Get URL path after /students/
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /students - Get all students
                List<Student> students = studentDAO.getAllStudents();
                sendJsonResponse(response, students);
                
            } else if (pathInfo.equals("/search")) {
                // GET /students/search?q=term - Search students
                String searchTerm = request.getParameter("q");
                if (searchTerm == null || searchTerm.trim().isEmpty()) {
                    sendError(response, "Search term is required", 400);
                    return;
                }
                List<Student> students = studentDAO.searchStudents(searchTerm.trim());
                sendJsonResponse(response, students);
                
            } else if (pathInfo.equals("/report")) {
                // GET /students/report - Generate course-wise report
                Map<String, Integer> report = studentDAO.getCourseWiseReport();
                sendJsonResponse(response, report);
                
            } else {
                // GET /students/rollNo - Get specific student
                String rollNo = pathInfo.substring(1); // Remove leading slash
                Student student = studentDAO.getStudentByRollNo(rollNo);
                if (student != null) {
                    sendJsonResponse(response, student);
                } else {
                    sendError(response, "Student not found", 404);
                }
            }
        } catch (Exception e) {
            sendError(response, "Server error: " + e.getMessage(), 500);
        }
    }
    
    /**
     * HANDLE POST REQUESTS - For creation operations
     * Supports: Add new student
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Parse JSON request body to Student object
            Student student = objectMapper.readValue(request.getReader(), Student.class);
            
            // Validate required fields
            if (student.getRollNo() == null || student.getRollNo().trim().isEmpty() ||
                student.getName() == null || student.getName().trim().isEmpty() || 
                student.getCourse() == null || student.getCourse().trim().isEmpty() || 
                student.getBatch() == null || student.getBatch().trim().isEmpty()) {
                sendError(response, "Missing required fields: rollNo, name, course, batch", 400);
                return;
            }
            
            // Check if student already exists
            if (studentDAO.getStudentByRollNo(student.getRollNo()) != null) {
                sendError(response, "Student with this roll number already exists", 409);
                return;
            }
            
            // Add student to database
            boolean success = studentDAO.addStudent(student);
            if (success) {
                sendJsonResponse(response, Map.of("message", "Student added successfully", "success", true));
            } else {
                sendError(response, "Failed to add student", 500);
            }
            
        } catch (Exception e) {
            sendError(response, "Invalid request data: " + e.getMessage(), 400);
        }
    }
    
    /**
     * HANDLE PUT REQUESTS - For update operations
     * Supports: Update existing student
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Parse JSON request body
            Student student = objectMapper.readValue(request.getReader(), Student.class);
            
            // Validate required fields
            if (student.getRollNo() == null || student.getRollNo().trim().isEmpty()) {
                sendError(response, "Roll number is required for update", 400);
                return;
            }
            
            // Update student in database
            boolean success = studentDAO.updateStudent(student);
            if (success) {
                sendJsonResponse(response, Map.of("message", "Student updated successfully", "success", true));
            } else {
                sendError(response, "Student not found or update failed", 404);
            }
            
        } catch (Exception e) {
            sendError(response, "Invalid request data: " + e.getMessage(), 400);
        }
    }
    
    /**
     * HANDLE DELETE REQUESTS - For deletion operations
     * Supports: Delete student by roll number
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            sendError(response, "Roll number is required for deletion", 400);
            return;
        }
        
        try {
            String rollNo = pathInfo.substring(1); // Remove leading slash
            
            // Delete student from database
            boolean success = studentDAO.deleteStudent(rollNo);
            if (success) {
                sendJsonResponse(response, Map.of("message", "Student deleted successfully", "success", true));
            } else {
                sendError(response, "Student not found or deletion failed", 404);
            }
            
        } catch (Exception e) {
            sendError(response, "Server error: " + e.getMessage(), 500);
        }
    }
    
    /**
     * HELPER METHOD: Send JSON response
     * Converts Java objects to JSON and sends HTTP response
     */
    private void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
        String jsonResponse = objectMapper.writeValueAsString(data);
        response.getWriter().write(jsonResponse);
    }
    
    /**
     * HELPER METHOD: Send error response
     * Standardized error response format
     */
    private void sendError(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        Map<String, Object> error = Map.of(
            "success", false,
            "message", message,
            "status", statusCode
        );
        sendJsonResponse(response, error);
    }
    
    /**
     * SERVLET DESTROY METHOD - Called when servlet is unloaded
     * Purpose: Clean up resources
     */
    @Override
    public void destroy() {
        System.out.println("StudentServlet destroyed");
        // Database connection is managed by DatabaseConfig singleton
        // Close connection if needed: DatabaseConfig.closeConnection();
    }
}
