package bob.task;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Verifies task invariants shared by command input and saved records.
 */
public class TaskValidationTest {
    @Test
    public void constructor_invalidDescription_rejectsUnpersistableTask() {
        for (String description : new String[] {"", "  ", "a | b", "a\nb", "a\u001Bb"}) {
            assertThrows(InvalidTaskException.class, () -> new Todo(description));
        }
    }

    @Test
    public void event_startNotBeforeEnd_rejectsEqualReversedAndMixedEndpoints() {
        for (String[] period : new String[][] {
            {"2/12/2019", "2/12/2019"},
            {"3/12/2019", "2/12/2019"},
            {"2/12/2019 1400", "2/12/2019 1400"},
            {"2/12/2019 1600", "2/12/2019 1400"},
            {"2/12/2019 1400", "2/12/2019"}
        }) {
            assertThrows(InvalidTaskException.class, () -> new Event("meeting", period[0], period[1]));
        }
        assertDoesNotThrow(() -> new Event("meeting", "2/12/2019", "2/12/2019 1400"));
    }

    @Test
    public void hasSameDetails_normalizedDescriptionsAndStatus_detectsDuplicatesOnlyWithSameDatesAndType() {
        Todo completed = new Todo("Read   book");
        completed.markAsDone();
        assertTrue(completed.hasSameDetails(new Todo("read book")));
        assertFalse(completed.hasSameDetails(new Deadline("read book", "2/12/2019")));
        assertTrue(new Deadline("read", "2/12/2019").hasSameDetails(new Deadline("READ", "02/12/2019")));
        assertFalse(new Deadline("read", "2/12/2019").hasSameDetails(new Deadline("read", "3/12/2019")));
        assertTrue(new Event("meet", "2/12/2019", "3/12/2019")
                .hasSameDetails(new Event("MEET", "02/12/2019", "03/12/2019")));
        assertFalse(new Event("meet", "2/12/2019", "3/12/2019")
                .hasSameDetails(new Event("meet", "2/12/2019", "4/12/2019")));
    }
}
