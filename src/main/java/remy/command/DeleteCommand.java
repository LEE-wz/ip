package remy.command;

import remy.exception.RemyException;
import remy.storage.Storage;
import remy.task.Task;
import remy.task.TaskList;
import remy.ui.Ui;

/**
 * Deletes a task at a user-provided one-based index.
 */
public class DeleteCommand extends Command {
    /** One-based index of the task to delete. */
    private final int taskIndex;

    /**
     * Creates a command that deletes the task at the given one-based index.
     *
     * @param taskIndex one-based index of the task to delete
     */
    public DeleteCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (tasks.isEmpty()) {
            throw new RemyException("There is no task for you to delete LOL.");
        }
        if (taskIndex > tasks.size() || taskIndex < 1) {
            throw new RemyException("Your index is out of range :/");
        }

        Task task = tasks.remove(taskIndex - 1);
        saveTasks(tasks, ui, storage);
        ui.showTaskDeleted(task, tasks.size());
    }
}
