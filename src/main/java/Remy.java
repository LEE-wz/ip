import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

/**
 * The Remy class is a public class that encapsulates a chatbot named after one of the main characters in the movie
 * 'Ratatouille'.
 *
 * @author LEE-wz
 */
public class Remy {
    /** An array list that stores the list of tasks. */
    public static ArrayList<Task> tasks = new ArrayList<>();

    /** The file used to persist tasks between chatbot sessions. */
    private static final Path TASK_FILE = Path.of("./data/remy.txt");

    /**
     * The main logic of the chatbot.
     * The chatbot will perform certain actions based on user's input.
     */
    public static void main(String[] args) {
        displayGreetMessage();

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
                System.out.println(e.getMessage());
            }

            if (canStopLoop) {
                break;
            }

            message = scanner.nextLine();
        }

        scanner.close();
        displayExitMessage();
    }

    /** Prints greeting message when user starts the chatbot. */
    public static void displayGreetMessage() {
        System.out.println(
                """
                        ____________________________________________________________
                         _____                     \s
                        |  __ \\                    \s
                        | |__) | ___ _ __ ___  _   _
                        |  _  / / _ \\ '_ ` _ \\| | | |
                        | | \\ \\|  __/ | | | | | |_| |
                        |_|  \\_\\\\___|_| |_| |_|\\__, |
                                                __/ |
                                               |___/\s
                        Yo! I am Remy the rat from Ratatouille.
                        How can I serve you today? :D
                        
                        ____________________________________________________________
                        """);
    }

    /** Prints exit message when user ends the chatbot. */
    public static void displayExitMessage() {
        System.out.println(
                """
                        ____________________________________________________________
                        Cya. Call me again when you need me!
                        ____________________________________________________________
                        """);
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
        String result =
                "____________________________________________________________\n"
                        + "Okay, I have helped you create a task:\n"
                        + task + "\n"
                        + "Now you have " + (tasks.size()) + " task(s) in the list. Better hurry before it piles up!\n"
                        + "____________________________________________________________\n";
        System.out.println(result);
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

        String description = message.substring(4);
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

        Deadline newDeadline = new Deadline(description, deadline);
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

        Event newEvent = new Event(description, start, end);
        addTask(newEvent);
    }

    /**
     * Prints out the list of tasks for the user.
     * Each task shows its type of task, description, and whether they are done or not.
     */
    public static void listTasks() {
        System.out.println("____________________________________________________________\n");
        for (Task task : tasks) {
            System.out.println((tasks.indexOf(task) + 1) + ". " + task);
        }
        System.out.println("____________________________________________________________\n");
    }

    /**
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
        String result =
                "____________________________________________________________\n"
                        + "Okay, I have helped you removed a task, remember to thank me:\n"
                        + taskToDelete + "\n"
                        + "Now you have " + (tasks.size()) + " tasks in the list. Good luck LOL.\n"
                        + "____________________________________________________________\n";
        System.out.println(result);
    }

    /** Saves the current task list to the hard disk. */
    public static void saveTasks() {
        try {
            Files.writeString(
                    TASK_FILE,
                    tasks.stream().map(Task::toString).collect(Collectors.joining(System.lineSeparator()))
                            + System.lineSeparator());
        } catch (IOException e) {
            throw new RuntimeException("Unable to save tasks to " + TASK_FILE, e);
        }
    }
}
