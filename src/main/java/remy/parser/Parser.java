package remy.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;

import remy.command.AddCommand;
import remy.command.Command;
import remy.command.DeleteCommand;
import remy.command.ExitCommand;
import remy.command.FindCommand;
import remy.command.ListCommand;
import remy.command.MarkCommand;
import remy.exception.RemyException;
import remy.task.Deadline;
import remy.task.Event;
import remy.task.Task;
import remy.task.Todo;

/**
 * Interprets user commands and converts their arguments into application values.
 *
 * @author LEE-wz
 */
public class Parser {
    /** Command keyword for ending the application. */
    private static final String BYE_COMMAND = "bye";

    /** Command keyword for listing tasks. */
    private static final String LIST_COMMAND = "list";

    /** Command keyword for finding tasks. */
    private static final String FIND_COMMAND = "find";

    /** Command keyword for deleting a task. */
    private static final String DELETE_COMMAND = "delete";

    /** Command keyword for marking a task as completed. */
    private static final String MARK_COMMAND = "mark";

    /** Command keyword for marking a task as incomplete. */
    private static final String UNMARK_COMMAND = "unmark";

    /** Command keyword for creating a to-do task. */
    private static final String TODO_COMMAND = "todo";

    /** Command keyword for creating a deadline task. */
    private static final String DEADLINE_COMMAND = "deadline";

    /** Command keyword for creating an event task. */
    private static final String EVENT_COMMAND = "event";

    /** Delimiter introducing a deadline endpoint. */
    private static final String DEADLINE_DELIMITER = "/by";

    /** Delimiter introducing an event's start endpoint. */
    private static final String EVENT_START_DELIMITER = "/from";

    /** Delimiter introducing an event's end endpoint. */
    private static final String EVENT_END_DELIMITER = "/to";

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
            case FIND -> new FindCommand(parseFindKeyword(message));
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

        if (message.equals(BYE_COMMAND)) {
            return CommandType.BYE;
        }
        if (message.equals(LIST_COMMAND)) {
            return CommandType.LIST;
        }
        if (message.equals(FIND_COMMAND) || (message.startsWith(FIND_COMMAND)
                && Character.isWhitespace(message.charAt(FIND_COMMAND.length())))) {
            return CommandType.FIND;
        }
        if (message.startsWith(DELETE_COMMAND)) {
            return CommandType.DELETE;
        }
        if (message.startsWith(MARK_COMMAND)) {
            return CommandType.MARK;
        }
        if (message.startsWith(UNMARK_COMMAND)) {
            return CommandType.UNMARK;
        }
        if (message.startsWith(TODO_COMMAND)) {
            return CommandType.TODO;
        }
        if (message.startsWith(DEADLINE_COMMAND)) {
            return CommandType.DEADLINE;
        }
        if (message.startsWith(EVENT_COMMAND)) {
            return CommandType.EVENT;
        }

        return CommandType.UNKNOWN;
    }

    /**
     * Parses the keyword supplied to a find command.
     *
     * @param message user message containing the keyword
     * @return the search keyword
     * @throws RemyException if no keyword is supplied
     */
    private String parseFindKeyword(String message) {
        String keyword = message.substring(FIND_COMMAND.length()).strip();
        if (keyword.isEmpty()) {
            throw new RemyException("You need to provide a keyword to find.");
        }
        return keyword;
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
            case MARK -> parseTaskIndex(message, MARK_COMMAND,
                    "you forgot which task to mark as done -_-.", "you have to put an integer :0");
            case UNMARK -> parseTaskIndex(message, UNMARK_COMMAND,
                    "you forgot which task to unmark as undone -_-.", "you have to put an integer :0");
            case DELETE -> parseTaskIndex(message, DELETE_COMMAND,
                    "You forgot which task to delete -_-.", "You have to put an integer :0");
            default -> throw new IllegalArgumentException("Command does not contain a task index: " + commandType);
        };
    }

    /**
     * Parses an indexed command's integer argument.
     *
     * @param message user message containing the index
     * @param commandKeyword command keyword before the index
     * @param missingIndexMessage error message for an omitted index
     * @param invalidIndexMessage error message for a non-integer index
     * @return parsed one-based task index
     */
    private int parseTaskIndex(String message, String commandKeyword, String missingIndexMessage,
            String invalidIndexMessage) {
        if (message.strip().length() == commandKeyword.length()) {
            throw new RemyException(missingIndexMessage);
        }

        try {
            String index = message.substring(commandKeyword.length()).strip();
            return Integer.parseInt(index);
        } catch (NumberFormatException e) {
            throw new RemyException(invalidIndexMessage);
        }
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
     * Parses a todo command.
     *
     * @param message user message containing a todo description
     * @return a todo task
     */
    private Todo parseTodo(String message) {
        if (message.strip().length() == TODO_COMMAND.length()) {
            throw RemyException.createForMissingTodoDescription();
        }

        String description = message.substring(TODO_COMMAND.length()).strip();
        return new Todo(description);
    }

    /**
     * Parses a deadline command.
     *
     * @param message user message containing a deadline description and date
     * @return a deadline task
     */
    private Deadline parseDeadline(String message) {
        if (message.length() == DEADLINE_COMMAND.length()) {
            throw RemyException.createForMissingDeadlineDetails(false, false);
        }

        String[] messageSplit = message.split(DEADLINE_DELIMITER, 0);
        if (messageSplit.length == 1) {
            throw RemyException.createForMissingDeadlineDetails(true, false);
        }

        String description = messageSplit[0].substring(DEADLINE_COMMAND.length()).strip();
        String deadline = messageSplit[1].strip();
        boolean isMissingDescription = description.isEmpty();
        boolean isMissingDeadline = deadline.isEmpty();
        if (isMissingDescription || isMissingDeadline) {
            throw RemyException.createForMissingDeadlineDetails(
                    !isMissingDescription, !isMissingDeadline);
        }

        return createDeadline(description, deadline);
    }

    /**
     * Creates a deadline from validated command details.
     *
     * @param description deadline description
     * @param deadline deadline endpoint to parse
     * @return a deadline containing the parsed endpoint
     * @throws RemyException if the endpoint has an unsupported date format
     */
    private Deadline createDeadline(String description, String deadline) {
        LocalDateTime deadlineDateTime = DateParser.parseDateTime(deadline);
        if (deadlineDateTime != null) {
            return new Deadline(description, deadlineDateTime);
        }

        LocalDate deadlineDate = DateParser.parseDate(deadline);
        if (deadlineDate != null) {
            return new Deadline(description, deadlineDate);
        }

        throw RemyException.createForMissingDeadlineDetails(true, false);
    }

    /**
     * Parses an event command.
     *
     * @param message user message containing an event description and endpoints
     * @return an event task
     */
    private Event parseEvent(String message) {
        if (message.length() == EVENT_COMMAND.length()) {
            throw RemyException.createForMissingEventDetails(false, false, false);
        }

        String eventDelimiterPattern = EVENT_START_DELIMITER + "|" + EVENT_END_DELIMITER;
        String[] messageSplit = message.split(eventDelimiterPattern, 0);
        if (messageSplit.length == 1) {
            throw RemyException.createForMissingEventDetails(true, false, false);
        }

        String description = messageSplit[0].substring(EVENT_COMMAND.length()).strip();
        boolean isMissingDescription = description.isEmpty();
        if (messageSplit.length == 2) {
            boolean hasFrom = message.contains(EVENT_START_DELIMITER);
            boolean hasTo = message.contains(EVENT_END_DELIMITER);
            throw RemyException.createForMissingEventDetails(!isMissingDescription, hasFrom, hasTo);
        }

        String start = messageSplit[1].strip();
        String end = messageSplit[2].strip();
        boolean isMissingStart = start.isEmpty();
        boolean isMissingEnd = end.isEmpty();
        if (isMissingDescription || isMissingStart || isMissingEnd) {
            throw RemyException.createForMissingEventDetails(
                    !isMissingDescription, !isMissingStart, !isMissingEnd);
        }

        return createEvent(description, start, end);
    }

    /**
     * Creates an event from validated command details.
     *
     * @param description event description
     * @param start event start endpoint to parse
     * @param end event end endpoint to parse
     * @return an event containing the parsed endpoints
     * @throws RemyException if the endpoints do not share a supported date format
     */
    private Event createEvent(String description, String start, String end) {
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

        throw RemyException.createForMissingEventDetails(true, false, false);
    }
}
