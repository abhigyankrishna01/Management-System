package com.studentmanagement;

import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLTransientConnectionException;
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
        } finally {
            // Close the shared DB connection cleanly on exit.
            DBConnection.closeConnection();
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

        int id = readPositiveInt(sc, "Student ID: ");
        String name = readNonBlank(sc, "Name: ");
        String dept = readNonBlank(sc, "Department: ");
        float cgpa = readFloat(sc, "CGPA (0-10): ", 0, 10);

        try {
            Student student = new Student(id, name, dept, cgpa);
            boolean inserted = studentDAO.addStudent(student);
            System.out.println(inserted ? "Student added successfully." : "Student was not added.");
        } catch (IllegalArgumentException e) {
            // Example: Duplicate ID
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            printDbConfigurationError(e);
        } catch (SQLException e) {
            printSqlError("adding student", e);
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

        } catch (IllegalStateException e) {
            printDbConfigurationError(e);
        } catch (SQLException e) {
            printSqlError("fetching students", e);
        }
    }

    private static void searchStudentFlow(Scanner sc) {
        System.out.println("\n--- Search Student by ID ---");

        int id = readPositiveInt(sc, "Enter Student ID: ");

        try {
            Optional<Student> student = studentDAO.searchStudent(id);
            if (student.isPresent()) {
                System.out.println("Found: " + student.get());
            } else {
                System.out.println("Student with ID " + id + " not found.");
            }

        } catch (IllegalStateException e) {
            printDbConfigurationError(e);
        } catch (SQLException e) {
            printSqlError("searching student", e);
        }
    }

    private static void updateStudentFlow(Scanner sc) {
        System.out.println("\n--- Update Student ---");

        int id = readPositiveInt(sc, "Enter Student ID to update: ");

        try {
            Optional<Student> existing = studentDAO.searchStudent(id);
            if (existing.isEmpty()) {
                System.out.println("Student with ID " + id + " not found.");
                return;
            }

            System.out.println("Current record: " + existing.get());

            String name = readNonBlank(sc, "New Name: ");
            String dept = readNonBlank(sc, "New Department: ");
            float cgpa = readFloat(sc, "New CGPA (0-10): ", 0, 10);

            Student updated = new Student(id, name, dept, cgpa);
            boolean ok = studentDAO.updateStudent(updated);

            System.out.println(ok ? "Student updated successfully." : "Update failed.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            printDbConfigurationError(e);
        } catch (SQLException e) {
            printSqlError("updating student", e);
        }
    }

    private static void deleteStudentFlow(Scanner sc) {
        System.out.println("\n--- Delete Student ---");

        int id = readPositiveInt(sc, "Enter Student ID to delete: ");

        try {
            boolean ok = studentDAO.deleteStudent(id);
            System.out.println(ok ? "Student deleted successfully." : "Student with ID " + id + " not found.");

        } catch (IllegalStateException e) {
            printDbConfigurationError(e);
        } catch (SQLException e) {
            printSqlError("deleting student", e);
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

    private static int readPositiveInt(Scanner sc, String prompt) {
        while (true) {
            int value = readInt(sc, prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Please enter a positive integer (> 0). ");
        }
    }

    private static float readFloat(Scanner sc, String prompt, float min, float max) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            try {
                float value = Float.parseFloat(input);
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

    // ------------------------------
    // Error helpers
    // ------------------------------

    private static void printDbConfigurationError(IllegalStateException e) {
        System.out.println("Database configuration error: " + e.getMessage());
        System.out.println("Fix: Check your .env values for DB_URL, DB_USER, DB_PASSWORD.");
        System.out.println("Also ensure the MySQL server is running and the database exists: student_management");
    }

    private static void printSqlError(String action, SQLException e) {
        if (e == null) {
            System.out.println("Database error while " + action + ".");
            return;
        }

        // These two are common when the DB server is down / cannot be reached.
        if (e instanceof SQLNonTransientConnectionException || e instanceof SQLTransientConnectionException) {
            System.out.println("Database unavailable while " + action + ".");
            System.out.println("Fix: Start MySQL server, verify DB_URL/DB_USER/DB_PASSWORD, and ensure the database exists.");
        } else {
            System.out.println("Database error while " + action + ".");
        }

        // Helpful diagnostics for debugging.
        System.out.println("Details: " + String.valueOf(e.getMessage()));
        if (e.getSQLState() != null) {
            System.out.println("SQLState: " + e.getSQLState() + " | ErrorCode: " + e.getErrorCode());
        }
    }
}
