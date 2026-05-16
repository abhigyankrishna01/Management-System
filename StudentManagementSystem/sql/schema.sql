-- ------------------------------------------------------------
-- Student Management System (MySQL)
-- Database: student_management
-- Table: students
-- ------------------------------------------------------------

CREATE DATABASE IF NOT EXISTS student_management;

USE student_management;

CREATE TABLE IF NOT EXISTS students (
  id INT NOT NULL,
  name VARCHAR(100) NOT NULL,
  department VARCHAR(100) NOT NULL,
  cgpa DECIMAL(4,2) NOT NULL,
  PRIMARY KEY (id),
  CHECK (cgpa >= 0 AND cgpa <= 10)
);

-- Optional: a few sample rows
-- INSERT INTO students (id, name, department, cgpa) VALUES
-- (1, 'Aisha Khan', 'CSE', 8.75),
-- (2, 'Rahul Mehta', 'ECE', 7.90);
