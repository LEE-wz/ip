import java.util.List;

/**
 * Handles all messages shown to the user by the Remy application.
 */
public class Ui {

    /** Displays the greeting shown when a chat session begins. */
    public void showGreeting() {
        System.out.println(
                """
                        ____________________________________________________________
                         _____                     \s
                        |  __ \\                    \s
                        | |__) | ___ _ __ ___  _   _
                        |  _  / / _ \\ '_ ` _ \\| | | |
                        | | \\ \\|  __/ | | | | | |_| |
                        |_|  \\_\\\\___|_| |_| |_|\\__, |
                                                __/ |
                                               |___/\s
                        Yo! I am Remy the rat from Ratatouille.
                        How can I serve you today? :D

                        ____________________________________________________________
                        """);
    }

    /** Displays the farewell shown when a chat session ends. */
    public void showFarewell() {
        System.out.println(
                """
                        ____________________________________________________________
                        Cya. Call me again when you need me!
                        ____________________________________________________________
                        """);
    }

    /**
     * Displays an error message produced while handling a command.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays every task in the task list.
     *
     * @param tasks tasks to display
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println("____________________________________________________________\n");
        for (Task task : tasks) {
            System.out.println((tasks.indexOf(task) + 1) + ". " + task);
        }
        System.out.println("____________________________________________________________\n");
    }

    /**
     * Displays the confirmation after a task has been added.
     *
     * @param task the task that was added
     * @param taskCount the number of tasks now in the list
     */
    public void showTaskAdded(Task task, int taskCount) {
        String result =
                "____________________________________________________________\n"
                        + "Okay, I have helped you create a task:\n"
                        + task + "\n"
                        + "Now you have " + taskCount + " task(s) in the list. Better hurry before it piles up!\n"
                        + "____________________________________________________________\n";
        System.out.println(result);
    }

    /**
     * Displays the confirmation after a task has been deleted.
     *
     * @param task the task that was deleted
     * @param taskCount the number of tasks now in the list
     */
    public void showTaskDeleted(Task task, int taskCount) {
        String result =
                "____________________________________________________________\n"
                        + "Okay, I have helped you removed a task, remember to thank me:\n"
                        + task + "\n"
                        + "Now you have " + taskCount + " tasks in the list. Good luck LOL.\n"
                        + "____________________________________________________________\n";
        System.out.println(result);
    }

    /**
     * Displays the confirmation after a task has been marked as done.
     *
     * @param task the task marked as done
     */
    public void showTaskMarkedAsDone(Task task) {
        System.out.println("____________________________________________________________\n");
        System.out.println(" Nice! I've marked this task as done:\n");
        System.out.println(task);
        System.out.println("____________________________________________________________\n");
    }

    /**
     * Displays the confirmation after a task has been marked as undone.
     *
     * @param task the task marked as undone
     */
    public void showTaskMarkedAsUndone(Task task) {
        System.out.println("____________________________________________________________\n");
        System.out.println(" OK, I've marked this task as not done yet:\n");
        System.out.println(task);
        System.out.println("____________________________________________________________\n");
    }

    /** Displays the error shown when saving tasks fails. */
    public void showSavingError() {
        System.out.println("Unable to save tasks to data/remy.txt");
    }

    /** Displays the error shown when loading tasks fails. */
    public void showLoadingError() {
        System.out.println("Unable to load saved tasks from data/remy.txt");
    }
}
