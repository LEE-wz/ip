package remy.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class encapsulates an Event task
 *
 * @author LEE-wz
 */
public class Event extends Task{

    /** Format used when displaying date-only event endpoints. */
    private static final DateTimeFormatter DATE_DISPLAY_FORMATTER =
        DateTimeFormatter.ofPattern("MMM dd yyyy");

    /** Format used when displaying event endpoints that include a time. */
    private static final DateTimeFormatter DATE_TIME_DISPLAY_FORMATTER =
        DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    /** The starting date/time of this Event task in LocalDate format */
    protected LocalDate startDate;

    /** The starting date/time of this Event task in LocalDateTime format */
    protected LocalDateTime startDateTime;

    /** The ending date/time of this Event task in LocalDate format */
    protected LocalDate endDate;

    /** The ending date/time of this Event task in LocalDateTime format */
    protected LocalDateTime endDateTime;

    /** Creates an event with date-only endpoints. 
     * 
     * @param description The description of this Event task
     * @param startDate The starting date of this Event task in LocalDate format
     * @param endDate The ending date of this Event task in LocalDate format
     */
    public Event(String description, LocalDate startDate, LocalDate endDate) {
        this(description, startDate, endDate, null, null);
    }

    /** 
     * Creates an event with date-time endpoints.
     * 
     * @param description The description of this Event task
     * @param startDateTime The starting date/time of this Event task in LocalDateTime
     * @param endDateTime The ending date/time of this Event task in LocalDateTime
     */
    public Event(String description, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this(description, null, null, startDateTime, endDateTime);
    }

    /**
     * Master Constructor for an Event task
     * @param description The description of this Event task
     * @param startDate The starting date/time of this Event task in LocalDate format
     * @param endDate The ending date/time of this Event task in LocalDate format
     * @param startDateTime The starting date/time of this Event task in LocalDateTime format
     * @param endDateTime The ending date/time of this Event task in LocalDateTime format
     */
    public Event(String description, LocalDate startDate, LocalDate endDate, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        super(description);
        this.startDate = startDate;
        this.endDate = endDate;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * Formats the given date or date-time into a string for display purposes.
     * @param date The date to format
     * @param dateTime The date-time to format
     * @return A string representation of the date or date-time
     */
    private static String formatEndpoint(LocalDate date, LocalDateTime dateTime) {
        if (dateTime != null) {
            return dateTime.format(DATE_TIME_DISPLAY_FORMATTER);
        }
        return date.format(DATE_DISPLAY_FORMATTER);
    }

    /**
     * Returns the string representation of the Event task
     *
     * @return A string representing the task
     */
    @Override
    public String toString() {
        String strFormat = "[E]" + super.toString() + " (from: ";

        strFormat += formatEndpoint(this.startDate, this.startDateTime);
        strFormat += " to: ";
        strFormat += formatEndpoint(this.endDate, this.endDateTime);
        return strFormat + ")";
    }
}
