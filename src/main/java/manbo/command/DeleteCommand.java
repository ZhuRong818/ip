package manbo.command;

import java.util.List;
import manbo.task.Task;
import manbo.storage.Storage;
import manbo.ui.Ui;
import manbo.exceptions.*;

/**
 * Represents a command that deletes a {@link Task} from the task list.
 * The task to delete is identified by its index (0-based).
 *
 * <p>Example usage:
 * <pre>
 *     Command cmd = new DeleteCommand(2); // deletes the 3rd task
 *     cmd.execute(tasks, ui, storage);
 * </pre>
 *
 * If the index is invalid (out of range), an {@link IndexOutOfRangeException} is thrown.
 */
public class DeleteCommand extends Command {

    /** Index of the task to delete (0-based). */
    private final int index;

    /**
     * Creates a new {@code DeleteCommand}.
     *
     * @param index zero-based index of the task to delete
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the command: validates the index, removes the task,
     * updates storage, and informs the user.
     *
     * @param tasks   the task list from which to remove the task
     * @param ui      the UI for displaying messages
     * @param storage the storage to persist changes
     * @throws ManboException if the index is out of range
     */
    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) throws ManboException {
        assert tasks != null : "Task list must not be null";
        assert ui != null : "Ui must not be null";
        assert storage != null : "Storage must not be null";
        // Validate index range
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfRangeException(index + 1, tasks.size());
        }

        // Remove task at given index
        Task removed = tasks.remove(index);

        // Persist updated task list
        storage.save(tasks);

        // Notify user
        ui.info("Noted. I've removed this task:\n  " + removed
                + "\n Now you have " + tasks.size() + " tasks in the list.");
    }
}
