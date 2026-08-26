package remy.command;

import remy.exception.RemyException;
import remy.storage.Storage;
import remy.task.Task;
import remy.task.TaskList;
import remy.ui.Ui;

/**
 * Marks a task as done or undone at a user-provided one-based index.
 */
public class MarkCommand extends Command {
    /** One-based index of the task to update. */
    private final int taskIndex;

    /** Whether to mark the task as done. */
    private final boolean isDone;

    /**
     * Creates a command that updates the completion state of a task.
     *
     * @param taskIndex one-based index of the task to update
     * @param isDone true to mark done, false to mark undone
     */
    public MarkCommand(int taskIndex, boolean isDone) {
        this.taskIndex = taskIndex;
        this.isDone = isDone;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (taskIndex > tasks.size() || taskIndex < 1) {
            throw new RemyException("your index is out of range :/");
        }

        Task task = tasks.get(taskIndex - 1);
        if (isDone) {
            task.markAsDone();
            ui.showTaskMarkedAsDone(task);
        } else {
            task.markAsUndone();
            ui.showTaskMarkedAsUndone(task);
        }
        saveTasks(tasks, ui, storage);
    }
}
