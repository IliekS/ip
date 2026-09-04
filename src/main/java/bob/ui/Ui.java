package bob.ui;

import java.util.Scanner;
import java.io.PrintStream;

import bob.task.Task;
import bob.task.TaskList;

/**
 * Handles console input and output for Bob.
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String BLUE = "\u001B[34m";
    private static final String GREEN = "\u001B[32m";
    private static final String RESET = "\u001B[0m";

    private final Scanner scanner;
    private final PrintStream output;

    /**
     * Creates a UI that reads from standard input.
     */
    public Ui() {
        this(new Scanner(System.in), System.out);
    }

    /** Creates a UI using the supplied input and output streams. */
    public Ui(Scanner scanner, PrintStream output) {
        this.scanner = scanner;
        this.output = output;
    }

    /**
     * Displays Bob's welcome banner and greeting.
     */
    public void showWelcome() {
        output.println(BLUE + "Hello! I'm Bob.");
        output.println("What can I do for you?" + RESET);
    }

    /**
     * Returns whether another command is available from standard input.
     *
     * @return True if another command is available.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Returns the next command entered by the user.
     *
     * @return Next command.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the separator that appears before a command response.
     */
    public void showCommandStart() {
        output.println(DIVIDER);
        output.println();
    }

    /**
     * Displays the separator that appears after a command response.
     */
    public void showCommandEnd() {
        output.println(DIVIDER);
        output.println();
    }

    /**
     * Displays Bob's goodbye message.
     */
    public void showGoodbye() {
        output.println(BLUE + "Bye. Hope to see you again soon!" + RESET);
    }

    /**
     * Displays the required format for an incomplete command.
     *
     * @param format Command format to display.
     */
    public void showUsage(String format) {
        output.println(BLUE + "Invalid command format. Use: " + format + RESET);
    }

    /**
     * Displays a message for an unrecognized command.
     */
    public void showUnknownCommand() {
        output.println(BLUE + "I'm sorry, I don't understand that command." + RESET);
    }

    /**
     * Displays a newly added task and the updated task count.
     *
     * @param task Added task.
     * @param taskCount Updated number of tasks.
     */
    public void showAddedTask(Task task, int taskCount) {
        output.println(BLUE + "Got it. I've added this task:" + RESET);
        output.println(GREEN + task + RESET);
        output.println(BLUE + "Now you have " + taskCount + " tasks in the list." + RESET);
    }

    /**
     * Displays all tasks in their current order.
     *
     * @param tasks Tasks to display.
     */
    public void showTaskList(TaskList tasks) {
        if (tasks.isEmpty()) {
            output.println(BLUE + "No tasks yet!" + RESET);
            return;
        }
        output.println(BLUE + "Here are the tasks in your list:" + RESET);
        for (int i = 0; i < tasks.size(); i++) {
            output.println(GREEN + (i + 1) + "." + tasks.get(i + 1) + RESET);
        }
    }

    /**
     * Displays tasks matching a find command in their original order.
     *
     * @param tasks Matching tasks to display.
     */
    public void showMatchingTasks(TaskList tasks) {
        output.println(BLUE + "Here are the matching tasks in your list:" + RESET);
        for (int i = 0; i < tasks.size(); i++) {
            output.println(GREEN + (i + 1) + "." + tasks.get(i + 1) + RESET);
        }
    }

    /**
     * Displays a task after its completion status changes.
     *
     * @param task Updated task.
     * @param isDone Whether the task is complete.
     */
    public void showMarkedTask(Task task, boolean isDone) {
        String statusMessage = isDone
                ? "I marked this task as done:"
                : "I marked this task as not done:";
        output.println(BLUE + statusMessage + RESET);
        output.println(GREEN + task + RESET);
    }

    /**
     * Displays a removed task and the updated task count.
     *
     * @param task Removed task.
     * @param taskCount Updated number of tasks.
     */
    public void showDeletedTask(Task task, int taskCount) {
        output.println(BLUE + "Noted. I've removed this task:" + RESET);
        output.println(GREEN + task + RESET);
        output.println(BLUE + "Now you have " + taskCount + " tasks in the list." + RESET);
    }

    /**
     * Displays a message for a task number outside the current list.
     */
    public void showInvalidTaskNumber() {
        output.println(BLUE + "Invalid task number." + RESET);
    }

    /**
     * Displays a message for a task number that is not an integer.
     */
    public void showInvalidTaskNumberFormat() {
        output.println(BLUE + "Invalid task number format." + RESET);
    }

    /**
     * Displays the accepted date and date-time input formats.
     */
    public void showInvalidDateTime() {
        output.println(BLUE + "Invalid date or time. Use dd/MM/yyyy or "
                + "dd/MM/yyyy HHmm with a 24-hour time." + RESET);
    }

    /**
     * Reports that saved tasks could not be loaded.
     */
    public void showLoadingError() {
        output.println("Warning: could not load tasks from the data file.");
    }

    /**
     * Reports that tasks could not be saved.
     */
    public void showSavingError() {
        output.println("Warning: could not save tasks to the data file.");
    }
}
