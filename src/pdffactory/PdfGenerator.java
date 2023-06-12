package pdffactory;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PdfGenerator {
    public static void generatePdfFromCode(PDPageContentStream contentStream, PDDocument document, List<Map<String, String>> data) {
        try {

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12); // Use a different font (e.g., Helvetica)
            contentStream.newLineAtOffset(25, 825);
            contentStream.showText("Team Overview");
            contentStream.endText();

            // Define the initial x and y coordinate offsets
            float xOffset = 25;
            float yOffset = 845;

            String previousTeamId = null;
            // Iterate through the data and display it in the PDF
            for (Map<String, String> row : data) {
                String currentTeamId = row.get("teamid");
                String option = row.get("option");
                // Increase the y-coordinate offset for each record
                yOffset -= 60; // Adjust the value to increase or decrease the vertical spacing

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8); // Use the same font for data
                contentStream.newLineAtOffset(xOffset, yOffset);
                if (!currentTeamId.equals(previousTeamId)) {
                    // Display the team information
                    contentStream.newLineAtOffset(0, -5);
                    contentStream.showText("Team:");
                    contentStream.newLineAtOffset(0, -20); // Add line break and vertical spacing

                    contentStream.showText("ID: " + row.get("teamid") + ", Name: " + row.get("teamname"));
                    contentStream.newLineAtOffset(0, -20); // Add line break and vertical spacing
                    contentStream.showText("Members:");
                } else {
                    contentStream.newLineAtOffset(0, -15); // Add vertical spacing between members
                }
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("ID: " + row.get("studentid") + ", Name: " + row.get("first") + " " + row.get("last") + ", E-mailadres: " + row.get("email"));
                if (Objects.equals(option, "1")) {
                    // Add vertical spacing between members
                    contentStream.showText(", age: " + row.get("age") + ", birth: " + row.get("birth") + ", skill: " + row.get("skill") + ", contact: " + row.get("contact"));
                }
                contentStream.newLineAtOffset(0, -38); // Add vertical spacing between members

                contentStream.endText();
                previousTeamId = currentTeamId;
            }

            contentStream.close();
            int version = 1;
            boolean is_done = false;
            String filePath = null;
            while (!is_done) {
                filePath = "C:\\Users\\mento\\OneDrive\\Desktop\\pdf\\fileV" + version + ".pdf";

                try {
                    // Save and close the document outside the loop
                    document.save(filePath);
                    System.out.println("PDF created successfully in directory C:\\Users\\mento\\OneDrive\\Desktop\\pdf\\");

                    // Open the generated PDF
                    File file = new File(filePath);
                    if (file.exists()) {
                        Desktop.getDesktop().open(file);
                    }

                    is_done = true; // Set the flag to exit the loop

                } catch (IOException e) {
                    e.printStackTrace();
                }

                version++; // Increment the version number
            }

            // Close the document after the loop
            try {
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
