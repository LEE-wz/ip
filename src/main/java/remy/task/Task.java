package remy.task;

/**
 * Represents a task with a description and completion state.
 *
 * @author LEE-wz
 */
public class Task {
    /** Description supplied for this task. */
    protected String description;

    /** Whether this task has been completed. */
    protected boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description Description supplied by the user.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns this task's description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns this task's completion status icon.
     *
     * @return 'X' if this task is done
     *         ' ' if this task is not done
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Marks this task as done. */
    public void markAsDone() {
        this.isDone = true;
    }

    /** Marks this task as undone. */
    public void markAsUndone() {
        this.isDone = false;
    }

    /**
     * Returns this task's display representation.
     *
     * @return A string containing the status icon and description.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
