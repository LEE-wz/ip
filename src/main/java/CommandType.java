public enum CommandType {
    BYE,
    LIST,
    DELETE,
    MARK,
    UNMARK,
    TODO,
    DEADLINE,
    EVENT,
    UNKNOWN;

    public static CommandType fromMessage(String message) {
        if (message == null) {
            return UNKNOWN;
        }

        if (message.equals("bye")) {
            return BYE;
        }
        if (message.equals("list")) {
            return LIST;
        }
        if (message.startsWith("delete")) {
            return DELETE;
        }
        if (message.startsWith("mark")) {
            return MARK;
        }
        if (message.startsWith("unmark")) {
            return UNMARK;
        }
        if (message.startsWith("todo")) {
            return TODO;
        }
        if (message.startsWith("deadline")) {
            return DEADLINE;
        }
        if (message.startsWith("event")) {
            return EVENT;
        }

        return UNKNOWN;
    }
}