/**
 * The Task class is a public class that encapsulates a task to be completed by the user.
 *
 * @author LEE-wz
 */
public class Task {
    /** The description of the task */
    protected String description;

    /** The state of the task whether it is done or not */
    protected boolean isDone;

    /**
     * Creates a task with the given description, and it is automatically marked as undone.
     *
     * @param description Description of the task specified by user
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon of the task based on whether it is done or not.
     *
     * @return 'X' if this task is done
     *         ' ' if this task is not done
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Marks this task as done and prints the success message for the user.
     */
    public void markAsDone() {
        this.isDone = true;

        System.out.println("____________________________________________________________\n");
        System.out.println(" Nice! I've marked this task as done:\n");
        System.out.println(this.toString());
        System.out.println("____________________________________________________________\n");
    }

    /**
     * Marks this task as undone and prints the success message for the user.
     */
    public void markAsUndone() {
        this.isDone = false;

        System.out.println("____________________________________________________________\n");
        System.out.println(" OK, I've marked this task as not done yet:\n");
        System.out.println(this.toString());
        System.out.println("____________________________________________________________\n");
    }

    /**
     * Return the string representation of this task
     *
     * @return A string consisting of its status icon and description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + this.description;
    }
}
