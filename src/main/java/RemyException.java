public class RemyException extends RuntimeException{

    // Invalid keyword
    public RemyException() {
        String message = "____________________________________________________________\n";
        message += "Wha-? My bad bruh, I have no clue what you're talking about :/\n";
        message += "____________________________________________________________\n";
        super(message);
    }

    public RemyException(String msg) {
        String message  = "____________________________________________________________\n";
        message += msg + "\n";
        message += "____________________________________________________________\n";
        super(message);
    }

    // Invalid todo task
    public RemyException(boolean hasDescription) {
        String message = "____________________________________________________________\n";
        message += "Bruh :/, you need to fill in the description of your todo task, unless you're doing nothing :/.\n";
        message += "\nYour todo task should look something like this, please don't mess it up again -_-: \n";
        message += "todo borrow book\n";
        message += "____________________________________________________________\n";
        super(message);
    }

    // Invalid deadline task
    public RemyException(boolean hasDescription, boolean hasDeadline) {
        String message = "____________________________________________________________\n";
        message += "Bruh :/, your deadline task is missing the following: \n";

        if (!hasDescription) {
            message += "- description. \n";
        }

        if (!hasDeadline) {
            message += "- DEADLINE of your DEADLINE task -_-. \n";
        }

        message += "\nYour deadline task should look something like this, please don't mess it up again -_-: \n";
        message += "deadline return book /by Sunday\n";
        message += "____________________________________________________________\n";
        super(message);
    }

    // Invalid event task
    public RemyException(boolean hasDescription, boolean hasStart, boolean hasEnd) {
        String message = "____________________________________________________________\n";
        message += "Bruh :/, your event task is missing the following: \n";

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
        message += "event project meeting /from Mon 2pm /to 4pm\n";
        message += "____________________________________________________________\n";
        super(message);
    }
}
