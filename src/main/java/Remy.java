import java.util.Objects;
import java.util.Scanner;

public class Remy {
    public static String[] messages = new String[100];
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
                listMessages();
            } else {
                addMessage(message);
            }

            message = scanner.nextLine();
        }

        System.out.println(exitMsg);

        scanner.close();
    }

    public static String getBannerMessage() {
        return
                  "____________________________________________________________\n"
                + " _____                      \n"
                + "|  __ \\                     \n"
                + "| |__) | ___ _ __ ___  _   _\n"
                + "|  _  / / _ \\ '_ ` _ \\| | | |\n"
                + "| | \\ \\|  __/ | | | | | |_| |\n"
                + "|_|  \\_\\\\___|_| |_| |_|\\__, |\n"
                + "                        __/ |\n"
                + "                       |___/ \n";
    }

    public static String getGreetMessage() {
        return
              "Hello! I'm Remy.\n"
            + "What can I do for you?\n"
            + "\n"
            + "____________________________________________________________\n";
    }

    public static String getExitMessage() {
        return
                  "____________________________________________________________\n"
                + "Bye. Hope to see you again soon!\n"
                + "____________________________________________________________\n";
    }

    public static String echoMessage(String msg) {
        return
                  "____________________________________________________________\n"
                + msg + "\n"
                + "____________________________________________________________\n";
    }

    public static void addMessage(String message) {
        if (nextFreePointer >= 100) {
            return;
        }
        messages[nextFreePointer] = message;
        nextFreePointer++;
        String result =
                  "____________________________________________________________\n"
                + "added: " + message + "\n"
                + "____________________________________________________________\n";


        System.out.println(result);
    }

    public static void listMessages() {
        System.out.println("____________________________________________________________\n");

        for (int index = 0; index < messages.length; index++) {
            if (messages[index] == null) {
                break;
            }

            System.out.println((index + 1) + ". " + messages[index]);
        }

        System.out.println("____________________________________________________________\n");
    }
}
