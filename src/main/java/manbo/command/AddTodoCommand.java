package manbo.command;

import java.util.List;
import manbo.task.*;
import manbo.storage.Storage;
import manbo.ui.Ui;
import manbo.exceptions.*;

/**
 * Represents a command that adds a {@link manbo.task.Todo} task
 * to the task list. A todo requires only a description (no date/time).
 *
 * <p>Example usage:
 * <pre>
 *     Command cmd = new AddTodoCommand("Read a book");
 *     cmd.execute(tasks, ui, storage);
 * </pre>
 *
 * If the description is empty, an {@link EmptyDescriptionException} is thrown.
 */
public class AddTodoCommand extends Command {

    /** Description of the todo task to be added. */
    private final String description;

    /**
     * Creates a new {@code AddTodoCommand}.
     *
     * @param description description of the todo task
     */
    public AddTodoCommand(String description) {
        this.description = description;
    }

    /**
     * Executes the command: validates input, creates a {@link Todo} task,
     * adds it to the list, persists to storage, and notifies the user.
     *
     * @param tasks   the task list to add the new task to
     * @param ui      the UI for user feedback
     * @param storage the storage for persisting tasks
     * @throws ManboException if description is missing or blank
     */
    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) throws ManboException {
        // Ensure description is present
        if (description == null || description.isBlank()) {
            throw new EmptyDescriptionException("todo");
        }

        // Construct new Todo task and add to list
        Task t = new Todo(description.trim());
        tasks.add(t);

        // Save to persistent storage
        storage.save(tasks);

        // Notify user
        ui.info("Got it. I've added this task:\n  " + t
                + "\n Now you have " + tasks.size() + " tasks in the list.");
    }
}
