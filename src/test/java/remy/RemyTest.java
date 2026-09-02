package remy;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests command processing through Remy's GUI-facing response interface.
 */
class RemyTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void getResponse_taskCommands_commandsExecuteAgainstSameTaskList() {
        Remy remy = createRemy();

        String addResponse = remy.getResponse("todo prepare ingredients");
        String listResponse = remy.getResponse("list");
        String markResponse = remy.getResponse("mark 1");

        assertTrue(addResponse.contains("prepare ingredients"));
        assertTrue(listResponse.contains("1. [T][ ] prepare ingredients"));
        assertTrue(markResponse.contains("[T][X] prepare ingredients"));
    }

    @Test
    void getResponse_invalidAndBlankCommands_errorMessagesReturned() {
        Remy remy = createRemy();

        String unknownCommandResponse = remy.getResponse("cook dinner");
        String blankCommandResponse = remy.getResponse("   ");

        assertTrue(unknownCommandResponse.contains("no clue what you're talking about"));
        assertTrue(blankCommandResponse.contains("Please type a command"));
    }

    @Test
    void getResponse_listCommandWithNoTasks_emptyListMessageReturned() {
        Remy remy = createRemy();

        String response = remy.getResponse("list");

        assertTrue(response.contains("There are no tasks in your list yet."));
    }

    @Test
    void getResponse_byeCommand_sessionEndsAndFurtherCommandsAreRejected() {
        Remy remy = createRemy();

        String byeResponse = remy.getResponse("bye");
        String laterResponse = remy.getResponse("list");

        assertTrue(remy.hasExited());
        assertTrue(byeResponse.contains("Cya. Call me again when you need me!"));
        assertTrue(laterResponse.contains("This chat has ended"));
    }

    /** Returns a Remy instance with isolated test storage. */
    private Remy createRemy() {
        return new Remy(temporaryDirectory.resolve("tasks.txt").toString());
    }
}
