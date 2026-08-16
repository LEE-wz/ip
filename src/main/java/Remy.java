public class Remy {
    public static void main(String[] args) {
        String banner = getBannerMessage();
        String greetMsg = getGreetMessage();
        String exitMsg = getExitMessage();

        System.out.println(banner);
        System.out.println(greetMsg);
        System.out.println(exitMsg);
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
            + "____________________________________________________________";
    }

    public static String getExitMessage() {
        return
                  "Bye. Hope to see you again soon!\n"
                + "____________________________________________________________";
    }
}
