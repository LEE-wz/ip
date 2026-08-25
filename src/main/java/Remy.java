import java.util.Scanner;

import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The Remy class is a public class that encapsulates a chatbot named after one of the main characters in the movie
 * 'Ratatouille'.
 *
 * @author LEE-wz
 */
public class Remy {
    /** Stores the tasks managed during this chat session. */
    private static TaskList tasks = new TaskList();

    /** Handles messages displayed to the user. */
    private static final Ui UI = new Ui();

    /** Loads and saves tasks between chat sessions. */
    private static final Storage STORAGE = new Storage("./data/remy.txt");

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

        LocalDateTime deadlineDateTime = DateParser.parseDateTime(deadline);
        Deadline newDeadline = deadlineDateTime == null
                ? null
                : new Deadline(description, deadlineDateTime);

        if (newDeadline == null) {
            LocalDate deadlineDate = DateParser.parseDate(deadline);
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

        LocalDateTime startDateTime = DateParser.parseDateTime(start);
        LocalDateTime endDateTime = DateParser.parseDateTime(end);
        LocalDate startDate = DateParser.parseDate(start);
        LocalDate endDate = DateParser.parseDate(end);

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
        UI.showTaskList(tasks.getTasks());
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

        Task taskToDelete = tasks.remove(formattedIndex - 1);
        saveTasks();
        UI.showTaskDeleted(taskToDelete, tasks.size());
    }

    /** Saves the current task list to the hard disk. */
    public static void saveTasks() {
        try {
            STORAGE.save(tasks);
        } catch (IOException | SecurityException e) {
            System.out.println("Unable to save tasks to data/remy.txt");
        }
    }

    /** Loads saved tasks from the hard disk when the chatbot starts. */
    public static void loadTasks() {
        try {
            tasks = STORAGE.load();
        } catch (IOException | SecurityException e) {
            System.out.println("Unable to load saved tasks from data/remy.txt");
        }
    }
}
