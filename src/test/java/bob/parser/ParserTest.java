package bob.parser;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import bob.command.Command;
import bob.command.ParsedCommand;
import org.junit.jupiter.api.Test;

/**
 * Tests recognition and argument extraction performed by {@link Parser}.
 */
public class ParserTest {
    private final Parser parser = new Parser();

    @Test
    public void parse_supportedCommandWords_returnsMatchingCommands() {
        assertAll(
                () -> assertEquals(Command.BYE, parser.parse("bye").getCommand()),
                () -> assertEquals(Command.LIST, parser.parse("list").getCommand()),
                () -> assertEquals(Command.FIND, parser.parse("find book").getCommand()),
                () -> assertEquals(Command.MARK, parser.parse("mark 1").getCommand()),
                () -> assertEquals(Command.UNMARK, parser.parse("unmark 1").getCommand()),
                () -> assertEquals(Command.DELETE, parser.parse("delete 1").getCommand()),
                () -> assertEquals(Command.TODO, parser.parse("todo read").getCommand()),
                () -> assertEquals(Command.DEADLINE, parser.parse("deadline read").getCommand()),
                () -> assertEquals(Command.EVENT, parser.parse("event meeting").getCommand()));
    }

    @Test
    public void parse_commandWithArguments_returnsArgumentsWithoutOuterWhitespace() {
        ParsedCommand result = parser.parse("  todo    read   a book  ");

        assertEquals(Command.TODO, result.getCommand());
        assertEquals("read   a book", result.getArguments());
    }

    @Test
    public void parse_commandWithoutArguments_returnsEmptyArguments() {
        assertEquals("", parser.parse("list").getArguments());
    }

    @Test
    public void parse_emptyInput_returnsUnknownCommand() {
        assertEquals(Command.UNKNOWN, parser.parse("   ").getCommand());
    }

    @Test
    public void parse_similarCommandPrefix_returnsUnknownCommand() {
        assertEquals(Command.UNKNOWN, parser.parse("deleteLater 1").getCommand());
    }

    @Test
    public void parse_commandWithDifferentCapitalization_returnsUnknownCommand() {
        assertEquals(Command.UNKNOWN, parser.parse("TODO read").getCommand());
    }
}
