package manbo.command;

import java.util.List;
import manbo.task.Task;
import manbo.storage.Storage;
import manbo.ui.Ui;
import manbo.exceptions.*;

/**
 * Represents a command that unmarks a {@link Task} (sets it as not done).
 * The task to unmark is identified by its index (0-based).
 *
 * <p>Example usage:
 * <pre>
 *     Command cmd = new UnmarkCommand(1); // unmarks the 2nd task
 *     cmd.execute(tasks, ui, storage);
 * </pre>
 *
 * If the index is invalid (out of range), an {@link IndexOutOfRangeException} is thrown.
 */
public class UnmarkCommand extends Command {

    /** Index of the task to unmark as not done (0-based). */
    private final int index;

    /**
     * Creates a new {@code UnmarkCommand}.
     *
     * @param index zero-based index of the task to unmark
     */
    public UnmarkCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the command: validates the index, unmarks the task,
     * saves changes to storage, and notifies the user.
     *
     * @param tasks   the task list containing the task
     * @param ui      the UI for displaying messages
     * @param storage the storage to persist changes
     * @throws ManboException if the index is out of range
     */
    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) throws ManboException {

        assert tasks != null : "Task list must not be null";
        assert ui != null : "Ui must not be null";
        assert storage != null : "Storage must not be null";
        // Validate index
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfRangeException(index + 1, tasks.size());
        }

        // Retrieve and unmark task
        Task t = tasks.get(index);
        t.unmarkAsDone();

        // Persist changes
        storage.save(tasks);

        // Notify user
        ui.info("OK, I've marked this task as not done yet:\n" + t);
    }
}
