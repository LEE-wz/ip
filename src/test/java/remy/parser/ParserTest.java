package remy.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import remy.command.Command;
import remy.command.ExitCommand;
import remy.command.FindCommand;
import remy.command.ListCommand;
import remy.exception.RemyException;
import remy.storage.Storage;
import remy.task.Task;
import remy.task.TaskList;
import remy.task.Todo;
import remy.ui.Ui;

/**
 * Tests conversion of user input into commands and resulting task changes.
 */
class ParserTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void parse_exitListAndFindCommands_expectedCommandTypesReturned() {
        Parser parser = new Parser();

        Command exitCommand = parser.parse("bye");
        Command listCommand = parser.parse("list");
        Command findCommand = parser.parse("find book");

        assertInstanceOf(ExitCommand.class, exitCommand);
        assertTrue(exitCommand.isExit());
        assertInstanceOf(ListCommand.class, listCommand);
        assertFalse(listCommand.isExit());
        assertInstanceOf(FindCommand.class, findCommand);
        assertFalse(findCommand.isExit());
    }

    @Test
    void parse_taskCreationCommands_expectedTasksAdded() {
        Parser parser = new Parser();
        TaskList tasks = new TaskList();

        execute(parser.parse("todo Read book"), tasks);
        execute(parser.parse("deadline Submit assignment /by 23/8/2026"), tasks);
        execute(parser.parse("event Project meeting /from 23/8/2026 1430 /to 23/8/2026 1600"), tasks);

        assertEquals(List.of(
                "[T][ ] Read book",
                "[D][ ] Submit assignment (by: Aug 23 2026)",
                "[E][ ] Project meeting (from: Aug 23 2026 14:30 to: Aug 23 2026 16:00)"),
                tasks.getTasks().stream().map(Task::toString).toList());
    }

    @Test
    void parse_indexCommands_expectedTaskChangesApplied() {
        Parser parser = new Parser();
        TaskList tasks = new TaskList(List.of(new Todo("Read book")));

        execute(parser.parse("mark 1"), tasks);
        assertEquals("[T][X] Read book", tasks.get(0).toString());

        execute(parser.parse("unmark 1"), tasks);
        assertEquals("[T][ ] Read book", tasks.get(0).toString());

        execute(parser.parse("delete 1"), tasks);
        assertTrue(tasks.isEmpty());
    }

    @Test
    void parse_unknownOrInvalidCommands_remyExceptionThrown() {
        Parser parser = new Parser();

        assertThrows(RemyException.class, () -> parser.parse(null));
        assertThrows(RemyException.class, () -> parser.parse("todo"));
        assertThrows(RemyException.class, () -> parser.parse("deadline submit /by tomorrow"));
        assertThrows(RemyException.class, () -> parser.parse("event meeting /from 23/8/2026"));
        assertThrows(RemyException.class, () -> parser.parse("mark one"));
        assertThrows(RemyException.class, () -> parser.parse("find"));
    }

    /** Executes a parsed command using isolated persistence for the test. */
    private void execute(Command command, TaskList tasks) {
        Storage storage = new Storage(temporaryDirectory.resolve("tasks.txt").toString());
        command.execute(tasks, new Ui(), storage);
    }
}
