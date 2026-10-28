package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

public class Main {
    static final String URL = "jdbc:mysql://localhost:3306/jdbc";
    static final String USER = "root";
    static final String PASSWORD = System.getenv("DB_PASSWORD");
    // Database connection
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // INSERT: Add student details


    public static void insertStudent(
            String name, int age, String gender, String aadhar) {

        String sql = "INSERT INTO student_table(name, age, gender, aadhar) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection cn = getConnection();
             java.sql.PreparedStatement pstmt = cn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, gender);
            pstmt.setString(4, aadhar);

            pstmt.executeUpdate();

            System.out.println(name + " inserted successfully!");

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            System.out.println(
                    "Cannot insert " + name
                            + ": Aadhaar number already exists."
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // READ: Display all student details
    public static void displayStudents() throws Exception {

        Connection cn = getConnection();
        Statement stmt = cn.createStatement();

        ResultSet rs = stmt.executeQuery(
                "SELECT * FROM student_table"
        );

        System.out.println("\n----- Student Details -----");

        while (rs.next()) {
            System.out.println(
                    "ID: " + rs.getInt("id")
                            + ", Name: " + rs.getString("name")
                            + ", Age: " + rs.getInt("age")
                            + ", Gender: " + rs.getString("gender")
                            + ", Aadhaar: " + rs.getString("aadhar")
            );
        }

        rs.close();
        stmt.close();
        cn.close();
    }

    // UPDATE: Update student age using ID
    public static void updateStudent(int id, int age)
            throws Exception {

        Connection cn = getConnection();
        Statement stmt = cn.createStatement();

        String sql = "UPDATE student_table SET age = "
                + age + " WHERE id = " + id;

        int rows = stmt.executeUpdate(sql);

        System.out.println(rows + " student record(s) updated.");

        stmt.close();
        cn.close();
    }

    // DELETE: Delete student using ID
    public static void deleteStudent(int id) throws Exception {

        Connection cn = getConnection();
        Statement stmt = cn.createStatement();

        String sql = "DELETE FROM student_table WHERE id = " + id;

        int rows = stmt.executeUpdate(sql);

        System.out.println(rows + " student record(s) deleted.");

        stmt.close();
        cn.close();
    }

    // MAIN METHOD
    public static void main(String[] args) {

        try {

            // 1. INSERT
            insertStudent("Keerthi", 21, "Female","8919485");
            insertStudent("shivamani", 8, "Male", "9665963");
            insertStudent("mounika", 18, "Female", "9010561");
            insertStudent("Raju", 30, "Male", "9057671");

            // 2. READ
            System.out.println("\nBefore Updating:");
            displayStudents();

            // 3. UPDATE: Change age of student ID 1
            updateStudent(1, 23);

            System.out.println("\nAfter Updating:");
            displayStudents();

            // 4. DELETE: Delete student ID 2
            deleteStudent(2);

            System.out.println("\nAfter Deleting:");
            displayStudents();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}