package manbo.command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import manbo.task.*;
import manbo.storage.Storage;
import manbo.ui.Ui;
import manbo.exceptions.*;

/**
 * Represents a command that adds a {@link manbo.task.Deadline} task
 * to the task list. The deadline requires a description and a date in
 * {@code yyyy-MM-dd} format.
 *
 * <p>Example usage:
 * <pre>
 *     Command cmd = new AddDeadlineCommand("Submit report", "2025-09-01");
 *     cmd.execute(tasks, ui, storage);
 * </pre>
 *
 * If the description is empty or the date format is invalid,
 * a {@link ManboException} is thrown.
 */
public class AddDeadlineCommand extends Command {
    /** Accepted date format: yyyy-MM-dd (ISO_LOCAL_DATE). */
    private static final DateTimeFormatter IN_DATE = DateTimeFormatter.ISO_LOCAL_DATE;

    private final String description;
    private final String byStr;

    /**
     * Creates a new {@code AddDeadlineCommand}.
     *
     * @param description description of the deadline task
     * @param byStr deadline date as a string in yyyy-MM-dd format
     */
    public AddDeadlineCommand(String description, String byStr) {
        this.description = description;
        this.byStr = byStr;
    }

    /**
     * Executes the command: validates input, creates a {@link Deadline} task,
     * adds it to the list, persists to storage, and notifies the user.
     *
     * @param tasks   the task list to add the new task to
     * @param ui      the UI for user feedback
     * @param storage the storage for persisting tasks
     * @throws ManboException if description is empty, date is missing, or date format is invalid
     */
    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) throws ManboException {
        // Ensure description is present
        if (description == null || description.isBlank()) {
            throw new EmptyDescriptionException("deadline");
        }
        // Ensure /by argument is present
        if (byStr == null || byStr.isBlank()) {
            throw new ManboException("Please specify the /by date as yyyy-MM-dd.");
        }

        try {
            // Parse date string into LocalDate
            LocalDate by = LocalDate.parse(byStr, IN_DATE);

            // Construct new Deadline task and add to list
            Task t = new Deadline(description.trim(), by);
            tasks.add(t);

            // Save to persistent storage
            storage.save(tasks);

            // Notify user
            ui.info("Got it. I've added this task:\n  " + t
                    + "\n Now you have " + tasks.size() + " tasks in the list.");
        } catch (DateTimeParseException e) {
            throw new ManboException("Invalid date format/value. Use yyyy-MM-dd (e.g., 2019-12-02).");
        }
    }
}
