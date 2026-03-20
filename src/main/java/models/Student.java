/**
 * STUDENT MODEL CLASS (ENTITY CLASS)
 * Purpose: Represents Student entity with properties and behaviors
 * OOP Concepts: Encapsulation, Data Hiding, Java Beans pattern
 * 
 * This class models a Student entity with private fields for data encapsulation.
 * Provides constructors, getters/setters following JavaBeans conventions,
 * and a toString method for easy object representation.
 */
package models;

import java.util.Objects;

public class Student {
    // Private fields for encapsulation - data hiding principle
    private int id;
    private String rollNo;
    private String name;
    private String course;
    private String batch;
    private String contactInfo;
    private String email;
    
    // Default constructor - required for Java Beans pattern and JSON deserialization
    public Student() {}
    
    // Parameterized constructor - for easy object creation from known data
    public Student(String rollNo, String name, String course, String batch, String contactInfo, String email) {
        this.rollNo = rollNo;
        this.name = name;
        this.course = course;
        this.batch = batch;
        this.contactInfo = contactInfo;
        this.email = email;
    }
    
    // Parameterized constructor with ID - for database retrieval
    public Student(int id, String rollNo, String name, String course, String batch, String contactInfo, String email) {
        this.id = id;
        this.rollNo = rollNo;
        this.name = name;
        this.course = course;
        this.batch = batch;
        this.contactInfo = contactInfo;
        this.email = email;
    }
    
    // GETTER AND SETTER METHODS - Encapsulation principle
    // Allows controlled access to private fields
    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }
    
    public String getRollNo() { 
        return rollNo; 
    }
    
    public void setRollNo(String rollNo) { 
        this.rollNo = rollNo; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    public String getCourse() { 
        return course; 
    }
    
    public void setCourse(String course) { 
        this.course = course; 
    }
    
    public String getBatch() { 
        return batch; 
    }
    
    public void setBatch(String batch) { 
        this.batch = batch; 
    }
    
    public String getContactInfo() { 
        return contactInfo; 
    }
    
    public void setContactInfo(String contactInfo) { 
        this.contactInfo = contactInfo; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    // Override toString for better object representation in logs/debugging
    @Override
    public String toString() {
        return String.format("Student[ID=%d, RollNo=%s, Name=%s, Course=%s, Batch=%s, Email=%s]", 
                            id, rollNo, name, course, batch, email);
    }
    
    // equals and hashCode methods for proper object comparison (optional but recommended)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return id == student.id && Objects.equals(rollNo, student.rollNo);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, rollNo);
    }
}
