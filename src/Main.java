    import java.io.IOException;
    import java.sql.SQLException;
    import java.util.Scanner;
    import java.util.Objects;

    public class Main {
        static ViewStudents view = new ViewStudents();
        static Register register = new Register();
        static Update update = new Update();
        static Delete delete = new Delete();

        public static void main(String[] args) throws IOException, SQLException {
            System.out.println("\u001B[38;5;208m" + "Welcome to UNASAT HACKATHON");
            System.out.print("\033[0m");
            Scanner scanner = new Scanner(System.in);
            String choice;

            do {
                System.out.println("\u001B[38;5;208m" + "Choose an option below then enter: ");
                System.out.print("\033[0m");
                System.out.print("\033[38;5;208m" + "| " + "\033[0m"); // Make the left | blue
                System.out.print("\033[38;5;33m" + "View" + "\033[0m"); // Make the left orange
                System.out.print("\033[38;5;208m" + " | " + "\033[0m"); // Make the middle | orange
                System.out.print("\033[38;5;33m" + "Register" + "\033[0m"); // Make the option blue
                System.out.print("\033[38;5;208m" + " | " + "\033[0m"); // Make the right | orange
                System.out.print("\033[38;5;33m" + "Update" + "\033[0m"); // Make the option blue
                System.out.print("\033[38;5;208m" + " | " + "\033[0m"); // Make the right | orange
                System.out.print("\033[38;5;33m" + "Delete" + "\033[0m"); // Make the option blue
                System.out.print("\033[38;5;208m" + " | " + "\033[0m"); // Make the right | orange
                System.out.print("\033[38;5;33m" + "Exit" + "\033[0m"); // Make the option blue
                System.out.print("\033[38;5;208m" + " | " + "\033[0m"); // Make the right | orange
                System.out.println();
                choice = scanner.next().toLowerCase();
                switch (choice) {
                    case "view" -> view.View(); // Pass the contentStream as an argument
                    case "register" -> register.RegisterTeams();
                    case "update" -> update.UpdateInformation();
                    case "delete" -> delete.DeleteInformation();
                    case "exit" -> {
                        System.out.print("\033[38;5;208m" + "Exiting Program" + "\033[0m");
                        for (int i = 0; i < 3; i++) {
                            try {
                                Thread.sleep(500);
                                System.out.print("\033[38;5;208m" + "." + "\033[0m");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println();
                        System.exit(0);
                    }
                    default -> System.out.println("\u001B[31mInvalid choice!\u001B[0m");
                }
            } while (!Objects.equals(choice, "exit"));
        }
    }
