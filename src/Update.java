import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Update {
    public void UpdateInformation(){
        try(Connection conn= DatabaseConnection.getConnection()){
            Scanner scanner = new Scanner(System.in);
            String studenten_nummer = null;
            while (true) {
                Scanner sc = new Scanner(System.in);
                System.out.print("Student number: ");
                studenten_nummer = sc.next();

                String sql = "SELECT student.studenten_nummer\n" +
                        "FROM team\n" +
                        "LEFT JOIN student ON team.team_id = student.team_id\n" +
                        "LEFT JOIN contact_gegevens ON student.contact_id = contact_gegevens.contact_id\n" +
                        "WHERE student.studenten_nummer = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, studenten_nummer); // Set the student number as a parameter in the query
                ResultSet rs = stmt.executeQuery();

                if (!rs.next()) { // If no rows are returned, the student number does not exist
                    break;
                } else {
                    System.out.println("\u001B[31mStudent exists!\u001B[0m");
                }
            }
            System.out.print("\033[38;5;208m" + "What do you want to update:" + "\033[0m");
            System.out.println("\033[38;5;208m" + "| " + "\033[0m"); // Make the left | blue
            System.out.print("\033[38;5;33m" + "team name" + "\033[0m"); // Make the left orange
            System.out.print("\033[38;5;208m" + " | " + "\033[0m"); // Make the middle | orange
            System.out.print("\033[38;5;33m" + "skill" + "\033[0m"); // Make the option blue
            System.out.print("\033[38;5;208m" + " | " + "\033[0m"); // Make the right | orange
            System.out.print("\033[38;5;33m" + "residence" + "\033[0m"); // Make the option blue
            System.out.print("\033[38;5;208m" + " | " + "\033[0m"); // Make the right | orange
            System.out.print("\033[38;5;33m" + "contact number" + "\033[0m"); // Make the option blue
            System.out.print("\033[38;5;208m" + " | " + "\033[0m"); // Make the right | orange
            System.out.println();
            String choice= scanner.nextLine().toLowerCase();
            String column ="";
            switch (choice){
                case "team name"-> column= "team.team_naam";
                case "skill"-> column= "student.vaardigheid";
                case "residence"-> column= "contact_gegevens.verblijfplaats";
                case "contact number"-> column= "contact_gegevens.contact_nummer";
                default -> System.out.println("invalid choice!");

            }
            System.out.print("\033[38;5;208m" + "What do you want to update it to?" + "\033[0m");
            String value = scanner.next();

            String UpdateSql= "UPDATE team\n" +
                    "LEFT JOIN student ON team.team_id = student.team_id\n" +
                    "LEFT JOIN contact_gegevens ON student.contact_id = contact_gegevens.contact_id\n" +
                    "SET " + column + " = " + (choice.equals("contact number") ? value : "'" + value + "'") +
                    " WHERE student.studenten_nummer = '" + studenten_nummer + "'";


            PreparedStatement updateStmt= conn.prepareStatement(UpdateSql);
            updateStmt.executeUpdate();

            System.out.println("The "+ choice +" has been updated to "+ value + " for " + studenten_nummer);
            conn.close();
            updateStmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
