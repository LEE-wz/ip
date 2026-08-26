import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Interprets user commands and converts their arguments into application values.
 */
public class Parser {

    /**
     * Parses a user message into an executable command.
     *
     * @param message user message to inspect
     * @return command represented by the user message
     * @throws RemyException if the message is unknown or its arguments are invalid
     */
    public Command parse(String message) {
        CommandType commandType = parseCommandType(message);
        return switch (commandType) {
            case BYE -> new ExitCommand();
            case LIST -> new ListCommand();
            case DELETE -> new DeleteCommand(parseTaskIndex(message, commandType));
            case MARK -> new MarkCommand(parseTaskIndex(message, commandType), true);
            case UNMARK -> new MarkCommand(parseTaskIndex(message, commandType), false);
            case TODO, DEADLINE, EVENT -> new AddCommand(parseTask(message, commandType));
            case UNKNOWN -> throw new RemyException();
        };
    }

    /**
     * Identifies the command represented by a user message.
     *
     * @param message user message to inspect
     * @return the command type represented by the message, or UNKNOWN when it is not recognised
     */
    private CommandType parseCommandType(String message) {
        if (message == null) {
            return CommandType.UNKNOWN;
        }

        if (message.equals("bye")) {
            return CommandType.BYE;
        }
        if (message.equals("list")) {
            return CommandType.LIST;
        }
        if (message.startsWith("delete")) {
            return CommandType.DELETE;
        }
        if (message.startsWith("mark")) {
            return CommandType.MARK;
        }
        if (message.startsWith("unmark")) {
            return CommandType.UNMARK;
        }
        if (message.startsWith("todo")) {
            return CommandType.TODO;
        }
        if (message.startsWith("deadline")) {
            return CommandType.DEADLINE;
        }
        if (message.startsWith("event")) {
            return CommandType.EVENT;
        }

        return CommandType.UNKNOWN;
    }

    /**
     * Parses a task index for a mark, unmark, or delete command.
     *
     * @param message user message containing the task index
     * @param commandType type of indexed command
     * @return the one-based task index supplied by the user
     * @throws RemyException if the index is missing or is not an integer
     */
    private int parseTaskIndex(String message, CommandType commandType) {
        return switch (commandType) {
            case MARK -> parseTaskIndex(message, 4,
                    "you forgot which task to mark as done -_-.", "you have to put an integer :0");
            case UNMARK -> parseTaskIndex(message, 6,
                    "you forgot which task to unmark as undone -_-.", "you have to put an integer :0");
            case DELETE -> parseTaskIndex(message, 6,
                    "You forgot which task to delete -_-.", "You have to put an integer :0");
            default -> throw new IllegalArgumentException("Command does not contain a task index: " + commandType);
        };
    }

    /**
     * Parses a task-creation command and creates the requested task.
     *
     * @param message user message containing a task description and details
     * @param commandType type of task to create
     * @return newly created task
     * @throws RemyException if required task details are invalid or missing
     */
    private Task parseTask(String message, CommandType commandType) {
        return switch (commandType) {
            case TODO -> parseTodo(message);
            case DEADLINE -> parseDeadline(message);
            case EVENT -> parseEvent(message);
            default -> throw new IllegalArgumentException("Command does not create a task: " + commandType);
        };
    }

    /**
     * Parses an indexed command's integer argument.
     *
     * @param message user message containing the index
     * @param commandLength length of the command keyword
     * @param missingIndexMessage error message for an omitted index
     * @param invalidIndexMessage error message for a non-integer index
     * @return parsed one-based task index
     */
    private int parseTaskIndex(String message, int commandLength, String missingIndexMessage,
            String invalidIndexMessage) {
        if (message.strip().length() == commandLength) {
            throw new RemyException(missingIndexMessage);
        }

        try {
            String index = message.substring(commandLength).strip();
            return Integer.parseInt(index);
        } catch (NumberFormatException e) {
            throw new RemyException(invalidIndexMessage);
        }
    }

    /**
     * Parses a todo command.
     *
     * @param message user message containing a todo description
     * @return a todo task
     */
    private Todo parseTodo(String message) {
        if (message.strip().length() == 4) {
            throw new RemyException(false);
        }

        String description = message.substring(4).strip();
        return new Todo(description);
    }

    /**
     * Parses a deadline command.
     *
     * @param message user message containing a deadline description and date
     * @return a deadline task
     */
    private Deadline parseDeadline(String message) {
        if (message.length() == 8) {
            throw new RemyException(false, false);
        }

        String[] messageSplit = message.split("/by", 0);
        if (messageSplit.length == 1) {
            throw new RemyException(true, false);
        }

        String description = messageSplit[0].substring(8).strip();
        String deadline = messageSplit[1].strip();
        boolean isMissingDescription = description.isEmpty();
        boolean isMissingDeadline = deadline.isEmpty();
        if (isMissingDescription || isMissingDeadline) {
            throw new RemyException(!isMissingDescription, !isMissingDeadline);
        }

        LocalDateTime deadlineDateTime = DateParser.parseDateTime(deadline);
        if (deadlineDateTime != null) {
            return new Deadline(description, deadlineDateTime);
        }

        LocalDate deadlineDate = DateParser.parseDate(deadline);
        if (deadlineDate != null) {
            return new Deadline(description, deadlineDate);
        }

        throw new RemyException(true, false);
    }

    /**
     * Parses an event command.
     *
     * @param message user message containing an event description and endpoints
     * @return an event task
     */
    private Event parseEvent(String message) {
        if (message.length() == 5) {
            throw new RemyException(false, false, false);
        }

        String[] messageSplit = message.split("/from|/to", 0);
        if (messageSplit.length == 1) {
            throw new RemyException(true, false, false);
        }

        String description = messageSplit[0].substring(5).strip();
        boolean isMissingDescription = description.isEmpty();
        if (messageSplit.length == 2) {
            boolean hasFrom = message.contains("/from");
            boolean hasTo = message.contains("/to");
            throw new RemyException(!isMissingDescription, hasFrom, hasTo);
        }

        String start = messageSplit[1].strip();
        String end = messageSplit[2].strip();
        boolean isMissingStart = start.isEmpty();
        boolean isMissingEnd = end.isEmpty();
        if (isMissingDescription || isMissingStart || isMissingEnd) {
            throw new RemyException(!isMissingDescription, !isMissingStart, !isMissingEnd);
        }

        LocalDateTime startDateTime = DateParser.parseDateTime(start);
        LocalDateTime endDateTime = DateParser.parseDateTime(end);
        LocalDate startDate = DateParser.parseDate(start);
        LocalDate endDate = DateParser.parseDate(end);
        if (startDateTime != null && endDateTime != null) {
            return new Event(description, startDateTime, endDateTime);
        }
        if (startDate != null && endDate != null) {
            return new Event(description, startDate, endDate);
        }

        throw new RemyException(true, false, false);
    }
}
