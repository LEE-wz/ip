package remy.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests the task collection operations provided by {@link TaskList}.
 */
class TaskListTest {

    @Test
    void constructor_noArguments_emptyTaskListCreated() {
        TaskList taskList = new TaskList();

        assertTrue(taskList.isEmpty());
        assertEquals(0, taskList.size());
    }

    @Test
    void constructor_initialTasksSourceListChanges_taskListRemainsUnchanged() {
        Task firstTask = new Todo("Read book");
        List<Task> initialTasks = new java.util.ArrayList<>(List.of(firstTask));

        TaskList taskList = new TaskList(initialTasks);
        initialTasks.add(new Todo("Submit assignment"));

        assertEquals(1, taskList.size());
        assertEquals(firstTask, taskList.get(0));
    }

    @Test
    void add_task_taskAppendedAndSizeIncreases() {
        Task task = new Todo("Read book");
        TaskList taskList = new TaskList();

        taskList.add(task);

        assertFalse(taskList.isEmpty());
        assertEquals(1, taskList.size());
        assertEquals(task, taskList.get(0));
    }

    @Test
    void get_validIndex_correspondingTaskReturned() {
        Task firstTask = new Todo("Read book");
        Task secondTask = new Todo("Submit assignment");
        TaskList taskList = new TaskList(List.of(firstTask, secondTask));

        assertEquals(secondTask, taskList.get(1));
    }

    @Test
    void remove_middleTask_removedTaskReturnedAndRemainingTasksRetainOrder() {
        Task firstTask = new Todo("Read book");
        Task removedTask = new Todo("Submit assignment");
        Task lastTask = new Todo("Prepare presentation");
        TaskList taskList = new TaskList(List.of(firstTask, removedTask, lastTask));

        Task result = taskList.remove(1);

        assertEquals(removedTask, result);
        assertEquals(2, taskList.size());
        assertIterableEquals(List.of(firstTask, lastTask), taskList.getTasks());
    }

    @Test
    void isEmpty_onlyTaskRemoved_taskListBecomesEmpty() {
        TaskList taskList = new TaskList(List.of(new Todo("Read book")));

        taskList.remove(0);

        assertTrue(taskList.isEmpty());
    }

    @Test
    void find_matchingKeywordIgnoringCase_matchingTasksReturnedInListOrder() {
        Task firstMatch = new Todo("Read book");
        Task nonMatch = new Todo("Submit assignment");
        Task secondMatch = new Todo("Return BOOK");
        TaskList taskList = new TaskList(List.of(firstMatch, nonMatch, secondMatch));

        List<Task> matches = taskList.find("bOoK");

        assertIterableEquals(List.of(firstMatch, secondMatch), matches);
    }

    @Test
    void find_noMatchingKeyword_emptyListReturned() {
        TaskList taskList = new TaskList(List.of(new Todo("Read book")));

        assertTrue(taskList.find("assignment").isEmpty());
    }

    @Test
    void getTasks_emptyTaskList_emptyUnmodifiableListReturned() {
        List<Task> tasks = new TaskList().getTasks();

        assertTrue(tasks.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> tasks.add(new Todo("Read book")));
    }

    @Test
    void getTasks_taskListContainsTasks_tasksReturnedInAdditionOrder() {
        Task firstTask = new Todo("Read book");
        Task secondTask = new Todo("Submit assignment");
        TaskList taskList = new TaskList();
        taskList.add(firstTask);
        taskList.add(secondTask);

        List<Task> tasks = taskList.getTasks();

        assertIterableEquals(List.of(firstTask, secondTask), tasks);
    }

    @Test
    void getTasks_taskListChangesAfterSnapshot_snapshotRemainsUnchanged() {
        Task firstTask = new Todo("Read book");
        TaskList taskList = new TaskList();
        taskList.add(firstTask);
        List<Task> tasks = taskList.getTasks();

        taskList.add(new Todo("Submit assignment"));

        assertEquals(List.of(firstTask), tasks);
    }
}
