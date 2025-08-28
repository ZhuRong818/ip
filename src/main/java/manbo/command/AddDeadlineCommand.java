package manbo.command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import manbo.task.*;
import manbo.storage.Storage;
import manbo.ui.Ui;
import manbo.exceptions.*;

public class AddDeadlineCommand extends Command {
    private static final DateTimeFormatter IN_DATE = DateTimeFormatter.ISO_LOCAL_DATE;
    private final String description;
    private final String byStr;

    public AddDeadlineCommand(String description, String byStr) {
        this.description = description;
        this.byStr = byStr;
    }

    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) throws ManboException {
        if (description == null || description.isBlank()) throw new EmptyDescriptionException("deadline");
        if (byStr == null || byStr.isBlank()) throw new ManboException("Please specify the /by date as yyyy-MM-dd.");

        try {
            LocalDate by = LocalDate.parse(byStr, IN_DATE);
            Task t = new Deadline(description.trim(), by);
            tasks.add(t);
            storage.save(tasks);
            ui.info("Got it. I've added this task:\n  " + t + "\n Now you have " + tasks.size() + " tasks in the list.");
        } catch (DateTimeParseException e) {
            throw new ManboException("Invalid date format/value. Use yyyy-MM-dd (e.g., 2019-12-02).");
        }
    }
}
