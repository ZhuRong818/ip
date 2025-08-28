package manbo.command;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import manbo.task.*;
import manbo.storage.Storage;
import manbo.ui.Ui;
import manbo.exceptions.*;

/**
 * Represents a command that adds an {@link manbo.task.Event} task
 * to the task list. An event requires a description, a start datetime,
 * and an end datetime, all in {@code yyyy-MM-dd HHmm} format.
 *
 * <p>Example usage:
 * <pre>
 *     Command cmd = new AddEventCommand("Team meeting",
 *                                       "2025-09-01 1000",
 *                                       "2025-09-01 1200");
 *     cmd.execute(tasks, ui, storage);
 * </pre>
 *
 * If description is empty or datetime strings are invalid,
 * a {@link ManboException} is thrown.
 */
public class AddEventCommand extends Command {
    /** Accepted datetime format: yyyy-MM-dd HHmm (e.g., 2025-09-01 0930). */
    private static final DateTimeFormatter IN_DT_TIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private final String description;
    private final String fromDateTime; // Start datetime string
    private final String toDateTime;   // End datetime string

    /**
     * Creates a new {@code AddEventCommand}.
     *
     * @param description description of the event
     * @param fromDateTime start datetime string (yyyy-MM-dd HHmm)
     * @param toDateTime   end datetime string (yyyy-MM-dd HHmm)
     */
    public AddEventCommand(String description, String fromDateTime, String toDateTime) {
        this.description = description;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
    }

    /**
     * Executes the command: validates input, creates an {@link Event} task,
     * adds it to the list, persists to storage, and notifies the user.
     *
     * @param tasks   the task list to add the new task to
     * @param ui      the UI for user feedback
     * @param storage the storage for persisting tasks
     * @throws ManboException if description is empty or datetime values are invalid
     */
    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) throws ManboException {
        // Ensure description is present
        if (description == null || description.isBlank()) {
            throw new EmptyDescriptionException("event");
        }

        try {
            // Parse input datetime strings
            LocalDateTime from = LocalDateTime.parse(fromDateTime, IN_DT_TIME);
            LocalDateTime to   = LocalDateTime.parse(toDateTime, IN_DT_TIME);

            // Construct new Event task and add to list
            Task t = new Event(description.trim(), from, to);
            tasks.add(t);

            // Save to persistent storage
            storage.save(tasks);

            // Notify user
            ui.info("Got it. I've added this task:\n  " + t
                    + "\n Now you have " + tasks.size() + " tasks in the list.");
        } catch (DateTimeParseException e) {
            throw new ManboException("Invalid /from or /to. Use yyyy-MM-dd HHmm (e.g., 2019-12-02 0930).");
        }
    }
}
