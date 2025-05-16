package ExpLearning;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    static final String DB_URL = "jdbc:mysql://localhost:3307/hospital_db";
    static final String USER = "root"; // change if needed
    static final String PASS = "ritesh";     // change if needed

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            int choice;

            do {
                System.out.println("\n=== Hospital Management System ===");
                System.out.println("1. Add Patient");
                System.out.println("2. Add Doctor");
                System.out.println("3. Schedule Appointment");
                System.out.println("4. View Appointments");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1 -> addPatient(conn);
                    case 2 -> addDoctor(conn);
                    case 3 -> scheduleAppointment(conn);
                    case 4 -> viewAppointments(conn);
                    case 5 -> System.out.println("Exiting...");
                    default -> System.out.println("Invalid choice.");
                }
            } while (choice != 5);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void addPatient(Connection conn) throws SQLException {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter age: ");
        int age = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter gender: ");
        String gender = sc.nextLine();

        String sql = "INSERT INTO patients (name, age, gender) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, gender);
            stmt.executeUpdate();
            System.out.println("Patient added successfully.");
        }
    }

    static void addDoctor(Connection conn) throws SQLException {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter age: ");
        int age = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter gender: ");
        String gender = sc.nextLine();
        System.out.print("Enter specialization: ");
        String specialization = sc.nextLine();

        String sql = "INSERT INTO doctors (name, age, gender, specialization) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, gender);
            stmt.setString(4, specialization);
            stmt.executeUpdate();
            System.out.println("Doctor added successfully.");
        }
    }

    static void scheduleAppointment(Connection conn) throws SQLException {
        System.out.print("Enter doctor ID: ");
        int docId = sc.nextInt();
        System.out.print("Enter patient ID: ");
        int patId = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter appointment date (dd-mm-yyyy): ");
        String date = sc.nextLine();

        String sql = "INSERT INTO appointments (doctor_id, patient_id, date) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, docId);
            stmt.setInt(2, patId);
            stmt.setString(3, date);
            stmt.executeUpdate();
            System.out.println("Appointment scheduled successfully.");
        }
    }

    static void viewAppointments(Connection conn) throws SQLException {
        String sql = """
            SELECT a.appointment_id, a.date, d.name AS doctor_name, p.name AS patient_name
            FROM appointments a
            JOIN doctors d ON a.doctor_id = d.doctor_id
            JOIN patients p ON a.patient_id = p.patient_id
            """;

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("Appointment ID: %d | Date: %s | Doctor: %s | Patient: %s%n",
                        rs.getInt("appointment_id"),
                        rs.getString("date"),
                        rs.getString("doctor_name"),
                        rs.getString("patient_name"));
            }
        }
    }
}
