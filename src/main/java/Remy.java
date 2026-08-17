import java.util.Objects;
import java.util.Scanner;

public class Remy {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String banner = getBannerMessage();
        String greetMsg = getGreetMessage();
        String exitMsg = getExitMessage();

        System.out.println(banner);
        System.out.println(greetMsg);
        String message = scanner.nextLine();
        while (!message.equals("bye")) {
            System.out.println(echoMessage(message));
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
}
