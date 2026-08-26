package remy.task;

/**
 * This class encapsulates a To/do task
 *
 * @author LEE-wz
 */
public class Todo extends Task{

    /**
     * Constructor for a To/do task
     *
     * @param description The description of this To/do task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the string representation of the To/do task
     *
     * @return A string representing the task
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
