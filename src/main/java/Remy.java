import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import java.util.stream.Collectors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * The Remy class is a public class that encapsulates a chatbot named after one of the main characters in the movie
 * 'Ratatouille'.
 *
 * @author LEE-wz
 */
public class Remy {
    /** An array list that stores the list of tasks. */
    public static ArrayList<Task> tasks = new ArrayList<>();

    /** Handles messages displayed to the user. */
    private static final Ui UI = new Ui();

    /** The file used to persist tasks between chatbot sessions. */
    private static final Path TASK_FILE = Path.of("./data/remy.txt");

    /**
     * The main logic of the chatbot.
     * The chatbot will perform certain actions based on user's input.
     */
    public static void main(String[] args) {
        loadTasks();
        UI.showGreeting();

        Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();

        boolean canStopLoop = false;

        while (true) {
            try {
                if (message == null) {
                    throw new RemyException("You didn't type anything bruh D:");
                }

                CommandType commandType = CommandType.fromMessage(message);
                switch (commandType) {
                    case BYE -> canStopLoop = true;
                    case LIST -> listTasks();
                    case DELETE -> deleteTask(message);
                    case MARK -> markTaskAsDone(message);
                    case UNMARK -> markTaskAsUndone(message);
                    case TODO -> addTodo(message);
                    case DEADLINE -> addDeadline(message);
                    case EVENT -> addEvent(message);
                    case UNKNOWN -> throw new RemyException();
                }
            } catch (RemyException e) {
                UI.showError(e.getMessage());
            }

            if (canStopLoop) {
                break;
            }

            message = scanner.nextLine();
        }

        scanner.close();
        UI.showFarewell();
    }

    /**
     * Marks the task specified by the user as 'done'.
     *
     * @param message Message given by user into chatbot input
     * @throws RemyException If task index is omitted or invalid (out of range)
     * @throws NumberFormatException If task index given is not an integer
     */
    public static void markTaskAsDone(String message) {

        if (message.strip().length() == 4) {
            throw new RemyException("you forgot which task to mark as done -_-.");
        }

        int formattedIndex;
        try {
            String idx = message.substring(4).strip();
            formattedIndex = Integer.parseInt(idx);

        } catch (NumberFormatException e) {
            throw new RemyException("you have to put an integer :0");
        }

        if (formattedIndex > tasks.size() || formattedIndex < 1) {
            throw new RemyException("your index is out of range :/");
        }

        tasks.get(formattedIndex - 1).markAsDone();
        saveTasks();
    }

    /**
     * Marks the task specified by the user as 'undone'.
     *
     * @param message Message given by user into chatbot input
     * @throws RemyException If task index is omitted or invalid (out of range)
     * @throws NumberFormatException If task index given is not an integer
     */
    public static void markTaskAsUndone(String message) {
        if (message.strip().length() == 6) {
            throw new RemyException("you forgot which task to unmark as undone -_-.");
        }

        int formattedIndex;
        try {
            String idx = message.substring(6).strip();
            formattedIndex = Integer.parseInt(idx);

        } catch (NumberFormatException e) {
            throw new RemyException("you have to put an integer :0");
        }

        if (formattedIndex > tasks.size() || formattedIndex < 1) {
            throw new RemyException("your index is out of range :/");
        }

        tasks.get(formattedIndex - 1).markAsUndone();
        saveTasks();
    }

    /**
     * Adds a task into list of tasks, and prints the current number of tasks.
     *
     * @param task Task specified by user, which can be either an Event, Deadline, or To\do
     */
    public static void addTask(Task task) {
        tasks.add(task);
        saveTasks();
        UI.showTaskAdded(task, tasks.size());
    }

    /**
     * Creates a new To\do class with the description specified by user in the message text,
     * and adds it into list of tasks.
     *
     * @param message Message given by user into chatbot input
     * @throws RemyException If description is omitted
     */
    public static void addTodo(String message) {
        if (message.strip().length() == 4) {
            throw new RemyException(false);
        }

        String description = message.substring(4).strip();
        Todo newTodo = new Todo(description);
        addTask(newTodo);
    }

    /**
     * Creates a new Deadline class with the description and deadline specified by user in the message text,
     * and adds it into list of tasks.
     *
     * @param message Message given by user into chatbot input
     * @throws RemyException If description or deadline is omitted
     */
    public static void addDeadline(String message) {
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

        LocalDateTime deadlineDateTime = parseDateTime(deadline);
        Deadline newDeadline = deadlineDateTime == null
                ? null
                : new Deadline(description, deadlineDateTime);

        if (newDeadline == null) {
            LocalDate deadlineDate = parseDate(deadline);
            if (deadlineDate != null) {
                newDeadline = new Deadline(description, deadlineDate);
            }
        }

        if (newDeadline == null) {
            throw new RemyException(true, false);
        }

        addTask(newDeadline);
    }

    /**
     * Creates a new Event class with the description, start and end specified by user in the message text,
     * and adds it into list of tasks.
     *
     * @param message Message given by user into chatbot input
     * @throws RemyException If description, or start, or end is omitted
     */
    public static void addEvent(String message) {
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

        LocalDateTime startDateTime = parseDateTime(start);
        LocalDateTime endDateTime = parseDateTime(end);
        LocalDate startDate = parseDate(start);
        LocalDate endDate = parseDate(end);

        Event newEvent;
        if (startDateTime != null && endDateTime != null) {
            newEvent = new Event(description, startDateTime, endDateTime);
        } else if (startDate != null && endDate != null) {
            newEvent = new Event(description, startDate, endDate);
        } else {
            throw new RemyException(true, false, false);
        }
        addTask(newEvent);
    }

    /**
     * Prints out the list of tasks for the user.
     * Each task shows its type of task, description, and whether they are done or not.
     */
    public static void listTasks() {
        UI.showTaskList(tasks);
    }

    /**
     * Deletes a task specified by the user from the list of tasks
     *
     * @param message Message given by user into chatbot input
     * @throws RemyException If there are zero tasks, task index is omitted or invalid (out of range)
     */
    public static void deleteTask(String message) {
        if (tasks.isEmpty()) {
            throw new RemyException("There is no task for you to delete LOL.");
        }

        if (message.strip().length() == 6) {
            throw new RemyException("You forgot which task to delete -_-.");
        }

        int formattedIndex;
        try {
            String idx = message.substring(6).strip();
            formattedIndex = Integer.parseInt(idx);
        } catch (NumberFormatException e) {
            throw new RemyException("You have to put an integer :0");
        }

        if (formattedIndex > tasks.size() || formattedIndex < 1) {
            throw new RemyException("Your index is out of range :/");
        }

        Task taskToDelete = tasks.get(formattedIndex - 1);
        tasks.remove(taskToDelete);
        saveTasks();
        UI.showTaskDeleted(taskToDelete, tasks.size());
    }

    /** Saves the current task list to the hard disk. */
    public static void saveTasks() {
        Path temporaryFile = null;
        try {
            Path parent = TASK_FILE.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            String taskData = tasks.stream()
                    .map(Task::toString)
                    .collect(Collectors.joining(System.lineSeparator()));

            temporaryFile = Files.createTempFile(parent, "remy-", ".tmp");
            Files.writeString(temporaryFile, taskData);

            try {
                Files.move(temporaryFile, TASK_FILE, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                Files.move(temporaryFile, TASK_FILE, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException | SecurityException e) {
            System.out.println("Unable to save tasks to " + TASK_FILE);
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException | SecurityException ignored) {
                    // The original save error is more useful to the user.
                }
            }
        }
    }

    /** Loads saved tasks from the hard disk when the chatbot starts. */
    public static void loadTasks() {
        try {
            if (!Files.exists(TASK_FILE)) {
                return;
            }

            ArrayList<Task> loadedTasks = new ArrayList<>();
            for (String line : Files.readAllLines(TASK_FILE)) {
                Task task = parseTask(line);
                if (task != null) {
                    loadedTasks.add(task);
                }
            }
            tasks.clear();
            tasks.addAll(loadedTasks);
        } catch (IOException | SecurityException e) {
            System.out.println("Unable to load saved tasks from " + TASK_FILE);
        }
    }

    /**
     * Parses a line from the saved task file and creates a corresponding Task object.
     *
     * @param line A line from the saved task file representing a task
     * @return A Task object corresponding to the line, or null if the line is invalid
     */
    private static Task parseTask(String line) {
        // Validate the line format before parsing
        if (line == null || line.length() < 8 || line.charAt(0) != '[' || line.charAt(2) != ']'
                || line.charAt(3) != '[' || (line.charAt(4) != ' ' && line.charAt(4) != 'X')
                || line.charAt(5) != ']' || line.charAt(6) != ' ') {
            return null;
        }

        boolean isDone = line.charAt(4) == 'X';
        String taskDetails = line.substring(7);
        if (taskDetails.strip().isEmpty()) {
            return null;
        }
        Task task;

        switch (line.charAt(1)) {
            case 'T' -> task = new Todo(taskDetails);
            case 'D' -> {
                String marker = " (by: ";
                if (!taskDetails.endsWith(")") || !taskDetails.contains(marker)) {
                    return null;
                }

                int markerIndex = taskDetails.lastIndexOf(marker);
                if (markerIndex == 0 || markerIndex + marker.length() == taskDetails.length() - 1) {
                    return null;
                }

                String description = taskDetails.substring(0, markerIndex);
                String deadline = taskDetails.substring(markerIndex + marker.length(), taskDetails.length() - 1);
                task = parseSavedDeadline(description, deadline);
                if (task == null) {
                    return null;
                }
            }
            case 'E' -> {
                String startMarker = " (from: ";
                String endMarker = " to: ";
                if (!taskDetails.endsWith(")") || !taskDetails.contains(startMarker)
                        || !taskDetails.contains(endMarker)) {
                    return null;
                }

                int startIndex = taskDetails.lastIndexOf(startMarker);
                int endIndex = taskDetails.lastIndexOf(endMarker);
                if (endIndex < startIndex) {
                    return null;
                }
                if (startIndex == 0 || startIndex + startMarker.length() == endIndex
                        || endIndex + endMarker.length() == taskDetails.length() - 1) {
                    return null;
                }

                String description = taskDetails.substring(0, startIndex);
                String start = taskDetails.substring(startIndex + startMarker.length(), endIndex);
                String end = taskDetails.substring(endIndex + endMarker.length(), taskDetails.length() - 1);
                task = parseSavedEvent(description, start, end);
                if (task == null) {
                    return null;
                }
            }
            default -> {
                return null;
            }
        }

        task.isDone = isDone;
        return task;
    }

    /** Parses a deadline from its displayed or legacy persisted representation. 
     * 
     * @param description The description of the deadline
     * @param deadline The deadline of the task in string format
     * @return A Deadline object if parsing is successful, or null if parsing fails
     */
    private static Deadline parseSavedDeadline(String description, String deadline) {
        LocalDateTime deadlineDateTime = parseDateTime(deadline);
        if (deadlineDateTime != null) {
            return new Deadline(description, deadlineDateTime);
        }

        LocalDate deadlineDate = parseDate(deadline);
        if (deadlineDate != null) {
            return new Deadline(description, deadlineDate);
        }

        return null;
    }

    /** Parses event endpoints from their displayed or legacy persisted representations. 
     * 
     * @param description The description of the event
     * @param start The starting date/time of the event in string format
     * @param end The ending date/time of the event in string format
     * @return An Event object if parsing is successful, or null if parsing fails
     */
    private static Event parseSavedEvent(String description, String start, String end) {
        LocalDateTime startDateTime = parseDateTime(start);
        LocalDateTime endDateTime = parseDateTime(end);
        if (startDateTime != null && endDateTime != null) {
            return new Event(description, startDateTime, endDateTime);
        }

        LocalDate startDate = parseDate(start);
        LocalDate endDate = parseDate(end);
        if (startDate != null && endDate != null) {
            return new Event(description, startDate, endDate);
        }
        return null;
    }

    /**
     * Parses a date-time string into a LocalDateTime object using multiple supported formats.
     * If the string does not match any supported format, returns null.
     * 
     * @param value The date-time string to parse
     * @return A LocalDateTime object if parsing is successful, or null if parsing fails
     */
    private static LocalDateTime parseDateTime(String value) {
        List<DateTimeFormatter> formatters = List.of(
                DateTimeFormatter.ofPattern("d/M/yyyy HHmm"),
                DateTimeFormatter.ofPattern("d/M/yyyy HH:mm"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"),
                DateTimeFormatter.ofPattern("d-M-yyyy HHmm"),
                DateTimeFormatter.ofPattern("d-M-yyyy HH:mm"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy HHmm"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                DateTimeFormatter.ofPattern("yyyy-M-d HHmm"),
                DateTimeFormatter.ofPattern("yyyy-M-d HH:mm"),
                DateTimeFormatter.ofPattern("MMM dd yyyy HHmm"),
                DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm"),
                DateTimeFormatter.ofPattern("dd MMM yyyy HHmm"),
                DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDateTime.parse(value, formatter);
            } catch (DateTimeParseException ignored) {
                // Try the next supported date-time format.
            }
        }
        return null;
    }

    /**
     * Parses a date string into a LocalDate object using multiple supported formats.
     * If the string does not match any supported format, returns null.
     * 
     * @param value The date string to parse
     * @return A LocalDate object if parsing is successful, or null if parsing fails
     */
    private static LocalDate parseDate(String value) {
        List<DateTimeFormatter> formatters = List.of(
                DateTimeFormatter.ofPattern("d/M/yyyy"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("d-M-yyyy"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy-M-d"),
                DateTimeFormatter.ofPattern("MMM dd yyyy"),
                DateTimeFormatter.ofPattern("dd MMM yyyy"),
                DateTimeFormatter.ISO_LOCAL_DATE);
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(value, formatter);
            } catch (DateTimeParseException ignored) {
                // Try the next supported date format.
            }
        }
        return null;
    }
}
