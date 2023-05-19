import api.PdfGenerator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.sql.*;
import java.util.*;

public class ViewStudents {
    public void View(PDPageContentStream contentStream, PDDocument document) {
        Scanner scanner = new Scanner(System.in);
        String choice;
        do {
            System.out.println("\n\u001B[38;5;208m" + "Choose an option below then enter: ");
            System.out.print("\033[0m");
            System.out.print("\033[38;5;208m" + "| " + "\033[0m");
            System.out.print("\033[38;5;33m" + "View Teams" + "\033[0m");
            System.out.print("\033[38;5;208m" + " | " + "\033[0m");
            System.out.print("\033[38;5;33m" + "Search Team" + "\033[0m");
            System.out.print("\033[38;5;208m" + " | " + "\033[0m");
            System.out.print("\033[38;5;33m" + "Return" + "\033[0m");
            System.out.print("\033[38;5;208m" + " | " + "\033[0m");
            System.out.println();
            choice = scanner.nextLine().toLowerCase();
            switch (choice) {
                case "view teams":
                    ViewTeams(contentStream,document);
                    break;
                case "search team":
                    SearchTeamInfo(contentStream, document);
                    break;
                case "return":
                    System.out.println("Returning to menu");
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        } while (!Objects.equals(choice, "return"));
    }

    public void ViewTeams(PDPageContentStream contentStream, PDDocument document) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT team.team_id, team.team_naam, student.student_id, student.naam, student.achter_naam, contact_gegevens.unasat_emailadres\n" +
                    "FROM team\n" +
                    "LEFT JOIN student ON team.team_id = student.team_id\n" +
                    "LEFT JOIN contact_gegevens ON student.contact_id = contact_gegevens.contact_id;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            Map<Integer, String> teamMap = new HashMap<>();
            List<Map<String, String>> data = new ArrayList<>();

            while (rs.next()) {
                int teamid = rs.getInt("team_id");
                String teamname = rs.getString("team_naam");
                int studentid = rs.getInt("student_id");
                String first = rs.getString("naam");
                String last = rs.getString("achter_naam");
                String email = rs.getString("unasat_emailadres");
                if (!teamMap.containsKey(teamid)) {
                    System.out.println("\033[34mTeam:\033[0m");
                    System.out.println("\033[34mID:\033[0m " + teamid + ", \033[34mName:\033[0m " + teamname);
                    System.out.println("\033[34mMembers:\033[0m");
                    teamMap.put(teamid, teamname);
                }
                System.out.println("\033[34mID:\033[0m " + studentid + ", \033[34mName:\033[0m " + first + " " + last + ", \033[34mE-mailadres:\033[0m " + email);

                // Store the data in a separate map
                Map<String, String> row = new HashMap<>();
                row.put("teamid", String.valueOf(teamid));
                row.put("teamname", teamname);
                row.put("studentid", String.valueOf(studentid));
                row.put("first", first);
                row.put("last", last);
                row.put("email", email);
                data.add(row);
            }
            rs.close();
            stmt.close();

            Scanner pdfsc = new Scanner(System.in);
            System.out.println("Do you want a PDF? (Y/N)");
            String is_pdf = pdfsc.next();
            if (is_pdf.equalsIgnoreCase("Y")) {
                PdfGenerator pdfgenerator = new PdfGenerator();
                pdfgenerator.generatePdfFromCode(contentStream, document, data);
            } else {
                System.out.println("Returning to menu");
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }


    public void SearchTeamInfo(PDPageContentStream contentStream, PDDocument document) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter team name: ");
        String input = scanner.nextLine();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT team.team_id, team.team_naam, student.student_id, student.naam, student.achter_naam, student.leeftijd, student.geboorte_datum, student.vaardigheid, contact_gegevens.unasat_emailadres, contact_gegevens.contact_nummer, contact_gegevens.verblijfplaats\n" +
                    "FROM team\n" +
                    "LEFT JOIN student ON team.team_id = student.team_id\n" +
                    "LEFT JOIN contact_gegevens ON student.contact_id = contact_gegevens.contact_id\n" +
                    "WHERE team.team_naam = '" + input + "';";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            Map<Integer, String> teamMap = new HashMap<>();
            List<Map<String, String>> data = new ArrayList<>();

            while (rs.next()) {
                int teamid = rs.getInt("team_id");
                String teamname = rs.getString("team_naam");
                int studentid = rs.getInt("student_id");
                String first = rs.getString("naam");
                String last = rs.getString("achter_naam");
                String email = rs.getString("unasat_emailadres");
                int age = rs.getInt("leeftijd");
                String birth = rs.getString("geboorte_datum");
                String skill = rs.getString("vaardigheid");
                int contact = rs.getInt("contact_nummer");
                String residence = rs.getString("verblijfplaats");

                if (!teamMap.containsKey(teamid)) {
                    System.out.println("\n\033[34mTeam:\033[0m");
                    System.out.println("\033[34mID:\033[0m " + teamid + ", \033[34mName:\033[0m " + teamname);
                    System.out.println("\033[34mMembers:\033[0m");
                    teamMap.put(teamid, teamname);
                }
                System.out.println("\033[34mID:\033[0m " + studentid + ", \033[34mName:\033[0m " + first + " " + last + ", \033[34mAge:\033[0m " + age + ", \033[34mDate of birth:\033[0m " + birth + ", \033[34mSkill:\033[0m " + skill + ", \033[34mE-mailadres:\033[0m " + email + ", \033[34mContact number:\033[0m " + contact + ", \033[34mResidence:\033[0m " + residence);

                // Store the data in a separate map
                Map<String, String> row = new HashMap<>();
                row.put("teamid", String.valueOf(teamid));
                row.put("teamname", teamname);
                row.put("studentid", String.valueOf(studentid));
                row.put("first", first);
                row.put("last", last);
                row.put("email", email);
                row.put("age", String.valueOf(age));
                row.put("birth", birth);
                row.put("skill", skill);
                row.put("contact", String.valueOf(contact));
                row.put("residence", residence);
                data.add(row);
            }
            rs.close();
            stmt.close();

            Scanner pdfsc = new Scanner(System.in);
            System.out.println("\nDo you want a PDF? (Y/N)");
            String is_pdf = pdfsc.next();
            if (is_pdf.equalsIgnoreCase("Y")) {
                PdfGenerator pdfgenerator = new PdfGenerator();
                pdfgenerator.generatePdfFromCode(contentStream, document, data);
            } else {
                System.out.println("Returning to menu");
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
