package remy.command;

import remy.storage.Storage;
import remy.task.Task;
import remy.task.TaskList;
import remy.ui.Ui;

/**
 * Adds a task to the task list.
 *
 * @author LEE-wz
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

    /**
     * Adds a task to tasklist and saves it in storage
     *
     * @param tasks task collection to operate on
     * @param ui user interface used to display results
     * @param storage persistence service used to save changes
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);
        saveTasks(tasks, ui, storage);
        ui.showTaskAdded(task, tasks.size());
    }
}
