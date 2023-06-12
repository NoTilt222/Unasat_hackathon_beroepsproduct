package databasemanager;

import java.sql.*;  // Importing everything(*) is bad practice which causes bloatware but you should onlu import the necessary classes from the package. In this case it's java.sql

public class    DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/db_name";  // Declare a private static final variable named URL that contains the URL to connect to the database.
    private static final String USERNAME = "root";  // Declare a private static final variable named USERNAME that contains the username to connect to the database.
    private static final String PASSWORD = "password";  // Declare a private static final variable named PASSWORD that contains the password to connect to the database.

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);  // Open a new database connection using the URL, username, and password.
    }
}
