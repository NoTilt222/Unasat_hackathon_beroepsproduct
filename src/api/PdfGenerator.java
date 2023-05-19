package api;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PdfGenerator {
    public static void generatePdfFromCode(PDPageContentStream stream, PDDocument document, List<Map<String, String>> data) {
        try {
            // Use the provided stream parameter
            PDPageContentStream contentStream = stream;

            // Add your content to the existing stream
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12); // Use a different font (e.g., Helvetica)
            contentStream.newLineAtOffset(25, 825);
            contentStream.showText("Team Overview");
            contentStream.endText();

            // Define the initial x and y coordinate offsets
            float xOffset = 25;
            float yOffset = 815;

            // Iterate through the data and display it in the PDF
            for (Map<String, String> row : data) {
                // Increase the y-coordinate offset for each record
                yOffset -= 60; // Adjust the value to increase or decrease the vertical spacing

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12); // Use the same font for data
                contentStream.newLineAtOffset(xOffset, yOffset);

                // Display the team information
                contentStream.showText("Team:");
                contentStream.newLineAtOffset(0, -20); // Add line break and vertical spacing

                contentStream.showText("ID: " + row.get("teamid") + ", Name: " + row.get("teamname"));
                contentStream.newLineAtOffset(0, -15); // Add line break and vertical spacing

                contentStream.showText("Members:");

                // Display the member information
                contentStream.showText("ID: " + row.get("studentid") + ", Name: " + row.get("first") + " " + row.get("last") + ", E-mailadres: " + row.get("email"));
                contentStream.newLineAtOffset(0, -38 ); // Add vertical spacing between members

                // Repeat for other members

                contentStream.endText();
            }

            contentStream.close();

            String filePath = "C:\\Users\\mento\\OneDrive\\Desktop\\pdf\\file.pdf";
            // Save and close the document outside the method
            document.save(filePath);
            document.close();
            System.out.println("PDF created successfully in directory C:\\Users\\mento\\OneDrive\\Desktop\\pdf\\");

            // Open the generated PDF
            File file = new File(filePath);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
