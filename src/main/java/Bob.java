import java.util.Scanner;

/**
 * Starts Bob, the chatbot.
 */
public class Bob {
    /** Separates each chatbot response in the terminal. */
    private static final String DIVIDER = "____________________________________________________________";
    private static final String BLUE = "\u001B[34m";
    private static final String GREEN = "\u001B[32m";
    private static final String RESET = "\u001B[0m";

    private static String[] list = new String[100];
    private static int currListLen = 0;
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
        System.out.println(GREEN + "Hello! I'm Bob.");
        System.out.println("What can I do for you?" + RESET);
        System.out.println(DIVIDER);
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            System.out.println(DIVIDER);
            System.out.println();
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                System.out.println(BLUE + "Bye. Hope to see you again soon!" + RESET);
                System.out.println(DIVIDER);
                break;
            }
            if (command.equals("list")) {
                if (currListLen == 0) {
                    System.out.println(GREEN + "Nothing in list yet!" + RESET);
                    continue;
                }
                for (int i = 0; i < currListLen; i++) {
                    System.out.println(GREEN + (i+1) + ". " + list[i] + RESET);
                }
            } else {
                list[currListLen] = command;
                currListLen++;
    
                System.out.println(GREEN + command + RESET);
            }
            System.out.println(DIVIDER);
            System.out.println();
        }
    }
}
