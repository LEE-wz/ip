import java.util.Scanner;

import java.io.IOException;

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

    /** Interprets user commands and their arguments. */
    private static final Parser PARSER = new Parser();

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

                CommandType commandType = PARSER.parseCommandType(message);
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
     * @throws RemyException If task index is omitted, invalid, or out of range
     */
    public static void markTaskAsDone(String message) {

        int formattedIndex = PARSER.parseTaskIndex(message, CommandType.MARK);

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
     * @throws RemyException If task index is omitted, invalid, or out of range
     */
    public static void markTaskAsUndone(String message) {
        int formattedIndex = PARSER.parseTaskIndex(message, CommandType.UNMARK);

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
        addTask(PARSER.parseTask(message, CommandType.TODO));
    }

    /**
     * Creates a new Deadline class with the description and deadline specified by user in the message text,
     * and adds it into list of tasks.
     *
     * @param message Message given by user into chatbot input
     * @throws RemyException If description or deadline is omitted
     */
    public static void addDeadline(String message) {
        addTask(PARSER.parseTask(message, CommandType.DEADLINE));
    }

    /**
     * Creates a new Event class with the description, start and end specified by user in the message text,
     * and adds it into list of tasks.
     *
     * @param message Message given by user into chatbot input
     * @throws RemyException If description, or start, or end is omitted
     */
    public static void addEvent(String message) {
        addTask(PARSER.parseTask(message, CommandType.EVENT));
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
        int formattedIndex = PARSER.parseTaskIndex(message, CommandType.DELETE);

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
