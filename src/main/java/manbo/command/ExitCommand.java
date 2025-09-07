package manbo.command;

import java.util.List;
import manbo.task.Task;
import manbo.storage.Storage;
import manbo.ui.Ui;

/**
 * Represents a command that terminates the program.
 * When executed, it displays a farewell message and signals
 * the application to exit.
 *
 * <p>Example usage:
 * <pre>
 *     Command cmd = new ExitCommand();
 *     cmd.execute(tasks, ui, storage);
 *     boolean exit = cmd.isExit(); // true
 * </pre>
 */
public class ExitCommand extends Command {

    /**
     * Executes the command: tells the UI to display a goodbye message.
     *
     * @param tasks   the current task list (not used here)
     * @param ui      the UI for displaying the farewell message
     * @param storage the storage (not used here)
     */
    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) {

        assert tasks != null : "Task list must not be null";
        assert ui != null : "Ui must not be null";
        assert storage != null : "Storage must not be null";
        ui.sayBye();
    }

    /**
     * Indicates that this command ends the program.
     *
     * @return always {@code true}
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
