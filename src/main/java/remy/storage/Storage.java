package remy.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

import remy.parser.DateParser;
import remy.task.Deadline;
import remy.task.Event;
import remy.task.Task;
import remy.task.TaskList;
import remy.task.Todo;

/**
 * Loads tasks from and saves tasks to Remy's task file.
 *
 * @author LEE-wz
 */
public class Storage {
    /** Marker identifying a persisted to-do task. */
    private static final String TODO_TASK_MARKER = "[T]";

    /** Marker identifying a persisted deadline task. */
    private static final String DEADLINE_TASK_MARKER = "[D]";

    /** Marker identifying a persisted event task. */
    private static final String EVENT_TASK_MARKER = "[E]";

    /** Marker identifying an incomplete persisted task. */
    private static final String INCOMPLETE_STATUS_MARKER = "[ ] ";

    /** Marker identifying a completed persisted task. */
    private static final String COMPLETED_STATUS_MARKER = "[X] ";

    /** Index immediately after a persisted task-type marker. */
    private static final int TASK_TYPE_MARKER_END_INDEX = 3;

    /** Index at which persisted task details begin. */
    private static final int TASK_DETAILS_START_INDEX = 7;

    /** File used to persist tasks between chat sessions. */
    private final Path taskFile;

    /**
     * Creates storage that persists tasks at the given file path.
     *
     * @param filePath file path used to persist tasks
     */
    public Storage(String filePath) {
        this.taskFile = Path.of(filePath);
    }

    /**
     * Saves tasks to the task file using an atomic replacement when supported.
     *
     * @param tasks tasks to save
     * @throws IOException if the task file cannot be written
     */
    public void save(TaskList tasks) throws IOException {
        Path temporaryFile = null;
        try {
            Path parent = taskFile.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            String taskData = tasks.getTasks().stream()
                    .map(Task::toString)
                    .collect(Collectors.joining(System.lineSeparator()));

            temporaryFile = Files.createTempFile(parent, "remy-", ".tmp");
            Files.writeString(temporaryFile, taskData);

            try {
                Files.move(temporaryFile, taskFile, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                Files.move(temporaryFile, taskFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException | SecurityException e) {
            deleteTemporaryFile(temporaryFile);
            throw e;
        }
    }

    /**
     * Loads valid tasks from the task file.
     *
     * @return the tasks loaded from the file, or an empty task list when the file does not exist
     * @throws IOException if the task file cannot be read
     */
    public TaskList load() throws IOException {
        if (!Files.exists(taskFile)) {
            return new TaskList();
        }

        ArrayList<Task> loadedTasks = new ArrayList<>();
        for (String line : Files.readAllLines(taskFile)) {
            Task task = parseTask(line);
            if (task != null) {
                loadedTasks.add(task);
            }
        }
        return new TaskList(loadedTasks);
    }

    /**
     * Deletes a temporary save file, preserving the original save failure if cleanup fails.
     *
     * @param temporaryFile temporary file to delete
     */
    private void deleteTemporaryFile(Path temporaryFile) {
        if (temporaryFile == null) {
            return;
        }

        try {
            Files.deleteIfExists(temporaryFile);
        } catch (IOException | SecurityException ignored) {
            // The original save error is more useful to the user.
        }
    }

    /**
     * Parses a line from the saved task file and creates its corresponding task.
     *
     * @param line line from the saved task file
     * @return the parsed task, or null when the line is invalid
     */
    private Task parseTask(String line) {
        if (!hasValidTaskStructure(line)) {
            return null;
        }

        String taskTypeMarker = line.substring(0, TASK_TYPE_MARKER_END_INDEX);
        String taskStatusMarker = line.substring(TASK_TYPE_MARKER_END_INDEX, TASK_DETAILS_START_INDEX);
        String taskDetails = line.substring(TASK_DETAILS_START_INDEX);
        if (taskDetails.strip().isEmpty()) {
            return null;
        }

        Task task;
        switch (taskTypeMarker) {
            case TODO_TASK_MARKER -> task = new Todo(taskDetails);
            case DEADLINE_TASK_MARKER -> task = parseDeadlineTask(taskDetails);
            case EVENT_TASK_MARKER -> task = parseEventTask(taskDetails);
            default -> {
                return null;
            }
        }

        if (task == null) {
            return null;
        }

        if (taskStatusMarker.equals(COMPLETED_STATUS_MARKER)) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Returns whether a saved line has enough content and a recognized completion marker.
     *
     * @param line saved task line to inspect
     * @return true when the line can be split safely into its persisted components
     */
    private boolean hasValidTaskStructure(String line) {
        if (line == null || line.length() <= TASK_DETAILS_START_INDEX) {
            return false;
        }

        String taskStatusMarker = line.substring(TASK_TYPE_MARKER_END_INDEX, TASK_DETAILS_START_INDEX);
        return taskStatusMarker.equals(INCOMPLETE_STATUS_MARKER)
                || taskStatusMarker.equals(COMPLETED_STATUS_MARKER);
    }

    /**
     * Parses a saved deadline task.
     *
     * @param taskDetails description and deadline from the saved task
     * @return the parsed deadline task, or null when its format is invalid
     */
    private Deadline parseDeadlineTask(String taskDetails) {
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
        return parseSavedDeadline(description, deadline);
    }

    /**
     * Parses a saved event task.
     *
     * @param taskDetails description and endpoints from the saved task
     * @return the parsed event task, or null when its format is invalid
     */
    private Event parseEventTask(String taskDetails) {
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
        return parseSavedEvent(description, start, end);
    }

    /**
     * Parses a deadline from its displayed or legacy persisted representation.
     *
     * @param description description of the deadline
     * @param deadline deadline string from the task file
     * @return the parsed deadline, or null when the date is invalid
     */
    private Deadline parseSavedDeadline(String description, String deadline) {
        LocalDateTime deadlineDateTime = DateParser.parseDateTime(deadline);
        if (deadlineDateTime != null) {
            return new Deadline(description, deadlineDateTime);
        }

        LocalDate deadlineDate = DateParser.parseDate(deadline);
        if (deadlineDate != null) {
            return new Deadline(description, deadlineDate);
        }

        return null;
    }

    /**
     * Parses event endpoints from their displayed or legacy persisted representation.
     *
     * @param description description of the event
     * @param start starting endpoint string from the task file
     * @param end ending endpoint string from the task file
     * @return the parsed event, or null when the endpoints are invalid
     */
    private Event parseSavedEvent(String description, String start, String end) {
        LocalDateTime startDateTime = DateParser.parseDateTime(start);
        LocalDateTime endDateTime = DateParser.parseDateTime(end);
        if (startDateTime != null && endDateTime != null) {
            return new Event(description, startDateTime, endDateTime);
        }

        LocalDate startDate = DateParser.parseDate(start);
        LocalDate endDate = DateParser.parseDate(end);
        if (startDate != null && endDate != null) {
            return new Event(description, startDate, endDate);
        }
        return null;
    }
}
