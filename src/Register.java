import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;

public class Register {
    public void RegisterTeam() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Team name: ");
            String teamName = scanner.nextLine().trim();

            String firstName1;
            String lastName1;
            while (true) {
                System.out.print("Enter 1st person's full name: ");
                String person1 = scanner.nextLine().trim();
                String[] names1 = person1.split(" ", 2); // "2" means split the string into 2 parts
                firstName1 = names1[0];
                lastName1 = names1[1];

                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT naam, achter_naam " +
                                "FROM student " +
                                "WHERE EXISTS " +
                                "(SELECT naam, achter_naam from student WHERE naam = ? AND achter_naam = ?)"
                );
                stmt.setString(1, firstName1);
                stmt.setString(2, lastName1);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    System.out.println("\u001B[31mPerson already exists\u001B[0m");
                } else {
                    break;
                }
            }

            System.out.print("Student number: ");
            String student_number1 = scanner.next();

            System.out.print("Birth date (2002-04-09): ");
            String birth_date1 = scanner.next();
            LocalDate birthdate1 = LocalDate.parse(birth_date1);
            LocalDate current_date = LocalDate.now();

            Period age_1 = Period.between(birthdate1, current_date);
            int age1 = age_1.getYears();

            System.out.print("Skill: ");
            String skill1 = scanner.next();

            System.out.print("Contact number: ");
            int contact_number1 = Integer.parseInt(scanner.next());

            String email_adres1 = null;
            while (true) {
                System.out.print("Unasat Email Address: ");
                email_adres1 = scanner.next();
                if (email_adres1.contains("@unasat.sr")) {
                    break;
                } else {
                    System.out.println("\u001B[31mInvalid email address\u001B[0m");
                }
            }

            System.out.print("Residence: ");
            String residence1 = scanner.next();
            
            scanner.nextLine();
            
            String firstName2;
            String lastName2;
            while (true) {
                System.out.print("Enter 2nd person's full name: ");
                String person2 = scanner.nextLine().trim();
                String[] names2 = person2.split(" ", 2); // "2" means split the string into 2 parts
                firstName2 = names2[0];
                lastName2 = names2[1];

                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT naam, achter_naam " +
                                "FROM student " +
                                "WHERE EXISTS " +
                                "(SELECT naam, achter_naam from student WHERE naam = ? AND achter_naam = ?)"
                );
                stmt.setString(1, firstName2);
                stmt.setString(2, lastName2);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    System.out.println("\u001B[31mPerson already exists\u001B[0m");
                } else {
                    break;
                }
            }

            System.out.print("Student number: ");
            String student_number2 = scanner.next();

            System.out.print("Birth date(2002-04-09): ");
            String birth_date2 = scanner.next();

            LocalDate birthdate2 = LocalDate.parse(birth_date2);
            Period age_2 = Period.between(birthdate2, current_date);
            int age2 = age_2.getYears();
            System.out.print("Skill: ");
            String skill2 = scanner.next();

            System.out.print("Contact number: ");
            int contact_number2 = Integer.parseInt(scanner.next());

            String email_adres2 = null;
            while (true) {
                System.out.print("Unasat Email Address: ");
                email_adres2 = scanner.next();
                if (email_adres2.contains("@unasat.sr")) {
                    break;
                } else {
                    System.out.println("\u001B[31mInvalid email address\u001B[0m");
                }
            }

            System.out.print("Residence: ");
            String residence2 = scanner.next();

            String insertTeamSql = "INSERT INTO TEAM(team_naam) VALUES(?)";
            PreparedStatement insertTeamstmt = conn.prepareStatement(insertTeamSql, Statement.RETURN_GENERATED_KEYS);
            insertTeamstmt.setString(1, teamName);
            insertTeamstmt.executeUpdate();

            ResultSet generatedKeysTeam = insertTeamstmt.getGeneratedKeys();
            int teamId = -1;
            if (generatedKeysTeam.next()) {
                teamId = generatedKeysTeam.getInt(1);
            }

            String insertContactSql = "INSERT INTO contact_gegevens(contact_nummer, unasat_emailadres, verblijfplaats) values(?,?,?)";
            PreparedStatement insertContactStmt = conn.prepareStatement(insertContactSql, Statement.RETURN_GENERATED_KEYS);
            insertContactStmt.setInt(1, contact_number1);
            insertContactStmt.setString(2, email_adres1);
            insertContactStmt.setString(3, residence1);
            insertContactStmt.executeUpdate();

            ResultSet generatedKeysContact = insertContactStmt.getGeneratedKeys();
            int contactId1 = -1;
            if (generatedKeysContact.next()) {
                contactId1 = generatedKeysContact.getInt(1);
            }

            insertContactStmt.setInt(1, contact_number2);
            insertContactStmt.setString(2, email_adres2);
            insertContactStmt.setString(3, residence2);
            insertContactStmt.executeUpdate();

            generatedKeysContact = insertContactStmt.getGeneratedKeys();
            int contactId2 = -1;
            if (generatedKeysContact.next()) {
                contactId2 = generatedKeysContact.getInt(1);
            }

            String insertStudentSql = "INSERT INTO student(naam, achter_naam,studenten_nummer,leeftijd,geboorte_datum,vaardigheid, team_id, contact_id) VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement insertStudentStmt = conn.prepareStatement(insertStudentSql);
            insertStudentStmt.setString(1, firstName1);
            insertStudentStmt.setString(2, lastName1);
            insertStudentStmt.setString(3, student_number1);
            insertStudentStmt.setInt(4, age1);
            insertStudentStmt.setDate(5, Date.valueOf(birth_date1));
            insertStudentStmt.setString(6, skill1);
            insertStudentStmt.setInt(7, teamId);
            insertStudentStmt.setInt(8, contactId1);
            insertStudentStmt.executeUpdate();

            insertStudentStmt.setString(1, firstName2);
            insertStudentStmt.setString(2, lastName2);
            insertStudentStmt.setString(3, student_number2);
            insertStudentStmt.setInt(4, age2);
            insertStudentStmt.setDate(5, Date.valueOf(birth_date2));
            insertStudentStmt.setString(6, skill2);
            insertStudentStmt.setInt(7, teamId);
            insertStudentStmt.setInt(8, contactId2);
            insertStudentStmt.executeUpdate();

            System.out.println("Student " + firstName1 + " " + lastName1 + " has been created.");
            System.out.println("Student " + firstName2 + " " + lastName2 + " has been created.");
            insertStudentStmt.close();
        }
        catch (SQLException ex) { // Catch any SQL exceptions that may occur
            System.out.println("Error: " + ex.getMessage()); // Print the error message
        }
    }
}
