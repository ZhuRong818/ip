package manbo;

import java.util.*;
import manbo.storage.Storage;
import manbo.task.Task;
import manbo.ui.Ui;
import manbo.parser.Parser;
import manbo.command.Command;
import manbo.exceptions.ManboException;

/**
 * Main class for the Manbo task management application.
 * Manbo is a personal task tracker that helps users manage their todos, deadlines, and events.
 * The application supports adding, listing, marking, unmarking, and deleting tasks.
 *
 * @author Manbo Development Team
 * @version 1.0
 */
public class Manbo {
    private final Storage storage = new Storage("data/manbo.txt");
    private final List<Task> tasks = new ArrayList<>();
    private final Ui ui = new Ui();

    /**
     * Main entry point for the Manbo application.
     * Creates a new instance of Manbo and starts the application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        new Manbo().run();
    }

    /**
     * Runs the main application loop.
     * Initializes the application, loads existing tasks from storage,
     * and processes user commands until an exit command is received.
     * Handles exceptions and displays appropriate error messages to the user.
     */
    public void run() {
        ui.showWelcome();
        tasks.addAll(storage.load());

        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand().trim();
                ui.showLine();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (ManboException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

// In manbo/Manbo.java

    /** A tiny result type for GUI use. */
    public static final class Reply {
        public final String text;
        public final boolean isExit;
        public Reply(String text, boolean exit) { this.text = text; this.isExit = exit; }
    }

    /** Run one user input through the *same* pipeline the CLI uses. */
    public Reply handle(String input) {
        try {
            // If your Ui has no reset(), just remove the next line.
            ui.reset();

            Command c = Parser.parse(input);
            c.execute(tasks, ui, storage);   // exactly what the CLI does
            // If your commands already save, you can omit the next line.
            storage.save(tasks);

            return new Reply(ui.out(), c.isExit());  // ui.out() is what your tests used
        } catch (ManboException e) {
            return new Reply(e.getMessage(), false);
        }
    }



}