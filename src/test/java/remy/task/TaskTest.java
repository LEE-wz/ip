package remy.task;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * Tests invariants shared by tasks and their endpoint-based subtypes.
 */
class TaskTest {

    @Test
    void constructor_nullDescription_assertionErrorThrown() {
        assertThrows(AssertionError.class, () -> new Todo(null));
    }

    @Test
    void constructor_blankDescription_assertionErrorThrown() {
        assertThrows(AssertionError.class, () -> new Todo("   "));
    }

    @Test
    void deadlineConstructor_noEndpoint_assertionErrorThrown() {
        assertThrows(AssertionError.class, () -> new Deadline("Submit report", null, null));
    }

    @Test
    void deadlineConstructor_twoEndpointRepresentations_assertionErrorThrown() {
        LocalDate date = LocalDate.of(2026, 9, 8);
        LocalDateTime dateTime = LocalDateTime.of(2026, 9, 8, 18, 0);

        assertThrows(AssertionError.class, () -> new Deadline("Submit report", date, dateTime));
    }

    @Test
    void eventConstructor_mixedEndpointRepresentations_assertionErrorThrown() {
        LocalDate startDate = LocalDate.of(2026, 9, 8);
        LocalDateTime endDateTime = LocalDateTime.of(2026, 9, 8, 18, 0);

        assertThrows(AssertionError.class, () ->
                new Event("Project meeting", startDate, null, null, endDateTime));
    }
}
