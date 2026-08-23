import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * This class encapsulates a Deadline task
 *
 * @author LEE-wz
 */
public class Deadline extends Task{
    /**
     * Date or time that this deadline task has to be finished
     */
    protected String by;

    /**
     * Constructor for a Deadline task
     *
     * @param description The description of this Deadline task
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the string representation of the Deadline task
     *
     * @return A string representing the task
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.by + ")";
    }
}
