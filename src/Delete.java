import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Delete {
    public void DeleteInformation() {
        try (Connection conn = DatabaseConnection.getConnection()) { // Get a connection to the database
            Scanner scanner = new Scanner(System.in); //Create a scanner object to read user input
            System.out.println("\033[38;5;208m" + "What do you want to delete?" + "\033[0m"); // prints the message "What do you want to delete?" to the console.
            System.out.print("\033[38;5;208m" + " | " + "\033[0m"); //Print a separator in orange
            System.out.print("\033[38;5;33m" + "Student" + "\033[0m"); //represents a menu option for deleting a student.
            System.out.print("\033[38;5;208m" + " | " + "\033[0m"); //Print a separator in orange
            System.out.print("\033[38;5;33m" + "Team" + "\033[0m"); //represents a menu option for deleting a team.
            System.out.print("\033[38;5;208m" + " | " + "\033[0m"); //Print a separator in orange
            System.out.println(); // Consume the newline character

            String choice = scanner.nextLine().toLowerCase(); // Read user input as a string and convert to lowercase

            switch (choice) {
                case "student":
                    deleteStudent(conn, scanner); //This method handles the deletion of a student.
                    break; // the "break" statement is used within a switch statement to immediately exit the switch block and continue executing the code after the switch block.
                case "team":
                    deleteTeam(conn, scanner); //This method handles the deletion of a team.
                    break;
                default:
                    System.out.println("Invalid choice!"); // Print an error message for an invalid choice
                    break;
            }

            conn.close(); // Close the database connection
        } catch (SQLException e) {
            throw new RuntimeException(e); // Throw a runtime exception if a SQL exception occurs

        }
    }

    private void deleteStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Student number: "); // Prompt the user to enter a student number
        String student_number = scanner.nextLine(); // Read the user input for the student number

        // Delete the student's information from the database
        String deleteSql = "DELETE FROM student WHERE studenten_nummer = ?"; // SQL delete statement to delete a student
        PreparedStatement deleteStmt = conn.prepareStatement(deleteSql); // Create a prepared statement with the delete SQL
        deleteStmt.setString(1, student_number); // Set the student number as a parameter in the delete statement
        int rowsDeleted = deleteStmt.executeUpdate(); // Execute the delete statement and get the number of rows deleted

        if (rowsDeleted > 0) {
            System.out.println("The information of student with student number " + student_number + " has been deleted."); // Print a success message if rows were deleted
        } else {
            System.out.println("No student with student number " + student_number + " was found in the database.");  // Print a message if no rows were deleted
        }

        deleteStmt.close();
    }

    private void deleteTeam(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Team ID: "); // Prompt the user to enter a team ID
        int team_id = scanner.nextInt(); // Read the user input for the team ID
        scanner.nextLine(); // Consume the newline character

        // Delete the students associated with the team
        String deleteStudentsSql = "DELETE FROM student WHERE team_id = ?"; // SQL delete statement to delete students associated with a team
        PreparedStatement deleteStudentsStmt = conn.prepareStatement(deleteStudentsSql); // Create a prepared statement with the delete students SQL
        deleteStudentsStmt.setInt(1, team_id); // Set the team ID as a parameter in the delete students statement
        int studentsDeleted = deleteStudentsStmt.executeUpdate(); // Execute the delete students statement and get the number of students deleted

        // Delete the team
        String deleteTeamSql = "DELETE FROM team WHERE team_id = ?"; // // SQL delete statement to delete a team
        PreparedStatement deleteTeamStmt = conn.prepareStatement(deleteTeamSql); // Create a prepared statement with the delete team SQL
        deleteTeamStmt.setInt(1, team_id); // Set the team ID as a parameter in the delete team statement
        int teamDeleted = deleteTeamStmt.executeUpdate(); // Execute the delete team statement and get the number of teams deleted

        if (teamDeleted > 0) {
            System.out.println("The team with ID " + team_id + " has been deleted, including all associated students."); // Print a success message if the team and associated students were deleted
        } else {
            System.out.println("No team with ID " + team_id + " was found in the database."); // Print a message if no team was deleted
        }

        deleteStudentsStmt.close();
        deleteTeamStmt.close();

    }
}
