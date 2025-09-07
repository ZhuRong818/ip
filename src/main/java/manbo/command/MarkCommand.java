package manbo.command;

import java.util.List;
import manbo.task.Task;
import manbo.storage.Storage;
import manbo.ui.Ui;
import manbo.exceptions.*;

/**
 * Represents a command that marks a {@link Task} as done in the task list.
 * The task to mark is identified by its index (0-based).
 *
 * <p>Example usage:
 * <pre>
 *     Command cmd = new MarkCommand(1); // marks the 2nd task as done
 *     cmd.execute(tasks, ui, storage);
 * </pre>
 *
 * If the index is invalid (out of range), an {@link IndexOutOfRangeException} is thrown.
 */
public class MarkCommand extends Command {

    /** Index of the task to mark as done (0-based). */
    private final int index;

    /**
     * Creates a new {@code MarkCommand}.
     *
     * @param index zero-based index of the task to mark
     */
    public MarkCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the command: validates the index, marks the task as done,
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

        // Retrieve and mark task as done
        Task t = tasks.get(index);
        t.markAsDone();

        // Persist changes
        storage.save(tasks);

        // Notify user
        ui.info("Nice! I've marked this task as done:\n" + t);
    }
}
