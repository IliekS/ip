package bob.storage;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import bob.task.Deadline;
import bob.task.Event;
import bob.task.Task;
import bob.task.Todo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests task serialization and error-tolerant loading performed by {@link Storage}.
 */
public class StorageTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    public void load_invalidAndDuplicateRecords_backsUpOriginalBeforeSavingRecovery() throws IOException {
        Path file = temporaryDirectory.resolve("bob.txt");
        String original = "T | 0 | first\nT | 1 | FIRST\nT | 0 |    \n"
                + "E | 0 | reversed | 03/12/2019 to 02/12/2019\n";
        Files.writeString(file, original);
        Storage storage = createStorage("bob.txt");

        List<Task> tasks = storage.load();

        assertEquals(1, tasks.size());
        assertEquals(3, storage.getSkippedLineCount());
        storage.save(tasks);
        try (Stream<Path> paths = Files.list(temporaryDirectory)) {
            Path backup = paths.filter(path -> path.toString().endsWith(".bak")).findFirst().orElseThrow();
            assertEquals(original, Files.readString(backup));
        }
        assertEquals(List.of("T | 0 | first"), Files.readAllLines(file));
    }

    @Test
    public void load_invalidEncoding_preventsOverwriteOfUnreadableFile() throws IOException {
        Path file = temporaryDirectory.resolve("bob.txt");
        byte[] original = {(byte) 0xc3, (byte) 0x28};
        Files.write(file, original);
        Storage storage = createStorage("bob.txt");

        assertThrows(IOException.class, storage::load);
        assertThrows(IOException.class, () -> storage.save(List.of(new Todo("new"))));
        assertEquals(2, Files.size(file));
        assertArrayEquals(original, Files.readAllBytes(file));
    }

    @Test
    public void save_invalidTask_preservesExistingFile() throws IOException {
        Storage storage = createStorage("bob.txt");
        storage.save(List.of(new Todo("original")));
        Task unsupported = new Task("unsupported") {
        };

        assertThrows(IllegalArgumentException.class, () -> storage.save(List.of(unsupported)));
        assertEquals(List.of("T | 0 | original"), Files.readAllLines(temporaryDirectory.resolve("bob.txt")));
    }

    @Test
    public void save_destinationIsDirectory_reportsFailureAndCleansTemporaryFile() throws IOException {
        Files.createDirectory(temporaryDirectory.resolve("bob.txt"));
        Files.writeString(temporaryDirectory.resolve("bob.txt/keep.txt"), "keep");

        assertThrows(IOException.class, () -> createStorage("bob.txt").save(List.of(new Todo("new"))));
        try (Stream<Path> paths = Files.list(temporaryDirectory)) {
            assertEquals(1, paths.count());
        }
        assertEquals("keep", Files.readString(temporaryDirectory.resolve("bob.txt/keep.txt")));
    }

    @Test
    public void load_missingFile_returnsEmptyTaskList() throws IOException {
        Storage storage = createStorage("missing.txt");

        assertTrue(storage.load().isEmpty());
    }

    @Test
    public void save_allTaskTypes_writesExpectedRecords() throws IOException {
        Todo todo = new Todo("read book");
        todo.markAsDone();
        List<Task> tasks = List.of(
                todo,
                new Deadline("return book", "2/12/2019 1800"),
                new Event("meeting", "3/12/2019 1400", "3/12/2019 1600"));
        Path filePath = temporaryDirectory.resolve("bob.txt");
        Storage storage = createStorage("bob.txt");

        storage.save(tasks);

        assertEquals(List.of(
                "T | 1 | read book",
                "D | 0 | return book | 02/12/2019 1800",
                "E | 0 | meeting | 03/12/2019 1400 to 03/12/2019 1600"),
                Files.readAllLines(filePath, StandardCharsets.UTF_8));
    }

    @Test
    public void load_validRecords_reconstructsTaskTypesDetailsAndStatuses() throws IOException {
        Path filePath = temporaryDirectory.resolve("bob.txt");
        Files.write(filePath, List.of(
                "T | 1 | read book",
                "D | 0 | return book | 02/12/2019 1800",
                "E | 0 | meeting | 03/12/2019 1400 to 03/12/2019 1600"),
                StandardCharsets.UTF_8);

        ArrayList<Task> tasks = createStorage("bob.txt").load();

        assertEquals(3, tasks.size());
        assertTrue(tasks.get(0) instanceof Todo);
        assertTrue(tasks.get(0).isDone());
        assertEquals("[D][ ] return book (by: Dec 02 2019 1800hrs)", tasks.get(1).toString());
        assertEquals("[E][ ] meeting (from: Dec 03 2019 1400hrs to: Dec 03 2019 1600hrs)",
                tasks.get(2).toString());
    }

    @Test
    public void load_mixedValidAndInvalidRecords_skipsInvalidRecords() throws IOException {
        Path filePath = temporaryDirectory.resolve("bob.txt");
        Files.write(filePath, List.of(
                "T | 0 | valid task",
                "X | 0 | unknown type",
                "T | 2 | invalid status",
                "D | 0 | bad date | 31/02/2019",
                "E | 0 | missing end | 03/12/2019 1400"),
                StandardCharsets.UTF_8);

        ArrayList<Task> tasks = createStorage("bob.txt").load();

        assertEquals(1, tasks.size());
        assertEquals("valid task", tasks.get(0).getDescription());
    }

    @Test
    public void save_fileInMissingDirectory_createsParentDirectories() throws IOException {
        Path nestedDirectory = temporaryDirectory.resolve(Path.of("nested", "data"));
        Storage storage = new Storage(nestedDirectory.toString(), "bob.txt");

        storage.save(List.of(new Todo("saved task")));

        assertTrue(Files.exists(nestedDirectory.resolve("bob.txt")));
    }

    @Test
    public void load_completedTimedTasks_restoresCompletionStatus() throws IOException {
        Files.write(temporaryDirectory.resolve("bob.txt"), List.of(
                "D | 1 | return book | 02/12/2019",
                "E | 1 | meeting | 03/12/2019 1400 to 03/12/2019 1600"), StandardCharsets.UTF_8);

        ArrayList<Task> tasks = createStorage("bob.txt").load();

        assertEquals(2, tasks.size());
        assertTrue(tasks.get(0) instanceof Deadline);
        assertTrue(tasks.get(1) instanceof Event);
        assertTrue(tasks.get(0).isDone());
        assertTrue(tasks.get(1).isDone());
    }

    @Test
    public void load_invalidFieldStructures_continuesToNextValidRecord() throws IOException {
        Files.write(temporaryDirectory.resolve("bob.txt"), List.of(
                "T | 0",
                "T | 0 | extra field | unexpected",
                "D | 0 | missing deadline",
                "E | 0 | missing period",
                "T | 0 | ",
                "T | 0 | valid task"), StandardCharsets.UTF_8);

        ArrayList<Task> tasks = createStorage("bob.txt").load();

        assertEquals(1, tasks.size());
        assertEquals("valid task", tasks.get(0).getDescription());
        assertFalse(tasks.get(0).isDone());
    }

    @Test
    public void save_unsupportedTaskType_exceptionThrown() {
        Storage storage = createStorage("bob.txt");
        Task unsupportedTask = new Task("unsupported") {
        };

        assertThrows(IllegalArgumentException.class,
                () -> storage.save(List.of(unsupportedTask)));
        assertFalse(Files.exists(temporaryDirectory.resolve("bob.txt")));
    }

    /**
     * Creates storage for a test file in the temporary directory.
     */
    private Storage createStorage(String fileName) {
        return new Storage(temporaryDirectory.toString(), fileName);
    }
}
