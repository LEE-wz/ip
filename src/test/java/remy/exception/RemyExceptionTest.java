package remy.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RemyExceptionTest {
    private static final String MESSAGE_DIVIDER = "____________________________________________________________\n";

    @Test
    void constructor_customMessage_framedMessageReturned() {
        RemyException exception = new RemyException("Custom error");

        assertEquals(MESSAGE_DIVIDER + "Custom error\n" + MESSAGE_DIVIDER, exception.getMessage());
    }

    @Test
    void constructor_missingDeadlineAndDescription_requestedDetailsListed() {
        RemyException exception = new RemyException(false, false);

        assertEquals(MESSAGE_DIVIDER
                + "Bruh :/, your deadline task is missing the following: \n"
                + "- description. \n"
                + "- DEADLINE of your DEADLINE task -_-. \n"
                + "\nYour deadline task should look something like this, please don't mess it up again -_-: \n"
                + "deadline return book /by Sunday\n"
                + MESSAGE_DIVIDER, exception.getMessage());
    }
}
