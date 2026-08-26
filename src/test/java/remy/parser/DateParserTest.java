package remy.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * Tests date and date-time formats accepted from user input and saved task data.
 */
class DateParserTest {

    @Test
    void parseDateTime_supportedNumericAndTextFormats_correctDateTimeReturned() {
        assertEquals(LocalDateTime.of(2026, 8, 23, 14, 30),
                DateParser.parseDateTime("23/8/2026 1430"));
        assertEquals(LocalDateTime.of(2026, 8, 23, 14, 30),
                DateParser.parseDateTime("23 Aug 2026 14:30"));
        assertEquals(LocalDateTime.of(2026, 8, 23, 14, 30),
                DateParser.parseDateTime("2026-08-23T14:30"));
    }

    @Test
    void parseDateTime_invalidOrDateOnlyInput_nullReturned() {
        assertNull(DateParser.parseDateTime("23/8/2026"));
        assertNull(DateParser.parseDateTime("not a date"));
    }

    @Test
    void parseDate_supportedNumericAndTextFormats_correctDateReturned() {
        assertEquals(LocalDate.of(2026, 8, 23), DateParser.parseDate("23/8/2026"));
        assertEquals(LocalDate.of(2026, 8, 23), DateParser.parseDate("23 Aug 2026"));
        assertEquals(LocalDate.of(2026, 8, 23), DateParser.parseDate("2026-08-23"));
    }

    @Test
    void parseDate_invalidOrDateTimeInput_nullReturned() {
        assertNull(DateParser.parseDate("23/8/2026 1430"));
        assertNull(DateParser.parseDate("not a date"));
    }
}
