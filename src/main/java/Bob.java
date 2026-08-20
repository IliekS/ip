/**
 * Starts Bob, the chatbot.
 */
public class Bob {
    /**
     * Displays Bob's welcome banner, greeting, and farewell.
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
        System.out.println();
        System.out.println("Bye. Hope to see you again soon!");
    }
}
