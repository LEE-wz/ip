package remy.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import remy.task.Deadline;
import remy.task.Event;
import remy.task.Task;
import remy.task.TaskList;
import remy.task.Todo;

/**
 * Tests task-file persistence through {@link Storage}.
 *
 * @author LEE-wz
 */
class StorageTest {
    /**
     * Temporary directory provided by JUnit for creating temporary files during tests.
     */
    @TempDir
    Path temporaryDirectory;

    /**
     * Tests that the load method returns an empty task list when the task file does not exist.
     *
     * @throws IOException if an I/O error occurs during the test
     */
    @Test
    void load_taskFileDoesNotExist_emptyTaskListReturned() throws IOException {
        Storage storage = new Storage(temporaryDirectory.resolve("tasks.txt").toString());

        TaskList loadedTasks = storage.load();

        assertTrue(loadedTasks.isEmpty());
    }

    /**
     * Tests that the save and load methods correctly persist and retrieve tasks of all types, preserving their completion status.
     *
     * @throws IOException if an I/O error occurs during the test
     */
    @Test
    void saveAndLoad_tasksOfAllTypes_tasksAndCompletionStatusPreserved() throws IOException {
        Task todo = new Todo("Read book");
        todo.markAsDone();
        Task deadline = new Deadline("Submit assignment", LocalDate.of(2026, 8, 23));
        Task event = new Event("Project meeting", LocalDateTime.of(2026, 8, 24, 14, 30),
                LocalDateTime.of(2026, 8, 24, 16, 0));
        TaskList tasks = new TaskList(List.of(todo, deadline, event));
        Path taskFile = temporaryDirectory.resolve("data").resolve("tasks.txt");
        Storage storage = new Storage(taskFile.toString());

        storage.save(tasks);
        TaskList loadedTasks = storage.load();

        assertTrue(Files.exists(taskFile));
        assertEquals(tasks.getTasks().stream().map(Task::toString).toList(),
                loadedTasks.getTasks().stream().map(Task::toString).toList());
    }

    /**
     * Tests that the load method ignores invalid lines in the task file and only loads valid tasks.
     *
     * @throws IOException if an I/O error occurs during the test
     */
    @Test
    void load_taskFileContainsInvalidLines_invalidLinesIgnored() throws IOException {
        Path taskFile = temporaryDirectory.resolve("tasks.txt");
        Files.writeString(taskFile, String.join(System.lineSeparator(),
                "[T][X] Read book",
                "not a task",
                "[D][ ] Missing deadline"));
        Storage storage = new Storage(taskFile.toString());

        TaskList loadedTasks = storage.load();

        assertEquals(List.of("[T][X] Read book"),
                loadedTasks.getTasks().stream().map(Task::toString).toList());
    }
}
