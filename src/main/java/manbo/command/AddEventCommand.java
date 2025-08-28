package manbo.command;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import manbo.task.*;
import manbo.storage.Storage;
import manbo.ui.Ui;
import manbo.exceptions.*;

public class AddEventCommand extends Command {
    private static final DateTimeFormatter IN_DT_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private final String description;
    private final String fromDateTime; // "yyyy-MM-dd HHmm"
    private final String toDateTime;   // same; parser can normalize HHmm shortcut

    public AddEventCommand(String description, String fromDateTime, String toDateTime) {
        this.description = description;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
    }

    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) throws ManboException {
        if (description == null || description.isBlank()) throw new EmptyDescriptionException("event");
        try {
            LocalDateTime from = LocalDateTime.parse(fromDateTime, IN_DT_TIME);
            LocalDateTime to   = LocalDateTime.parse(toDateTime,   IN_DT_TIME);
            Task t = new Event(description.trim(), from, to);
            tasks.add(t);
            storage.save(tasks);
            ui.info("Got it. I've added this task:\n  " + t + "\n Now you have " + tasks.size() + " tasks in the list.");
        } catch (DateTimeParseException e) {
            throw new ManboException("Invalid /from or /to. Use yyyy-MM-dd HHmm or HHmm (normalized).");
        }
    }
}
