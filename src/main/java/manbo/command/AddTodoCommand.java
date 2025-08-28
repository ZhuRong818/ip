package manbo.command;

import java.util.List;
import manbo.task.*;
import manbo.storage.Storage;
import manbo.ui.Ui;
import manbo.exceptions.*;

public class AddTodoCommand extends Command {
    private final String description;
    public AddTodoCommand(String description) { this.description = description; }

    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) throws ManboException {
        if (description == null || description.isBlank()) throw new EmptyDescriptionException("todo");
        Task t = new Todo(description.trim());
        tasks.add(t);
        storage.save(tasks);
        ui.info("Got it. I've added this task:\n  " + t + "\n Now you have " + tasks.size() + " tasks in the list.");
    }
}
