import java.util.Scanner;

/**
 * Starts Bob, the chatbot.
 */
public class Bob {
    /** Separates each chatbot response in the terminal. */
    private static final String DIVIDER = "____________________________________________________________";

    /**
     * Displays Bob's welcome banner and responds to commands until the user says bye.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        String banner = " ____        _     \n"
                + "| __ )  ___ | |__  \n"
                + "|  _ \\ / _ \\| '_ \\ \n"
                + "| |_) | (_) | |_) |\n"
                + "|____/ \\___/|_.__/ \n";

        System.out.println(banner);
        System.out.println("Hello! I'm Bob.");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(DIVIDER);
                break;
            }

            System.out.println(command);
            System.out.println(DIVIDER);
        }
    }
}
