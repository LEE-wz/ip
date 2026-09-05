package remy.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import remy.task.Task;
import remy.task.Todo;

/**
 * Tests task-related messages displayed by {@link Ui}.
 */
class UiTest {
    private PrintStream originalStandardOut;
    private ByteArrayOutputStream output;

    @BeforeEach
    void setUp() {
        originalStandardOut = System.out;
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalStandardOut);
    }

    @Test
    void showMatchingTasks_matchingTasks_expectedHeadingAndNumberedTasksDisplayed() {
        Task firstTask = new Todo("read book");
        firstTask.markAsDone();
        Task secondTask = new Todo("return book");
        secondTask.markAsDone();

        new Ui().showMatchingTasks(List.of(firstTask, secondTask));

        String expectedOutput = """
                ____________________________________________________________

                Here are the matching tasks in your list:
                1. [T][X] read book
                2. [T][X] return book
                ____________________________________________________________

                """;
        assertEquals(expectedOutput, getNormalizedOutput());
    }

    @Test
    void showTaskList_tasks_expectedHeadingAndNumberedTasksDisplayed() {
        Task firstTask = new Todo("read book");
        Task secondTask = new Todo("return book");

        new Ui().showTaskList(List.of(firstTask, secondTask));

        String expectedOutput = """
                ____________________________________________________________

                Here are the tasks in your list:
                1. [T][ ] read book
                2. [T][ ] return book
                ____________________________________________________________

                """;
        assertEquals(expectedOutput, getNormalizedOutput());
    }

    @Test
    void showFarewell_noArguments_farewellBetweenDividersDisplayed() {
        new Ui().showFarewell();

        String expectedOutput = """
                ____________________________________________________________
                Cya. Call me again when you need me!
                ____________________________________________________________

                """;
        assertEquals(expectedOutput, getNormalizedOutput());
    }

    private String getNormalizedOutput() {
        return output.toString().replace(System.lineSeparator(), "\n");
    }
}
