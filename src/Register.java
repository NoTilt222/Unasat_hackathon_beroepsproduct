import databasemanager.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register {
    public void RegisterTeams() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            Scanner scanner = new Scanner(System.in);
            String choice;
            do {
                System.out.println("\033[38;5;208m" + "What do you want to do?" + "\033[0m"); // prints the message "What do you want to delete?" to the console.
                System.out.print("\033[38;5;208m" + "| " + "\033[0m"); //Print a separator in orange
                System.out.print("\033[38;5;33m" + "Register Whole Team(team of 2)" + "\033[0m"); //represents a menu option for deleting a student.
                System.out.print("\033[38;5;208m" + " | " + "\033[0m"); //Print a separator in orange
                System.out.print("\033[38;5;33m" + "Assign New Student(This assigns a student to an existing team)" + "\033[0m"); //represents a menu option for deleting a team.
                System.out.print("\033[38;5;208m" + " | " + "\033[0m"); //Print a separator in orange
                System.out.print("\033[38;5;33m" + "Return" + "\033[0m");
                System.out.print("\033[38;5;208m" + " | " + "\033[0m");
                System.out.println(); // Consume the newline character
                choice = scanner.nextLine().toLowerCase();

                switch (choice) {
                    case "register whole team":
                        RegisterTeam(conn, scanner);
                        break; // the "break" statement is used within a switch statement to immediately exit the switch block and continue executing the code after the switch block.
                    case "assign new student":
                        RegisterStudent(conn, scanner);
                        break;
                    case "return":
                        System.out.println("Returning to menu");
                        break;
                    default:
                        System.out.println("\u001B[31mInvalid choice!\u001B[0m"); // Print an error message for an invalid choice
                        break;
                }
            } while (!Objects.equals(choice, "return"));
        }
        catch (SQLException ex) { // Catch any SQL exceptions that may occur
            System.out.println("Error: " + ex.getMessage()); // Print the error message
        }
    }
    private void RegisterTeam(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("\033[38;5;32m" + "Team name: " + "\033[0m");
        String teamName = scanner.nextLine().trim();

        String firstName1;
        String lastName1;
        while (true) {
            System.out.print("\033[38;5;32m" + "Enter 1st person's full name: " + "\033[0m");
            String person1 = scanner.nextLine().trim();
            String[] names1 = person1.split(" ", 2); // "2" means split the string into 2 parts

            if (names1.length < 2) {
                System.out.println("\u001B[31mPlease enter the full name (first name and last name).\u001B[0m");
                continue;
            }

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

        String student_number1;
        while (true) {
            System.out.print("\033[38;5;32m" + "Student number (XX/####/###): " + "\033[0m");
            student_number1 = scanner.next();

            // Check if the input matches the required format
            Pattern pattern = Pattern.compile("^[A-Za-z]{0,}[A-Za-z\\/\\d]{2}\\/\\d{4}\\/\\d{3}$");
            Matcher matcher = pattern.matcher(student_number1);
            if (matcher.matches()) {
                break;
            } else {
                System.out.println("\u001B[31mInvalid student number format. Please enter a valid student number (XX/####/###)\u001B[0m");
            }
        }

        String birth_date1;
        while (true) {
            System.out.print("\033[38;5;32m" + "Birth date (2002-04-09): " + "\033[0m");
            birth_date1 = scanner.next();
            try {
                LocalDate.parse(birth_date1);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("\u001B[31mInvalid date format. Please enter the date in the format 'yyyy-MM-dd'.\u001B[0m");
            }
        }
        LocalDate birthdate1 = LocalDate.parse(birth_date1);
        LocalDate current_date = LocalDate.now();

        Period age_1 = Period.between(birthdate1, current_date);
        int age1 = age_1.getYears();

        System.out.print("\033[38;5;32m" + "Skill: " + "\033[0m");
        String skill1 = scanner.next();
        scanner.nextLine();
        int contact_number1;
        while (true) {
            System.out.print("\033[38;5;32m" + "Contact number: " + "\033[0m");
            String input = scanner.nextLine();

            // Check if the input is empty or contains non-digit characters
            if (input.isEmpty() || !input.matches("\\d+")) {
                System.out.println("\u001B[31mInvalid input. Please enter a valid 7-digit number.\u001B[0m");
                continue;
            }

            contact_number1 = Integer.parseInt(input);

            int numbersize1 = String.valueOf(contact_number1).length();
            if (numbersize1 == 7) {
                break;
            } else {
                System.out.println("\u001B[31mInvalid phone number. Must contain 7 numbers.\u001B[0m");
            }
        }

        String email_adres1;
        while (true) {
            System.out.print("\033[38;5;32m" + "Unasat Email Address: " + "\033[0m");
            email_adres1 = scanner.next();
            if (email_adres1.contains("@unasat.sr")) {
                break;
            } else {
                System.out.println("\u001B[31mInvalid email address\u001B[0m");
            }
        }

        System.out.print("\033[38;5;32m" + "Residence: " + "\033[0m");
        String residence1 = scanner.next();

        scanner.nextLine();

        String firstName2;
        String lastName2;
        while (true) {
            System.out.print("\033[38;5;32m" + "Enter 2nd person's full name: " + "\033[0m");
            String person2 = scanner.nextLine().trim();
            String[] names2 = person2.split(" ", 2); // "2" means split the string into 2 parts

            if (names2.length < 2) {
                System.out.println("Please enter the full name (first name and last name).");
                continue;
            }

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

        String student_number2;
        while (true) {
            System.out.print("\033[38;5;32m" + "Student number (XX/####/###): " + "\033[0m");
            student_number2 = scanner.next();

            // Check if the input matches the required format
            Pattern pattern = Pattern.compile("^[A-Za-z]{0,}[A-Za-z\\/\\d]{2}\\/\\d{4}\\/\\d{3}$");
            Matcher matcher = pattern.matcher(student_number2);
            if (matcher.matches()) {
                break;
            } else {
                System.out.println("\u001B[31mInvalid student number format. Please enter a valid student number (XX/####/###)\u001B[0m");
            }
        }


        String birth_date2;
        while (true) {
            System.out.print("\033[38;5;32m" + "Birth date (2002-04-09): " + "\033[0m");
            birth_date2 = scanner.next();
            try {
                LocalDate.parse(birth_date2);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("\u001B[31mInvalid date format. Please enter the date in the format 'yyyy-MM-dd'.\u001B[0m");
            }
        }
        LocalDate birthdate2 = LocalDate.parse(birth_date2);
        Period age_2 = Period.between(birthdate2, current_date);
        int age2 = age_2.getYears();
        System.out.print("\033[38;5;32m" + "Skill: " + "\033[0m");
        String skill2 = scanner.next();

        scanner.nextLine();
        int contact_number2;
        while (true) {
            System.out.print("\033[38;5;32m" + "Contact number: " + "\033[0m");
            String input = scanner.nextLine();

            // Check if the input is empty or contains non-digit characters
            if (input.isEmpty() || !input.matches("\\d+")) {
                System.out.println("\u001B[31mInvalid input. Please enter a valid 7-digit number.\u001B[0m");
                continue;
            }

            contact_number2 = Integer.parseInt(input);

            int numbersize2 = String.valueOf(contact_number2).length();
            if (numbersize2 == 7) {
                break;
            } else {
                System.out.println("\u001B[31mInvalid phone number. Must contain 7 numbers.\u001B[0m");
            }
        }

        String email_adres2;
        while (true) {
            System.out.print("\033[38;5;32m" + "Unasat Email Address: " + "\033[0m");
            email_adres2 = scanner.next();
            if (email_adres2.contains("@unasat.sr")) {
                break;
            } else {
                System.out.println("\u001B[31mInvalid email address\u001B[0m");
            }
        }

        System.out.print("\033[38;5;32m" + "Residence: " + "\033[0m");
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

        System.out.println("\u001B[32mStudent " + firstName1 + " " + lastName1 + " has been created.\u001B[0m");
        System.out.println("\u001B[32mStudent " + firstName2 + " " + lastName2 + " has been created.\u001B[0m");
        scanner.nextLine();
        insertStudentStmt.close();
    }

    private void RegisterStudent(Connection conn, Scanner scanner) throws SQLException {
        int teamID = 0;
        System.out.println("\u001B[38;5;208mEnter the team ID you want to assign them to: \u001B[0m");

        String sql = "SELECT distinct team.team_id, team.team_naam\n" +
                "FROM team\n" +
                "LEFT JOIN student ON team.team_id = student.team_id\n" +
                "LEFT JOIN contact_gegevens ON student.contact_id = contact_gegevens.contact_id;";

        try {
            PreparedStatement stmt1 = conn.prepareStatement(sql);
            ResultSet rs1 = stmt1.executeQuery();
            while (rs1.next()) {
                int teamid = rs1.getInt("team_id");
                String teamname = rs1.getString("team_naam");
                System.out.println("\033[34mID:\033[0m " + teamid + ", \033[34mName:\033[0m " + teamname);
            }

            while (true) {
                try {
                    teamID = Integer.parseInt(scanner.nextLine().trim());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("\u001B[31mInvalid input. Please enter an integer.\u001B[0m");
                }
            }

            // Use the teamID for further processing

        } catch (Exception e) {
            // Handle exceptions
        }

        String firstName1;
        String lastName1;
        while (true) {
            System.out.print("\033[38;5;32m" + "Enter 1st person's full name: " + "\033[0m");
            String person1 = scanner.nextLine().trim();
            String[] names1 = person1.split(" ", 2); // "2" means split the string into 2 parts

            if (names1.length < 2) {
                System.out.println("\u001B[31mPlease enter the full name (first name and last name).\u001B[0m");
                continue;
            }

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

        String student_number1;
        while (true) {
            System.out.print("\033[38;5;32m" + "Student number (XX/####/###): " + "\033[0m");
            student_number1 = scanner.next();

            // Check if the input matches the required format
            Pattern pattern = Pattern.compile("^[A-Za-z]{0,}[A-Za-z\\/\\d]{2}\\/\\d{4}\\/\\d{3}$");
            Matcher matcher = pattern.matcher(student_number1);
            if (matcher.matches()) {
                break;
            } else {
                System.out.println("\u001B[31mInvalid student number format. Please enter a valid student number (XX/####/###)\u001B[0m");
            }
        }

        String birth_date1;
        while (true) {
            System.out.print("\033[38;5;32m" + "Birth date (2002-04-09): " + "\033[0m");
            birth_date1 = scanner.next();
            try {
                LocalDate.parse(birth_date1);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("\u001B[31mInvalid date format. Please enter the date in the format 'yyyy-MM-dd'.\u001B[0m");
            }
        }
        LocalDate birthdate1 = LocalDate.parse(birth_date1);
        LocalDate current_date = LocalDate.now();

        Period age_1 = Period.between(birthdate1, current_date);
        int age1 = age_1.getYears();

        System.out.print("\033[38;5;32m" + "Skill: " + "\033[0m");
        String skill1 = scanner.next();

        int contact_number1;
        while (true) {
            System.out.print("\033[38;5;32m" + "Contact number: " + "\033[0m");
            String input = scanner.next();

            // Check if the input is empty or contains non-digit characters
            if (input.isEmpty() || !input.matches("\\d+")) {
                System.out.println("\u001B[31mInvalid input. Please enter a valid 7-digit number.\u001B[0m");
                continue;
            }

            contact_number1 = Integer.parseInt(input);

            int numbersize1 = String.valueOf(contact_number1).length();
            if (numbersize1 == 7) {
                break;
            } else {
                System.out.println("\u001B[31mInvalid phone number. Must contain 7 numbers.\u001B[0m");
            }
        }

        String email_adres1;
        while (true) {
            System.out.print("\033[38;5;32m" + "Unasat Email Address: " + "\033[0m");
            email_adres1 = scanner.next();
            if (email_adres1.contains("@unasat.sr")) {
                break;
            } else {
                System.out.println("\u001B[31mInvalid email address\u001B[0m");
            }
        }

        System.out.print("\033[38;5;32m" + "Residence: " + "\033[0m");
        String residence1 = scanner.next();


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



        String insertStudentSql = "INSERT INTO student(naam, achter_naam,studenten_nummer,leeftijd,geboorte_datum,vaardigheid, team_id, contact_id) VALUES(?,?,?,?,?,?,?,?)";
        PreparedStatement insertStudentStmt = conn.prepareStatement(insertStudentSql);
        insertStudentStmt.setString(1, firstName1);
        insertStudentStmt.setString(2, lastName1);
        insertStudentStmt.setString(3, student_number1);
        insertStudentStmt.setInt(4, age1);
        insertStudentStmt.setDate(5, Date.valueOf(birth_date1));
        insertStudentStmt.setString(6, skill1);
        insertStudentStmt.setInt(7, teamID);
        insertStudentStmt.setInt(8, contactId1);
        insertStudentStmt.executeUpdate();


        System.out.println("\u001B[32mStudent " + firstName1 + " " + lastName1 + " has been created.\u001B[0m");
        scanner.nextLine();
        insertStudentStmt.close();
    }
}
