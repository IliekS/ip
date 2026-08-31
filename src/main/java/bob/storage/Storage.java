package bob.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import bob.parser.DateTimeParser;
import bob.task.Deadline;
import bob.task.Event;
import bob.task.Task;
import bob.task.Todo;

/**
 * Loads and saves Bob's tasks in a text file.
 */
public class Storage {
    private static final String FIELD_SEPARATOR = " | ";
    private static final String EVENT_TIME_SEPARATOR = " to ";

    private final Path filePath;

    /**
     * Creates storage that uses a path relative to the application's working directory.
     *
     * @param firstPathPart First directory or file name in the relative path.
     * @param remainingPathParts Remaining directory or file names in the relative path.
     */
    public Storage(String firstPathPart, String... remainingPathParts) {
        this.filePath = Path.of(firstPathPart, remainingPathParts);
    }

    /**
     * Creates storage inside the ip project for common application launch locations.
     *
     * @return Storage configured for Bob's data file.
     */
    public static Storage createDefaultStorage() {
        Path workingDirectory = Path.of("").toAbsolutePath();
        if (Files.isDirectory(workingDirectory.resolve(Path.of("src", "main", "java")))) {
            return new Storage("data", "bob.txt");
        }
        if (Files.isDirectory(workingDirectory.resolve(Path.of("ip", "src", "main", "java")))) {
            return new Storage("ip", "data", "bob.txt");
        }
        return new Storage("data", "bob.txt");
    }

    /**
     * Loads all valid tasks from the data file.
     * Missing files are treated as an empty task list.
     *
     * @return Tasks read from the data file.
     * @throws IOException If the file cannot be read.
     */
    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> loadedTasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return loadedTasks;
        }

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            try {
                loadedTasks.add(parseTask(line));
            } catch (IllegalArgumentException | DateTimeParseException exception) {
                System.err.println("Warning: skipped invalid data on line " + (i + 1) + ".");
            }
        }
        return loadedTasks;
    }

    /**
     * Saves all tasks, creating the data directory when necessary.
     *
     * @param tasks Tasks to save.
     * @throws IOException If the data file cannot be written.
     */
    public void save(List<Task> tasks) throws IOException {
        Path parentDirectory = filePath.getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        ArrayList<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(formatTask(task));
        }
        Files.write(filePath, lines, StandardCharsets.UTF_8);
    }

    /**
     * Converts one saved data line into a task.
     *
     * @param line Saved task data.
     * @return Task represented by the line.
     * @throws IllegalArgumentException If the data is malformed.
     */
    private Task parseTask(String line) {
        String[] fields = line.split(" \\| ", -1);
        if (fields.length < 3) {
            throw new IllegalArgumentException("Not enough fields");
        }

        Task task;
        switch (fields[0]) {
            case "T":
                requireFieldCount(fields, 3);
                task = new Todo(fields[2]);
                break;
            case "D":
                requireFieldCount(fields, 4);
                task = new Deadline(fields[2], fields[3]);
                break;
            case "E":
                requireFieldCount(fields, 4);
                String[] times = fields[3].split(EVENT_TIME_SEPARATOR, 2);
                if (times.length != 2) {
                    throw new IllegalArgumentException("Invalid event period");
                }
                task = new Event(fields[2], times[0], times[1]);
                break;
            default:
                throw new IllegalArgumentException("Unknown task type");
        }

        if (parseDoneStatus(fields[1])) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Converts a task into its saved data representation.
     *
     * @param task Task to format.
     * @return Data line representing the task.
     */
    private String formatTask(Task task) {
        String status = task.isDone() ? "1" : "0";
        if (task instanceof Todo) {
            return "T" + FIELD_SEPARATOR + status + FIELD_SEPARATOR + task.getDescription();
        } else if (task instanceof Deadline deadline) {
            return "D" + FIELD_SEPARATOR + status + FIELD_SEPARATOR + task.getDescription()
                    + FIELD_SEPARATOR + DateTimeParser.formatForStorage(deadline.getBy());
        } else if (task instanceof Event event) {
            return "E" + FIELD_SEPARATOR + status + FIELD_SEPARATOR + task.getDescription()
                    + FIELD_SEPARATOR + DateTimeParser.formatForStorage(event.getFrom())
                    + EVENT_TIME_SEPARATOR + DateTimeParser.formatForStorage(event.getTo());
        }
        throw new IllegalArgumentException("Unsupported task type");
    }

    /**
     * Converts a saved completion-status field into a boolean.
     *
     * @param status Saved status value.
     * @return True for a completed task and false for an incomplete task.
     * @throws IllegalArgumentException If the status is neither 0 nor 1.
     */
    private boolean parseDoneStatus(String status) {
        if (status.equals("1")) {
            return true;
        } else if (status.equals("0")) {
            return false;
        }
        throw new IllegalArgumentException("Invalid task status");
    }

    /**
     * Verifies that a saved record has the expected number of non-empty fields.
     *
     * @param fields Fields parsed from a saved record.
     * @param expectedCount Required number of fields.
     * @throws IllegalArgumentException If the record structure is invalid.
     */
    private void requireFieldCount(String[] fields, int expectedCount) {
        if (fields.length != expectedCount) {
            throw new IllegalArgumentException("Incorrect number of fields");
        }
        for (String field : fields) {
            if (field.isEmpty()) {
                throw new IllegalArgumentException("Empty field");
            }
        }
    }
}
