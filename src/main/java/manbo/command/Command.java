package manbo.command;

import java.util.List;
import manbo.task.Task;
import manbo.storage.Storage;
import manbo.ui.Ui;
import manbo.exceptions.ManboException;

/**
 * Represents an abstract command in the Manbo application.
 * All concrete commands (e.g., {@link AddTodoCommand}, {@link AddDeadlineCommand})
 * must extend this class and implement the {@link #execute(List, Ui, Storage)} method.
 *
 * Commands encapsulate user actions and define how to manipulate the task list,
 * interact with storage, and produce UI feedback.
 */
public abstract class Command {

    /**
     * Executes the command.
     *
     * @param tasks   the current task list
     * @param ui      the UI used for displaying messages
     * @param storage the storage used to persist changes
     * @throws ManboException if the command fails (invalid input, storage error, etc.)
     */
    public abstract void execute(List<Task> tasks, Ui ui, Storage storage) throws ManboException;

    /**
     * Indicates whether this command will exit the application.
     * By default, commands do not exit.
     *
     * @return {@code true} if this command signals program termination; {@code false} otherwise
     */
    public boolean isExit() {
        return false;
    }
}
