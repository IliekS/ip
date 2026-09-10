package bob.parser;

import java.time.format.DateTimeParseException;

import bob.command.Command;
import bob.command.ParsedCommand;
import bob.task.Deadline;

/**
 * Parses raw user input into commands and arguments.
 */
public class Parser {
    private static final String DEADLINE_SEPARATOR = " /by ";

    /**
     * Parses deadline arguments into a task without saving it.
     *
     * @param arguments Text following the deadline command word.
     * @return Deadline containing the parsed description and date or date-time.
     * @throws IllegalArgumentException If the separator or either argument is missing.
     * @throws DateTimeParseException If the deadline date or date-time is invalid.
     */
    public Deadline parseDeadline(String arguments) {
        int byIndex = arguments.indexOf(DEADLINE_SEPARATOR);
        if (byIndex <= 0) {
            throw new IllegalArgumentException("Missing deadline description or /by separator");
        }
        String description = arguments.substring(0, byIndex).trim();
        String by = arguments.substring(byIndex + DEADLINE_SEPARATOR.length()).trim();
        if (description.isEmpty() || by.isEmpty()) {
            throw new IllegalArgumentException("Deadline description and date must not be empty");
        }
        return new Deadline(description, by);
    }

    /**
     * Separates the first command word from its remaining arguments.
     *
     * @param input Raw command entered by the user.
     * @return The parsed command and arguments.
     */
    public ParsedCommand parse(String input) {
        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            return new ParsedCommand(Command.UNKNOWN, "");
        }

        String[] parts = trimmedInput.split("\\s+", 2);
        Command command = parseCommandWord(parts[0]);
        String arguments = parts.length == 2 ? parts[1].trim() : "";
        return new ParsedCommand(command, arguments);
    }

    /**
     * Converts a command word into a supported command type.
     *
     * @param commandWord First word of the user's input.
     * @return The matching command, or UNKNOWN when no command matches.
     */
    private Command parseCommandWord(String commandWord) {
        switch (commandWord) {
            case "bye":
                return Command.BYE;
            case "list":
                return Command.LIST;
            case "find":
                return Command.FIND;
            case "mark":
                return Command.MARK;
            case "unmark":
                return Command.UNMARK;
            case "delete":
                return Command.DELETE;
            case "todo":
                return Command.TODO;
            case "deadline":
                return Command.DEADLINE;
            case "event":
                return Command.EVENT;
            default:
                return Command.UNKNOWN;
        }
    }
}
