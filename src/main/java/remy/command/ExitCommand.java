package remy.command;

import remy.storage.Storage;
import remy.task.TaskList;
import remy.ui.Ui;

/**
 * Ends the application loop.
 */
public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        // Ending the application does not change tasks or require output here.
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
