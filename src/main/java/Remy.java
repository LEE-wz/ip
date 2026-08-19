import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;

public class Remy {
    public static ArrayList<Task> tasks2 = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        getGreetMessage();

        String message = scanner.nextLine();
        boolean stop = false;
        while (true) {
            try {
                switch (message) {
                    case null -> throw new RemyException("You didn't type anything bruh D:");

                    case String msg when msg.equals("bye") -> stop = true;

                    case String msg when msg.equals("list") -> listTasks();

                    case String msg when msg.startsWith("delete") -> deleteTask(msg);

                    case String msg when msg.startsWith("mark") -> markTaskAsDone(msg);

                    case String msg when msg.startsWith("unmark") -> markTaskAsUndone(msg);

                    case String msg when msg.startsWith("todo") -> addTodo(msg);

                    case String msg when msg.startsWith("deadline") -> addDeadline(msg);

                    case String msg when msg.startsWith("event") -> addEvent(msg);

                    default -> throw new RemyException();
                }

            } catch (RemyException e) {
                System.out.println(e.getMessage());
            }

            if (stop) {
                break;
            }

            message = scanner.nextLine();
        }
        scanner.close();

        getExitMessage();
    }

    public static void getGreetMessage() {
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

    public static void getExitMessage() {
        System.out.println(
                """
                        ____________________________________________________________
                        Cya. Call me when you need me!
                        ____________________________________________________________
                        """);
    }

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

        if (formattedIndex > tasks2.size() || formattedIndex < 1) {
            throw new RemyException("your index is out of range :/");
        }

        tasks2.get(formattedIndex - 1).markAsDone();
    }

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

        if (formattedIndex > tasks2.size() || formattedIndex < 1) {
            throw new RemyException("your index is out of range :/");
        }

        tasks2.get(formattedIndex - 1).markAsUndone();
    }

    public static void addTask(Task task) {
        tasks2.add(task);
        String result =
                "____________________________________________________________\n"
                        + "Okay, I have helped you create a task:\n"
                        + task + "\n"
                        + "Now you have " + (tasks2.size()) + " task(s) in the list. Better hurry before it piles up!.\n"
                        + "____________________________________________________________\n";

        System.out.println(result);
    }

    public static void addTodo(String message) {
        if (message.strip().length() == 4) {
            throw new RemyException(false);
        }

        String description = message.substring(4);
        Todo newTodo = new Todo(description);
        addTask(newTodo);
    }

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

        boolean missingDescription = description.isEmpty();
        boolean missingDeadline = deadline.isEmpty();

        if (missingDescription || missingDeadline) {
            throw new RemyException(!missingDescription, !missingDeadline);
        }

        Deadline newDeadline = new Deadline(description, deadline);
        addTask(newDeadline);
    }

    public static void addEvent(String message) {
        if (message.length() == 5) {
            throw new RemyException(false, false, false);
        }

        String[] messageSplit = message.split("/from|/to", 0);
        if (messageSplit.length == 1) {
            throw new RemyException(true, false, false);
        }

        String description = messageSplit[0].substring(5).strip();
        boolean missingDescription = description.isEmpty();

        if (messageSplit.length == 2) {
            boolean containsFrom = message.contains("/from");
            boolean containsTo = message.contains("/to");
            throw new RemyException(!missingDescription, containsFrom, containsTo);
        }

        String start = messageSplit[1].strip();
        String end = messageSplit[2].strip();

        boolean missingStart = start.isEmpty();
        boolean missingEnd = end.isEmpty();

        if (missingDescription || missingStart || missingEnd) {
            throw new RemyException(!missingDescription, !missingStart, !missingEnd);
        }

        Event newEvent = new Event(description, start, end);
        addTask(newEvent);
    }

    public static void listTasks() {
        System.out.println("____________________________________________________________\n");

        for (Task task : tasks2) {
            System.out.println((tasks2.indexOf(task) + 1) + ". " + task);
        }

        System.out.println("____________________________________________________________\n");
    }

    public static void deleteTask(String message) {
        if (tasks2.isEmpty()) {
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

        if (formattedIndex > tasks2.size() || formattedIndex < 1) {
            throw new RemyException("Your index is out of range :/");
        }

        Task taskToDelete = tasks2.get(formattedIndex - 1);

        tasks2.remove(taskToDelete);

        String result =
                "____________________________________________________________\n"
                        + "Okay, I have helped you removed a task, remember to thank me:\n"
                        + taskToDelete + "\n"
                        + "Now you have " + (tasks2.size()) + " tasks in the list. Good luck LOL.\n"
                        + "____________________________________________________________\n";

        System.out.println(result);
    }
}
