import com.mysql.cj.jdbc.PreparedStatementWrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Update {
    public void UpdateInformation() {
        try (Connection conn = DatabaseConnection.getConnection()) { // Get a connection to the database
            Scanner scanner = new Scanner(System.in); //Create a scanner object to read user input
            String studenten_nummer = null; // Initialize a variable to store the student number


            while (true) {
                Scanner sc = new Scanner(System.in); // Create another Scanner object
                System.out.print("Student number: "); // Prompt the user to enter a student number
                studenten_nummer = sc.next(); // Read the user input and store it in the studenten_nummer variable

                // SQL query to check if the student number exists in the database
                String sql = "SELECT student.studenten_nummer\n" +
                        "FROM team\n" +
                        "LEFT JOIN student ON team.team_id = student.team_id\n" +
                        "LEFT JOIN contact_gegevens ON student.contact_id = contact_gegevens.contact_id\n" +
                        "WHERE student.studenten_nummer = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);  // Create a prepared statement with the SQL query
                stmt.setString(1, studenten_nummer); // Set the student number as a parameter in the query
                ResultSet rs = stmt.executeQuery(); // Execute the query and store the result in a ResultSet

                if (rs.next()) { // If the ResultSet has a row, the student number exists
                    break; // exit the loop
                } else {
                    System.out.println("\u001B[31mStudent does not exist!\u001B[0m");// If no rows are returned, the student number does not exist
                }
            }

            while (true) {
                System.out.print("\033[38;5;208m" + "What do you want to update:" + "\033[0m"); // prompt the user to choose what to update
                System.out.println("\033[0m");
                System.out.print("\033[38;5;208m" + "| " + "\033[0m"); // Make the left | blue
                System.out.print("\033[38;5;33m" + "team name" + "\033[0m"); // Make the left orange
                System.out.print("\033[38;5;208m" + " | " + "\033[0m"); // Make the middle | orange
                System.out.print("\033[38;5;33m" + "skill" + "\033[0m"); // Make the option blue
                System.out.print("\033[38;5;208m" + " | " + "\033[0m"); // Make the right | orange
                System.out.print("\033[38;5;33m" + "residence" + "\033[0m"); // Make the option blue
                System.out.print("\033[38;5;208m" + " | " + "\033[0m"); // Make the right | orange
                System.out.print("\033[38;5;33m" + "contact number" + "\033[0m"); // Make the option blue
                System.out.print("\033[38;5;208m" + " | " + "\033[0m"); // Make the right | orange
                System.out.print("\033[38;5;33m" + "return" + "\033[0m"); // Make the option blue
                System.out.print("\033[38;5;208m" + " | " + "\033[0m"); // Make the right | orange
                System.out.println();
                String choice = scanner.nextLine().toLowerCase(); // Read user input and convert it to lowercase

                if (choice.equals("return")) {
                    System.out.println("\nReturning to menu...\n");
                    return; // Return to the calling method (in this case, the Main class)
                }

                String column = "";
                switch (choice) {
                    case "team name" -> column = "team.team_naam";
                    case "skill" -> column = "student.vaardigheid";
                    case "residence" -> column = "contact_gegevens.verblijfplaats";
                    case "contact number" -> column = "contact_gegevens.contact_nummer";
                    default -> {
                        System.out.println("Invalid choice!");
                        continue; // Continue to the next iteration of the loop
                    }
                }

                System.out.print("\033[38;5;208m" + "What do you want to update it to?" + "\033[0m");
                String value = scanner.next(); // Read the user input for the new value


                // SQL update statement to update the specified column with the new value
                String updateSql = "UPDATE team\n" +
                        "LEFT JOIN student ON team.team_id = student.team_id\n" +
                        "LEFT JOIN contact_gegevens ON student.contact_id = contact_gegevens.contact_id\n" +
                        "SET " + column + " = " + (choice.equals("contact number") ? value : "'" + value + "'") +
                        " WHERE student.studenten_nummer = '" + studenten_nummer + "'";

                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.executeUpdate();

                System.out.println("The " + choice + " has been updated to " + value + " for " + studenten_nummer);
                updateStmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
