package remy.command;

import remy.storage.Storage;
import remy.task.Task;
import remy.task.TaskList;
import remy.ui.Ui;

/**
 * Adds a task to the task list.
 */
public class AddCommand extends Command {
    /** Task to add. */
    private final Task task;

    /**
     * Creates a command that adds the given task.
     *
     * @param task task to add
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);
        saveTasks(tasks, ui, storage);
        ui.showTaskAdded(task, tasks.size());
    }
}
