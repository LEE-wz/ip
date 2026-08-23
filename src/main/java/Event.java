/**
 * This class encapsulates an Event task
 *
 * @author LEE-wz
 */
public class Event extends Task{
    /**
     * The starting date/time of this Event task
     */
    protected String start;

    /**
     * The ending date/time of this Event task
     */
    protected String end;

    /**
     * Constructor for an Event task
     * @param description The description of this Event task
     * @param start The starting date/time of this Event task
     * @param end The ending date/time of this Event task
     */
    public Event(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the string representation of the Event task
     *
     * @return A string representing the task
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.start + " to: " + this.end + ")";
    }
}
