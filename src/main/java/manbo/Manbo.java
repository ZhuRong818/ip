package manbo;

import java.util.*;
import manbo.storage.Storage;
import manbo.task.Task;
import manbo.ui.Ui;
import manbo.parser.Parser;
import manbo.command.Command;
import manbo.exceptions.ManboException;

public class Manbo {
    private final Storage storage = new Storage("data/manbo.txt");
    private final List<Task> tasks = new ArrayList<>();
    private final Ui ui = new Ui();

    public static void main(String[] args) {
        new Manbo().run();
    }

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
}
