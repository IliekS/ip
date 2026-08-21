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

    private static Task[] tasks = new Task[100];
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
        } else if (command.startsWith("todo ")) {
            createTodo(command);
        } else if (command.startsWith("deadline ")) {
            createDeadline(command);
        } else if (command.startsWith("event ")) {
            createEvent(command);
        } else {
            addTask(new Todo(command));
        }
    }

    private static void addTask(Task task) {
        tasks[currListLen] = task;
        currListLen++;
        System.out.println(BLUE + "Got it. I've added this task:" + RESET);
        System.out.println(GREEN + task + RESET);
        System.out.println(BLUE + "Now you have " + currListLen + " tasks in the list." + RESET);
    }

    /**
     * Adds a to-do task from a todo command.
     *
     * @param command the user's todo command
     */
    private static void createTodo(String command) {
        String description = command.substring("todo ".length()).trim();
        if (description.isEmpty()) {
            System.out.println(BLUE + "Invalid command format. Use: todo <description>" + RESET);
            return;
        }
        addTask(new Todo(description));
    }

    /**
     * Adds a deadline task from a deadline command.
     *
     * @param command the user's deadline command
     */
    private static void createDeadline(String command) {
        int byIndex = command.indexOf(" /by ");
        if (byIndex < 0) {
            System.out.println(BLUE + "Invalid command format. Use: deadline <description> /by <deadline>" + RESET);
            return;
        }
        String description = command.substring("deadline ".length(), byIndex).trim();
        String by = command.substring(byIndex + " /by ".length()).trim();
        if (description.isEmpty() || by.isEmpty()) {
            System.out.println(BLUE + "Invalid command format. Use: deadline <description> /by <deadline>" + RESET);
            return;
        }
        addTask(new Deadline(description, by));
    }

    /**
     * Adds an event task from an event command.
     *
     * @param command the user's event command
     */
    private static void createEvent(String command) {
        int fromIndex = command.indexOf(" /from ");
        int toIndex = command.indexOf(" /to ");
        if (fromIndex < 0 || toIndex < fromIndex) {
            System.out.println(BLUE + "Invalid command format. Use: event <description> /from <start> /to <end>" + RESET);
            return;
        }
        String description = command.substring("event ".length(), fromIndex).trim();
        String from = command.substring(fromIndex + " /from ".length(), toIndex).trim();
        String to = command.substring(toIndex + " /to ".length()).trim();
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            System.out.println(BLUE + "Invalid command format. Use: event <description> /from <start> /to <end>" + RESET);
            return;
        }
        addTask(new Event(description, from, to));
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
            System.out.println(GREEN + (i + 1) + "." + tasks[i] + RESET);
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
            Task task = tasks[taskNumber - 1];
            task.markAsDone();
            System.out.println(BLUE + "I marked this task as done:" + RESET);
            System.out.println(GREEN + task + RESET);
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
            Task task = tasks[taskNumber - 1];
            task.markAsNotDone();
            System.out.println(BLUE + "I marked this task as not done:" + RESET);
            System.out.println(GREEN + task + RESET);
        } catch (NumberFormatException e) {
            System.out.println(BLUE + "Invalid task number format." + RESET);
        }
    }
}
