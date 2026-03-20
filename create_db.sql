-- SQL Script to create the student database
-- Run this script in MySQL before starting the application

CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

-- The 'students' table will be created automatically by the application
-- If you want to add sample data, uncomment the following:

-- INSERT INTO students (roll_no, name, course, batch, contact_info, email) VALUES
-- ('CS001', 'John Doe', 'Computer Engineering', '2023', '1234567890', 'john@example.com'),
-- ('IT001', 'Jane Smith', 'IT Engineering', '2023', '0987654321', 'jane@example.com');
