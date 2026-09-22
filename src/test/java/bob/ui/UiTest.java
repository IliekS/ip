package bob.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

/**
 * Verifies error status used to style chatbot responses.
 */
public class UiTest {
    @Test
    public void showWelcome_afterSeparateStartupWarning_containsOnlyGreetingWithoutErrorStatus() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Ui ui = new Ui(new Scanner(""), new PrintStream(output, true, StandardCharsets.UTF_8));
        ui.showLoadingError();
        assertTrue(ui.hasError());
        assertEquals("Warning: could not load tasks from the data file.",
                MainWindow.cleanOutput(output.toString(StandardCharsets.UTF_8)));

        output.reset();
        ui.resetError();
        ui.showWelcome();

        assertEquals("hi im bob", MainWindow.cleanOutput(output.toString(StandardCharsets.UTF_8)));
        assertFalse(ui.hasError());
    }

    @Test
    public void errorStatus_allErrorResponses_setsStatusUntilReset() {
        Ui ui = new Ui(new Scanner(""), new PrintStream(new ByteArrayOutputStream()));
        List<Runnable> errors = List.of(() -> ui.showUsage("todo <description>"),
                ui::showUnknownCommand, ui::showInvalidTaskNumber, ui::showInvalidTaskNumberFormat,
                ui::showInvalidDateTime, ui::showLoadingError, ui::showSavingError);

        assertFalse(ui.hasError());
        for (Runnable error : errors) {
            error.run();
            assertTrue(ui.hasError());
            ui.showGoodbye();
            assertTrue(ui.hasError());
            ui.resetError();
            ui.showWelcome();
            assertFalse(ui.hasError());
        }
    }
}
