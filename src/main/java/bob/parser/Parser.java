package bob.parser;

import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bob.command.Command;
import bob.command.ParsedCommand;
import bob.task.Deadline;
import bob.task.Event;

/**
 * Parses raw user input into commands and arguments.
 */
public class Parser {
    private static final Pattern PARAMETER = Pattern.compile("(?U)(?<!\\S)/([A-Za-z]+)(?=\\s|$)");

    /**
     * Parses deadline arguments into a task without saving it.
     *
     * @param arguments Text following the deadline command word.
     * @return Deadline containing the parsed description and date or date-time.
     * @throws IllegalArgumentException If the separator or either argument is missing.
     * @throws DateTimeParseException If the deadline date or date-time is invalid.
     */
    public Deadline parseDeadline(String arguments) {
        String[] fields = parseFields(arguments, "by");
        return new Deadline(fields[0], fields[1]);
    }

    /**
     * Parses an event with exactly one /from parameter followed by one /to parameter.
     */
    public Event parseEvent(String arguments) {
        String[] fields = parseFields(arguments, "from", "to");
        return new Event(fields[0], fields[1], fields[2]);
    }

    /**
     * Extracts a description and required parameters, rejecting missing, repeated, or unexpected flags.
     */
    private String[] parseFields(String arguments, String... parameters) {
        Matcher matcher = PARAMETER.matcher(arguments);
        String[] fields = new String[parameters.length + 1];
        int fieldIndex = 0;
        int start = 0;
        while (matcher.find()) {
            if (fieldIndex >= parameters.length || !matcher.group(1).equals(parameters[fieldIndex])) {
                throw new IllegalArgumentException("Unexpected or repeated parameter");
            }
            fields[fieldIndex++] = arguments.substring(start, matcher.start()).strip();
            start = matcher.end();
        }
        if (fieldIndex != parameters.length) {
            throw new IllegalArgumentException("Missing parameter");
        }
        fields[fieldIndex] = arguments.substring(start).strip();
        for (String field : fields) {
            if (field.isEmpty()) {
                throw new IllegalArgumentException("Missing parameter value or description");
            }
        }
        return fields;
    }

    /**
     * Separates the first command word from its remaining arguments.
     *
     * @param input Raw command entered by the user.
     * @return The parsed command and arguments.
     */
    public ParsedCommand parse(String input) {
        String trimmedInput = input == null ? "" : input.strip();
        if (trimmedInput.isEmpty()) {
            return new ParsedCommand(Command.UNKNOWN, "");
        }

        String[] parts = trimmedInput.split("(?U)\\s+", 2);
        Command command = parseCommandWord(parts[0]);
        String arguments = parts.length == 2 ? parts[1].trim() : "";
        return new ParsedCommand(command, arguments);
    }

    /**
     * Converts a full command word or short alias into a supported command type.
     *
     * @param commandWord First word of the user's input.
     * @return The matching command, or UNKNOWN when no command matches.
     */
    private Command parseCommandWord(String commandWord) {
        // parse() handles blank input before extracting the first word.
        assert commandWord != null && !commandWord.isEmpty() : "Command word must already be extracted";
        switch (commandWord) {
            case "bye", "b":
                return Command.BYE;
            case "list", "l":
                return Command.LIST;
            case "find", "f":
                return Command.FIND;
            case "mark", "m":
                return Command.MARK;
            case "unmark", "u":
                return Command.UNMARK;
            case "delete", "del":
                return Command.DELETE;
            case "todo", "t":
                return Command.TODO;
            case "deadline", "d":
                return Command.DEADLINE;
            case "event", "e":
                return Command.EVENT;
            default:
                return Command.UNKNOWN;
        }
    }
}
