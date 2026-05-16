package com.studentmanagement;

/**
 * Represents a Student entity in our system.
 *
 * This class is a simple POJO (Plain Old Java Object) used to transfer data
 * between layers (Main -> DAO -> Database).
 */
public class Student {

    private int id;
    private String name;
    private String department;
    private float cgpa;

    /**
     * Default constructor (needed sometimes by frameworks/tools).
     */
    public Student() {
    }

    /**
     * Convenience constructor to create a fully populated Student.
     */
    public Student(int id, String name, String department, float cgpa) {
        if (id <= 0) {
            throw new IllegalArgumentException("Student ID must be a positive integer.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (department == null || department.isBlank()) {
            throw new IllegalArgumentException("Department cannot be empty.");
        }
        if (cgpa < 0.0f || cgpa > 10.0f) {
            throw new IllegalArgumentException("CGPA must be between 0 and 10.");
        }

        this.id = id;
        this.name = name;
        this.department = department;
        this.cgpa = cgpa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Student ID must be a positive integer.");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        if (department == null || department.isBlank()) {
            throw new IllegalArgumentException("Department cannot be empty.");
        }
        this.department = department;
    }

    public float getCgpa() {
        return cgpa;
    }

    public void setCgpa(float cgpa) {
        if (cgpa < 0.0f || cgpa > 10.0f) {
            throw new IllegalArgumentException("CGPA must be between 0 and 10.");
        }
        this.cgpa = cgpa;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", cgpa=" + cgpa +
                '}';
    }
}
