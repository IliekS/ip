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
    private static boolean[] isDone = new boolean[100];
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
        System.out.println(BLUE + "Hello! I'm Bob.");
        System.out.println("What can I do for you?" + RESET);
        System.out.println(DIVIDER);
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            System.out.println(DIVIDER);
            System.out.println();
            String command = scanner.nextLine();
            executeCommand(command);
            System.out.println(DIVIDER);
            System.out.println();
        }
    }

    private static void executeCommand(String command) {
        if (command.equals("bye")) {
            exit(command);
        } else if (command.equals("list")) {
            list(command);
        } else if (command.startsWith("mark ")) {
            mark(command);
        } else if (command.startsWith("unmark ")) {
            unmark(command);
        } else {
            addTask(command);
        }
    }

    private static void addTask(String command) {
        list[currListLen] = command;
        currListLen++;
        System.out.println(BLUE + "Added: " + GREEN + command + RESET);
        return;
    }

    private static void exit(String command) {
        if (!command.equals("bye")) { return; }
        System.out.println(BLUE + "Bye. Hope to see you again soon!" + RESET);
        System.out.println(DIVIDER);
        System.exit(0);
    }

    private static void list(String command) {
        if (!command.equals("list")) { return; }
        if (currListLen == 0) {
            System.out.println(BLUE + "No tasks yet!" + RESET);
            return;
        }
        System.out.println(BLUE + "Here are the tasks in your list:" + RESET);

        for (int i = 0; i < currListLen; i++) {
            String mark = isDone[i] ? "[X] " : "[ ] ";
            System.out.println(GREEN + (i+1) + ". " + mark + " " + list[i] + RESET);
        }
    }

    private static void mark(String command) {
        if (!command.startsWith("mark ")) { return; }
        String[] parts = command.split(" ");
        if (parts.length != 2) {
            System.out.println(BLUE + "Invalid command format. Use: mark <task_number>" + RESET);
            return;
        }
        try {
            int taskNumber = Integer.parseInt(parts[1]);
            if (taskNumber < 1 || taskNumber > currListLen) {
                System.out.println(BLUE + "Invalid task number." + RESET);
                return;
            }
            isDone[taskNumber - 1] = true;
            System.out.println(BLUE + "I marked this task as done:" + RESET);
            System.out.println(GREEN + "[X] " + list[taskNumber - 1] + RESET);
        } catch (NumberFormatException e) {
            System.out.println(BLUE + "Invalid task number format." + RESET);
        }
    }

    /**
     * Marks the specified task as incomplete.
     *
     * @param command an unmark command followed by a task number
     */
    private static void unmark(String command) {
        if (!command.startsWith("unmark ")) { return; }
        String[] parts = command.split(" ");
        if (parts.length != 2) {
            System.out.println(BLUE + "Invalid command format. Use: unmark <task_number>" + RESET);
            return;
        }
        try {
            int taskNumber = Integer.parseInt(parts[1]);
            if (taskNumber < 1 || taskNumber > currListLen) {
                System.out.println(BLUE + "Invalid task number." + RESET);
                return;
            }
            isDone[taskNumber - 1] = false;
            System.out.println(BLUE + "I marked this task as not done:" + RESET);
            System.out.println(GREEN + "[ ] " + list[taskNumber - 1] + RESET);
        } catch (NumberFormatException e) {
            System.out.println(BLUE + "Invalid task number format." + RESET);
        }
    }
}
