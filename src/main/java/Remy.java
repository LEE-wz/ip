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
        while (!message.equals("bye")) {

            if (message.equals("list")) {
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

            } else {
                addTask(message);
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

    public static void addTask(String taskDescription) {
        if (nextFreePointer >= 100) {
            return;
        }

        Task newTask = new Task(taskDescription);
        tasks[nextFreePointer] = newTask;
        nextFreePointer++;
        String result =
                  "____________________________________________________________\n"
                + "added: " + newTask.description + "\n"
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
