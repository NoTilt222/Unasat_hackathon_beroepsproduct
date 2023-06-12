import databasemanager.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Update {
    public void UpdateInformation() {
        try (Connection conn = DatabaseConnection.getConnection()) { // Get a connection to the database
            Scanner scanner = new Scanner(System.in); // Creating a Scanner object for user input
            int student_id; // Initializing a variable to store the student id

            while (true) { // Loop to prompt for the student number until a valid student is found
                Scanner sc = new Scanner(System.in); // Creating a new Scanner object for user input
                System.out.print("\033[38;5;32m" + "Student ID: " + "\033[0m"); // Prompting the user for the student id
                student_id = Integer.parseInt(sc.next()); // Reading the student id from the user input

                // SQL query to check if the student exists in the database
                String sql = "SELECT student.student_id\n" +
                        "FROM team\n" +
                        "LEFT JOIN student ON team.team_id = student.team_id\n" +
                        "LEFT JOIN contact_gegevens ON student.contact_id = contact_gegevens.contact_id\n" +
                        "WHERE student.student_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql); // Creating a PreparedStatement object with the SQL query
                stmt.setString(1, String.valueOf(student_id)); // Setting the student number parameter in the prepared statement
                ResultSet rs = stmt.executeQuery(); // Executing the query and storing the result in a ResultSet

                if (rs.next()) { // Checking if a student record was found
                    break; // Student exists, break out of the loop
                } else {
                    System.out.println("\u001B[31mStudent does not exist!\u001B[0m"); // Printing an error message if the student does not exist
                }
            }

            while (true) { // Loop to prompt for the update choice until the user chooses to return
                System.out.print("\033[38;5;208m" + "What do you want to update:" + "\033[0m"); // Prompting the user for the update choice
                System.out.println("\033[0m");
                System.out.print("\033[38;5;208m" + "| " + "\033[0m"); //Print a separator in orange
                System.out.print("\033[38;5;32m" + "Team Name" + "\033[0m"); //Make the option blue
                System.out.print("\033[38;5;208m" + " | " + "\033[0m"); //Print a separator in orange
                System.out.print("\033[38;5;32m" + "Skill" + "\033[0m"); //Make the option blue
                System.out.print("\033[38;5;208m" + " | " + "\033[0m"); //Print a separator in orange
                System.out.print("\033[38;5;32m" + "Residence" + "\033[0m"); //Make the option blue
                System.out.print("\033[38;5;208m" + " | " + "\033[0m"); //Print a separator in orange
                System.out.print("\033[38;5;32m" + "Contact Number" + "\033[0m"); //Make the option blue
                System.out.print("\033[38;5;208m" + " | " + "\033[0m"); //Print a separator in orange
                System.out.print("\033[38;5;32m" + "Student Team ID" + "\033[0m"); //Make the option blue
                System.out.print("\033[38;5;208m" + " | " + "\033[0m"); //Print a separator in orange
                System.out.print("\033[38;5;32m" + "Return" + "\033[0m"); //Make the option blue
                System.out.print("\033[38;5;208m" + " | " + "\033[0m"); //Print a separator in orange
                System.out.println(); // Printing the update options to the console
                String choice = scanner.nextLine().toLowerCase(); // Reading the user's choice

                if (choice.equals("return")) { // Checking if the user chose to return
                    System.out.println("\nReturning to menu...\n"); // Printing a message and returning to the menu
                    return;
                }

                String column = ""; // Variable to hold the database column to update
                String columnDisplay = ""; // Variable to hold the formatted column value

                switch (choice) { // Determining the column to update based on the user's choice
                    case "team name":
                        column = "team.team_naam";
                        columnDisplay = "\033[38;5;32m" + "team name" + "\033[0m";
                        break;
                    case "skill":
                        column = "student.vaardigheid";
                        columnDisplay = "\033[38;5;32m" + "skill" + "\033[0m";
                        break;
                    case "residence":
                        column = "contact_gegevens.verblijfplaats";
                        columnDisplay = "\033[38;5;32m" + "residence" + "\033[0m";
                        break;
                    case "contact number":
                        column = "contact_gegevens.contact_nummer";
                        columnDisplay = "\033[38;5;32m" + "contact number" + "\033[0m";
                        break;
                    case "student team id":
                        column = "student.team_id";
                        columnDisplay = "\033[38;5;32m" + "team id" + "\033[0m";
                        break;
                    default:
                        System.out.println("\u001B[31mInvalid choice!\u001B[0m"); // Printing an error message for invalid choices
                        continue; // Continuing to the next iteration of the loop
                }

                System.out.print("\033[38;5;208m" + "What do you want to update it to?" + "\033[0m"); // Prompting the user for the new value
                System.out.println("\033[0m");
                String value = scanner.nextLine(); // Reading the new value from the user input

                String updateSql = "UPDATE team\n" +
                        "LEFT JOIN student ON team.team_id = student.team_id\n" +
                        "LEFT JOIN contact_gegevens ON student.contact_id = contact_gegevens.contact_id\n" +
                        "SET " + column + " = " + (choice.equals("contact number") ? value : "'" + value + "'") +
                        " WHERE student.student_id = '" + student_id + "'";

                // SQL update statement to update the chosen column for the student
                PreparedStatement updateStmt = conn.prepareStatement(updateSql); // Creating a PreparedStatement object with the update statement
                updateStmt.executeUpdate();  // Executing the update statement

                System.out.print("\033[38;5;32m" + "The " + columnDisplay + "\033[0m"+ "\033[38;5;32m" +" has been updated to "+"\033[0m");
                System.out.print("\033[38;5;32m" + value + "\033[0m");
                System.out.println("\033[38;5;32m" + " for student with ID: " + student_id+ "\033[0m"); // Prints a success message with the updated value and student number
                updateStmt.close(); // Closing the PreparedStatement
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
