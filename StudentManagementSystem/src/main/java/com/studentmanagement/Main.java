package com.studentmanagement;

import com.studentmanagement.dao.StudentDAO;
import com.studentmanagement.model.Student;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Entry point of the application.
 *
 * This is a console-based, menu-driven program that calls StudentDAO methods.
 *
 * NOTE:
 * - Main handles user input and output (console)
 * - StudentDAO handles database operations
 */
public class Main {

    private static final StudentDAO studentDAO = new StudentDAO();

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Student Management System (JDBC)     ");
        System.out.println("========================================\n");

        try (Scanner sc = new Scanner(System.in)) {
            boolean running = true;

            while (running) {
                printMenu();
                int choice = readInt(sc, "Enter your choice: ");

                switch (choice) {
                    case 1 -> addStudentFlow(sc);
                    case 2 -> viewStudentsFlow();
                    case 3 -> searchStudentFlow(sc);
                    case 4 -> updateStudentFlow(sc);
                    case 5 -> deleteStudentFlow(sc);
                    case 6 -> {
                        running = false;
                        System.out.println("Exiting... Goodbye!");
                    }
                    default -> System.out.println("Invalid choice. Please select 1-6.");
                }

                System.out.println();
            }
        }
    }

    private static void printMenu() {
        System.out.println("1. Add Student");
        System.out.println("2. View All Students");
        System.out.println("3. Search Student by ID");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
        System.out.println("6. Exit");
    }

    // ------------------------------
    // Menu flows
    // ------------------------------

    private static void addStudentFlow(Scanner sc) {
        System.out.println("\n--- Add Student ---");

        int id = readInt(sc, "Student ID: ");
        String name = readNonBlank(sc, "Name: ");
        String dept = readNonBlank(sc, "Department: ");
        double cgpa = readDouble(sc, "CGPA (0-10): ", 0, 10);

        Student student = new Student(id, name, dept, cgpa);

        try {
            boolean inserted = studentDAO.addStudent(student);
            System.out.println(inserted ? "Student added successfully." : "Student was not added.");
        } catch (IllegalArgumentException e) {
            // Example: Duplicate ID
            System.out.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database error while adding student: " + e.getMessage());
        }
    }

    private static void viewStudentsFlow() {
        System.out.println("\n--- View All Students ---");

        try {
            List<Student> students = studentDAO.viewStudents();

            if (students.isEmpty()) {
                System.out.println("No students found.");
                return;
            }

            for (Student s : students) {
                System.out.println(s);
            }

        } catch (SQLException e) {
            System.out.println("Database error while fetching students: " + e.getMessage());
        }
    }

    private static void searchStudentFlow(Scanner sc) {
        System.out.println("\n--- Search Student by ID ---");

        int id = readInt(sc, "Enter Student ID: ");

        try {
            Optional<Student> student = studentDAO.searchStudent(id);
            if (student.isPresent()) {
                System.out.println("Found: " + student.get());
            } else {
                System.out.println("Student with ID " + id + " not found.");
            }

        } catch (SQLException e) {
            System.out.println("Database error while searching: " + e.getMessage());
        }
    }

    private static void updateStudentFlow(Scanner sc) {
        System.out.println("\n--- Update Student ---");

        int id = readInt(sc, "Enter Student ID to update: ");

        try {
            Optional<Student> existing = studentDAO.searchStudent(id);
            if (existing.isEmpty()) {
                System.out.println("Student with ID " + id + " not found.");
                return;
            }

            System.out.println("Current record: " + existing.get());

            String name = readNonBlank(sc, "New Name: ");
            String dept = readNonBlank(sc, "New Department: ");
            double cgpa = readDouble(sc, "New CGPA (0-10): ", 0, 10);

            Student updated = new Student(id, name, dept, cgpa);
            boolean ok = studentDAO.updateStudent(updated);

            System.out.println(ok ? "Student updated successfully." : "Update failed.");

        } catch (SQLException e) {
            System.out.println("Database error while updating: " + e.getMessage());
        }
    }

    private static void deleteStudentFlow(Scanner sc) {
        System.out.println("\n--- Delete Student ---");

        int id = readInt(sc, "Enter Student ID to delete: ");

        try {
            boolean ok = studentDAO.deleteStudent(id);
            System.out.println(ok ? "Student deleted successfully." : "Student with ID " + id + " not found.");

        } catch (SQLException e) {
            System.out.println("Database error while deleting: " + e.getMessage());
        }
    }

    // ------------------------------
    // Input helpers (avoid Scanner nextInt/nextDouble pitfalls)
    // ------------------------------

    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static double readDouble(Scanner sc, String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            try {
                double value = Double.parseDouble(input);
                if (value < min || value > max) {
                    System.out.println("Please enter a value between " + min + " and " + max + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readNonBlank(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = sc.nextLine().trim();
            if (!value.isBlank()) {
                return value;
            }
            System.out.println("This field cannot be empty.");
        }
    }
}
