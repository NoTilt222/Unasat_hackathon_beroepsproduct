import com.mysql.cj.jdbc.PreparedStatementWrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Update {
    public void UpdateInformation(){
        try(Connection conn= DatabaseConnection.getConnection()){
            Scanner scanner= new Scanner(System.in);
            System.out.print("Student number: ");
            String student_number = scanner.nextLine();
            System.out.print("What do you want to update:");
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
            System.out.print("What do you want to update it to?");
            String value = scanner.next();

            String UpdateSql= "UPDATE team\n" +
                    "LEFT JOIN student ON team.team_id = student.team_id\n" +
                    "LEFT JOIN contact_gegevens ON student.contact_id = contact_gegevens.contact_id\n" +
                    "SET " + column + " = " + (choice.equals("contact number") ? value : "'" + value + "'") +
                    " WHERE student.studenten_nummer = '" + student_number + "'";


            PreparedStatement updateStmt= conn.prepareStatement(UpdateSql);
            updateStmt.executeUpdate();

            System.out.println("The "+ choice +" has been updated to "+ value + " for " + student_number);
            conn.close();
            updateStmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
