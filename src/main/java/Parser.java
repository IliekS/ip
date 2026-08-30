/**
 * Parses raw user input into commands and arguments.
 */
public class Parser {
    /**
     * Separates the first command word from its remaining arguments.
     *
     * @param input raw command entered by the user
     * @return the parsed command and arguments
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
     * @param commandWord first word of the user's input
     * @return the matching command, or UNKNOWN when no command matches
     */
    private Command parseCommandWord(String commandWord) {
        switch (commandWord) {
            case "bye":
                return Command.BYE;
            case "list":
                return Command.LIST;
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
