public class RemyException extends RuntimeException{

    public RemyException() {
        super("Wha-? My bad bruh, I have no clue what you're talking about :/\n");
    }

    public RemyException(boolean hasDescription) {
        String message = "Bruh :/, you need to fill in the description of your todo task, unless you're doing nothing :/.\n";

        message += "\nYour todo task should look something like this, please don't mess it up again -_-: \n";

        message += "todo borrow book";

        super(message);
    }

    public RemyException(boolean hasDescription, boolean hasDeadline) {
        String message = "Bruh :/, your deadline task is missing the following: \n";

        if (!hasDescription) {
            message += "- description. \n";
        }

        if (!hasDeadline) {
            message += "- DEADLINE of your DEADLINE task -_-. \n";
        }

        message += "\nYour deadline task should look something like this, please don't mess it up again -_-: \n";

        message += "deadline return book /by Sunday";

        super(message);
    }

    public RemyException(boolean hasDescription, boolean hasStart, boolean hasEnd) {
        String message = "Bruh :/, your event task is missing the following: \n";

        if (!hasDescription) {
            message += "- description. \n";
        }

        if (!hasStart) {
            message += "- when your event task starts. \n";
        }

        if (!hasEnd) {
            message += "- when your event task ends. \n";
        }

        message += "\nYour event task should look something like this, please don't mess it up again -_-: \n";

        message += "event project meeting /from Mon 2pm /to 4pm";

        super(message);
    }
}
