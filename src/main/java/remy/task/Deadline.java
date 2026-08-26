package remy.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class encapsulates a Deadline task
 *
 * @author LEE-wz
 */
public class Deadline extends Task{

    /** Format used when displaying date-only deadlines. */
    private static final DateTimeFormatter DATE_DISPLAY_FORMATTER =
        DateTimeFormatter.ofPattern("MMM dd yyyy");

    /** Format used when displaying deadlines that include a time. */
    private static final DateTimeFormatter DATE_TIME_DISPLAY_FORMATTER =
        DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    /** Date or time that this deadline task has to be finished in LocalDate format */
    protected LocalDate byDate;

    /** Date or time that this deadline task has to be finished in LocalDateTime format */
    protected LocalDateTime byDateTime;

    /**
     * Constructor for a Deadline task
     *
     * @param description The description of this Deadline task
     * @param byDate The deadline of this Deadline task in LocalDate format
     */
    public Deadline(String description, LocalDate byDate) {
        this(description, byDate, null);
    }

    /**
     * Constructor for a Deadline task
     *
     * @param description The description of this Deadline task
     * @param byDateTime The deadline of this Deadline task in LocalDateTime format
     */
    public Deadline(String description, LocalDateTime byDateTime) {
        this(description, null, byDateTime);
    }

    /**
     * Master constructor for a Deadline Task
     *
     * @param description The description of this Deadline task
     * @param byDate The deadline of this Deadline task in LocalDate format
     * @param byDateTime The deadline of this Deadline task in LocalDateTime format
     */
    public Deadline(String description, LocalDate byDate, LocalDateTime byDateTime) {
        super(description);
        this.byDate = byDate;
        this.byDateTime = byDateTime;
    }

    /**
     * Returns the string representation of the Deadline task
     *
     * @return A string representing the task
     */
    @Override
    public String toString() {
        String strFormat = "[D]" + super.toString() + " (by: ";

        if (this.byDateTime != null) {
            strFormat += this.byDateTime.format(DATE_TIME_DISPLAY_FORMATTER);
        } else if (this.byDate != null) {
            strFormat += this.byDate.format(DATE_DISPLAY_FORMATTER);
        }

        strFormat += ')';
        return strFormat;
    }
}
