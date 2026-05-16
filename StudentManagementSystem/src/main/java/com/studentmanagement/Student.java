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
    private double cgpa;

    /**
     * Default constructor (needed sometimes by frameworks/tools).
     */
    public Student() {
    }

    /**
     * Convenience constructor to create a fully populated Student.
     */
    public Student(int id, String name, String department, double cgpa) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.cgpa = cgpa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
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
