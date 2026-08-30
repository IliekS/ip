import java.util.Scanner;

/**
 * Handles console input and output for Bob.
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String BLUE = "\u001B[34m";
    private static final String GREEN = "\u001B[32m";
    private static final String RESET = "\u001B[0m";

    private final Scanner scanner;

    /** Creates a UI that reads from standard input. */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /** Displays Bob's welcome banner and greeting. */
    public void showWelcome() {
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
    }

    /** Returns whether another command is available from standard input. */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /** Returns the next command entered by the user. */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Displays the separator that appears before a command response. */
    public void showCommandStart() {
        System.out.println(DIVIDER);
        System.out.println();
    }

    /** Displays the separator that appears after a command response. */
    public void showCommandEnd() {
        System.out.println(DIVIDER);
        System.out.println();
    }

    /** Displays Bob's goodbye message. */
    public void showGoodbye() {
        System.out.println(BLUE + "Bye. Hope to see you again soon!" + RESET);
    }

    /** Displays the required format for an incomplete command. */
    public void showUsage(String format) {
        System.out.println(BLUE + "Invalid command format. Use: " + format + RESET);
    }

    /** Displays a message for an unrecognized command. */
    public void showUnknownCommand() {
        System.out.println(BLUE + "I'm sorry, I don't understand that command." + RESET);
    }

    /** Displays a newly added task and the updated task count. */
    public void showAddedTask(Task task, int taskCount) {
        System.out.println(BLUE + "Got it. I've added this task:" + RESET);
        System.out.println(GREEN + task + RESET);
        System.out.println(BLUE + "Now you have " + taskCount + " tasks in the list." + RESET);
    }

    /** Displays all tasks in their current order. */
    public void showTaskList(TaskList tasks) {
        if (tasks.isEmpty()) {
            System.out.println(BLUE + "No tasks yet!" + RESET);
            return;
        }
        System.out.println(BLUE + "Here are the tasks in your list:" + RESET);
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(GREEN + (i + 1) + "." + tasks.get(i + 1) + RESET);
        }
    }

    /** Displays a task after its completion status changes. */
    public void showMarkedTask(Task task, boolean isDone) {
        String statusMessage = isDone
                ? "I marked this task as done:"
                : "I marked this task as not done:";
        System.out.println(BLUE + statusMessage + RESET);
        System.out.println(GREEN + task + RESET);
    }

    /** Displays a removed task and the updated task count. */
    public void showDeletedTask(Task task, int taskCount) {
        System.out.println(BLUE + "Noted. I've removed this task:" + RESET);
        System.out.println(GREEN + task + RESET);
        System.out.println(BLUE + "Now you have " + taskCount + " tasks in the list." + RESET);
    }

    /** Displays a message for a task number outside the current list. */
    public void showInvalidTaskNumber() {
        System.out.println(BLUE + "Invalid task number." + RESET);
    }

    /** Displays a message for a task number that is not an integer. */
    public void showInvalidTaskNumberFormat() {
        System.out.println(BLUE + "Invalid task number format." + RESET);
    }

    /** Reports that saved tasks could not be loaded. */
    public void showLoadingError() {
        System.err.println("Warning: could not load tasks from the data file.");
    }

    /** Reports that tasks could not be saved. */
    public void showSavingError() {
        System.err.println("Warning: could not save tasks to the data file.");
    }
}
