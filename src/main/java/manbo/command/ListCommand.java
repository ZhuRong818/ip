package manbo.command;

import java.util.List;
import manbo.task.Task;
import manbo.storage.Storage;
import manbo.ui.Ui;

/**
 * Represents a command to display all tasks in the task list.
 * This command shows the user the current list of tasks with their details.
 *
 * @author Manbo Development Team
 * @version 1.0
 */
public class ListCommand extends Command {

    /**
     * Executes the list command by displaying all tasks to the user.
     * The method retrieves the current task list and shows it through the UI.
     *
     * @param tasks the list of tasks currently being tracked
     * @param ui the user interface handler for displaying messages
     * @param storage the storage handler for data persistence (not used in this command)
     */
    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) {
        assert tasks != null : "Task list must not be null";
        assert ui != null : "Ui must not be null";
        assert storage != null : "Storage must not be null";

        ui.showList(tasks);
    }


    /**
     * Indicates whether this command should exit the application.
     * List command does not terminate the application.
     *
     * @return false always, as listing tasks doesn't exit the program
     */
    @Override
    public boolean isExit() {
        return false;
    }
}