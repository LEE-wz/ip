package remy;

import java.io.IOException;

import remy.command.Command;
import remy.exception.RemyException;
import remy.parser.Parser;
import remy.storage.Storage;
import remy.task.TaskList;
import remy.ui.Ui;

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

    /** Runs the chatbot command loop. */
    public static void main(String[] args) {
        loadTasks();
        UI.showGreeting();
        boolean canStopLoop = false;

        while (!canStopLoop) {
            try {
                String message = UI.readCommand();
                if (message == null) {
                    throw new RemyException("You didn't type anything bruh D:");
                }

                Command command = PARSER.parse(message);
                command.execute(tasks, UI, STORAGE);
                canStopLoop = command.isExit();
            } catch (RemyException e) {
                UI.showError(e.getMessage());
            }
        }

        UI.close();
        UI.showFarewell();
    }

    /** Loads saved tasks from the hard disk when the chatbot starts. */
    public static void loadTasks() {
        try {
            tasks = STORAGE.load();
        } catch (IOException | SecurityException e) {
            UI.showLoadingError();
        }
    }
}
