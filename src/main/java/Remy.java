import java.util.Objects;
import java.util.Scanner;

public class Remy {
    public static Task[] tasks = new Task[100];
    public static int nextFreePointer = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String banner = getBannerMessage();
        String greetMsg = getGreetMessage();
        String exitMsg = getExitMessage();

        System.out.println(banner);
        System.out.println(greetMsg);

        String message = scanner.nextLine();
        while (true) {
            try {
                if (message.equals("bye")) {
                    break;

                } else if (message.equals("list")) {
                    listTasks();

                } else if (message.startsWith("mark")) {
                    if (message.length() > 4) {
                        String idx = message.substring(4).strip();
                        int formattedIndex = Integer.parseInt(idx);
                        markTaskAsDone(formattedIndex - 1);
                    }

                } else if (message.startsWith("unmark")) {
                    if (message.length() > 6) {
                        String idx = message.substring(6).strip();
                        int formattedIndex = Integer.parseInt(idx);
                        markTaskAsUndone(formattedIndex - 1);
                    }

                } else if (message.startsWith("todo")) {
                    if (message.strip().length() == 4) {
                        throw new RemyException(false);
                    }

                    String description = message.substring(4);
                    Todo newTodo = new Todo(description);
                    addTask(newTodo);

                } else if (message.startsWith("deadline")) {
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

                } else if (message.startsWith("event")) {
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

                } else {
                    throw new RemyException();
                }

            } catch (RemyException e) {
                System.out.println(e.getMessage());
            }

            message = scanner.nextLine();
        }
        scanner.close();

        System.out.println(exitMsg);
    }

    public static String getBannerMessage() {
        return
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
                        """;
    }

    public static String getGreetMessage() {
        return
                """
                        Hello! I'm Remy.
                        What can I do for you?
                        
                        ____________________________________________________________
                        """;
    }

    public static String getExitMessage() {
        return
                """
                        ____________________________________________________________
                        Bye. Hope to see you again soon!
                        ____________________________________________________________
                        """;
    }

    public static void addTask(Task task) {
        if (nextFreePointer >= 100) {
            return;
        }

        tasks[nextFreePointer] = task;
        nextFreePointer++;
        String result =
                  "____________________________________________________________\n"
                + "Got it. I've added this task:\n"
                + task + "\n"
                + "Now you have " + (nextFreePointer - 1) + " tasks in the list.\n"
                + "____________________________________________________________\n";

        System.out.println(result);
    }

    public static void markTaskAsDone(int index) {
        if (index >= nextFreePointer) {
            return;
        }

        tasks[index].markAsDone();
    }

    public static void markTaskAsUndone(int index) {
        if (index >= nextFreePointer) {
            return;
        }

        tasks[index].markAsUndone();
    }

    public static void listTasks() {
        System.out.println("____________________________________________________________\n");

        for (int index = 0; index < tasks.length; index++) {
            if (tasks[index] == null) {
                break;
            }

            System.out.println((index + 1) + ". " + tasks[index]);
        }

        System.out.println("____________________________________________________________\n");
    }
}
