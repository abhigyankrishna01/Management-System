package com.studentmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) for Student.
 *
 * Responsibilities:
 * - Contains all SQL operations for the students table
 * - Uses PreparedStatement everywhere (prevents SQL injection)
 * - Returns simple Java objects (Student) to the caller
 */
public class StudentDAO {

    /**
     * Inserts a new student row.
     *
     * @param student student to insert
     * @return true if inserted, false otherwise
     * @throws SQLException when a database error occurs
     * @throws IllegalArgumentException for duplicate ID or invalid data
     */
    public boolean addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students (id, name, department, cgpa) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, student.getId());
            ps.setString(2, student.getName());
            ps.setString(3, student.getDepartment());
            ps.setDouble(4, student.getCgpa());

            return ps.executeUpdate() == 1;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Most common: duplicate primary key ID
            throw new IllegalArgumentException("A student with ID " + student.getId() + " already exists.", e);
        }
    }

    /**
     * Reads all students.
     */
    public List<Student> viewStudents() throws SQLException {
        String sql = "SELECT id, name, department, cgpa FROM students ORDER BY id";
        List<Student> students = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Student s = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getDouble("cgpa")
                );
                students.add(s);
            }
        }

        return students;
    }

    /**
     * Finds a student by ID.
     */
    public Optional<Student> searchStudent(int id) throws SQLException {
        String sql = "SELECT id, name, department, cgpa FROM students WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Student s = new Student(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("department"),
                            rs.getDouble("cgpa")
                    );
                    return Optional.of(s);
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Updates an existing student (matched by ID).
     *
     * @return true if exactly one row was updated
     */
    public boolean updateStudent(Student student) throws SQLException {
        String sql = "UPDATE students SET name = ?, department = ?, cgpa = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getName());
            ps.setString(2, student.getDepartment());
            ps.setDouble(3, student.getCgpa());
            ps.setInt(4, student.getId());

            return ps.executeUpdate() == 1;
        }
    }

    /**
     * Deletes a student by ID.
     *
     * @return true if deleted, false if the ID did not exist
     */
    public boolean deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }
}
