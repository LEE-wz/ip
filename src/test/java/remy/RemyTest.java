package remy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
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
    void getResponse_sortCommand_tasksReorderedAndExistingListOutputReturned() {
        Remy remy = createRemy();
        remy.getResponse("todo buy ingredients");
        remy.getResponse("deadline submit report /by 23/8/2026 1800");
        remy.getResponse("event workshop /from 25/8/2026 0900 /to 25/8/2026 1100");
        remy.getResponse("deadline pay fees /by 23/8/2026");
        remy.getResponse("event tutorial /from 23/8/2026 1400 /to 23/8/2026 1500");

        String response = remy.getResponse("sort /by date");

        String expectedResponse = """
                Here are the tasks in your list:
                1. [D][ ] pay fees (by: Aug 23 2026)
                2. [E][ ] tutorial (from: Aug 23 2026 14:00 to: Aug 23 2026 15:00)
                3. [D][ ] submit report (by: Aug 23 2026 18:00)
                4. [E][ ] workshop (from: Aug 25 2026 09:00 to: Aug 25 2026 11:00)
                5. [T][ ] buy ingredients
                """.strip();
        assertEquals(expectedResponse, response.replace(System.lineSeparator(), "\n"));
    }

    @Test
    void getResponse_sortThenReload_sortedIndicesAndPersistencePreserved() {
        Path taskFile = temporaryDirectory.resolve("persistent-tasks.txt");
        Remy remy = new Remy(taskFile.toString());
        remy.getResponse("deadline later task /by 25/9/2026");
        remy.getResponse("deadline earlier task /by 23/9/2026");
        remy.getResponse("sort /by date");

        String markResponse = remy.getResponse("mark 1");
        Remy reloadedRemy = new Remy(taskFile.toString());
        String reloadedList = reloadedRemy.getResponse("list");

        assertTrue(markResponse.contains("[D][X] earlier task"));
        assertTrue(reloadedList.indexOf("[D][X] earlier task")
                < reloadedList.indexOf("[D][ ] later task"));
    }

    @Test
    void getResponse_addAfterSorting_newTaskAppendedWithoutAutomaticResort() {
        Remy remy = createRemy();
        remy.getResponse("deadline later task /by 25/9/2026");
        remy.getResponse("deadline earlier task /by 23/9/2026");
        remy.getResponse("sort /by date");

        remy.getResponse("deadline newly added task /by 20/9/2026");
        String response = remy.getResponse("list");

        assertTrue(response.indexOf("earlier task") < response.indexOf("later task"));
        assertTrue(response.indexOf("later task") < response.indexOf("newly added task"));
    }

    @Test
    void getResponse_invalidSortCommand_errorReturnedAndOrderUnchanged() {
        Remy remy = createRemy();
        remy.getResponse("deadline later task /by 25/9/2026");
        remy.getResponse("deadline earlier task /by 23/9/2026");

        String errorResponse = remy.getResponse("sort /by name");
        String listResponse = remy.getResponse("list");

        String expectedError = """
                Invalid sort command.
                Use: sort /by date [/order asc|desc]
                Example: sort /by date /order asc
                """.strip();
        assertEquals(expectedError, errorResponse);
        assertTrue(listResponse.indexOf("later task") < listResponse.indexOf("earlier task"));
    }

    @Test
    void getResponse_findAfterSorting_matchesFollowCanonicalRelativeOrder() {
        Remy remy = createRemy();
        remy.getResponse("todo draft report notes");
        remy.getResponse("deadline submit report later /by 25/9/2026");
        remy.getResponse("deadline submit report earlier /by 23/9/2026");
        remy.getResponse("sort /by date");

        String response = remy.getResponse("find report");

        assertTrue(response.indexOf("submit report earlier") < response.indexOf("submit report later"));
        assertTrue(response.indexOf("submit report later") < response.indexOf("draft report notes"));
    }

    @Test
    void getResponse_sortEmptyTaskList_emptyResponseAndTaskFileCreated() throws IOException {
        Path taskFile = temporaryDirectory.resolve("empty-tasks.txt");
        Remy remy = new Remy(taskFile.toString());

        String response = remy.getResponse("sort /by date");

        assertEquals("There are no tasks in your list yet.", response);
        assertTrue(Files.exists(taskFile));
        assertTrue(Files.readString(taskFile).isEmpty());
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
