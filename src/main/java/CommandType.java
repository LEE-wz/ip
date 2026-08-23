/**
 * The CommandType enum is a data type to define a fixed set of instructions for the chatbot to follow.
 * Each CommandType will result in different actions performed by the chatbot.
 */
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

    /**
     * Returns the corresponding command type based on the message input by the user
     *
     * @param message Message given by user into chatbot input
     * @return One of the command types if message equals or contains certain keywords
     *         UNKNOWN if message does not contain any of the keywords
     */
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