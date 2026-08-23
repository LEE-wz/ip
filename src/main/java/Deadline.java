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
    /** Date or time that this deadline task has to be finished in String format */
    protected String by;

    /** Date or time that this deadline task has to be finished in LocalDateTime format */
    protected LocalDateTime byDateTime;

    /**
     * Constructor for a Deadline task
     *
     * @param description The description of this Deadline task
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
        this.byDateTime = null;
    }

    /**
     * Constructor for a Deadline task
     *
     * @param description The description of this Deadline task
     * @param byDateTime The deadline of this Deadline task in LocalDateTime format
     */
    public Deadline(String description, LocalDateTime byDateTime) {
        super(description);
        this.by = "";
        this.byDateTime = byDateTime;
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
