-- ------------------------------------------------------------
-- Student Management System (MySQL)
-- Database: student_management
-- Table: students
-- ------------------------------------------------------------

CREATE DATABASE IF NOT EXISTS student_management;

USE student_management;

CREATE TABLE IF NOT EXISTS students (
  id INT PRIMARY KEY,
  name VARCHAR(100),
  department VARCHAR(50),
  cgpa FLOAT
);

-- Optional: a few sample rows
-- INSERT INTO students (id, name, department, cgpa) VALUES
-- (1, 'Aisha Khan', 'CSE', 8.75),
-- (2, 'Rahul Mehta', 'ECE', 7.90);
