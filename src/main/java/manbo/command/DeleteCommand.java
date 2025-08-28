package manbo.command;

import java.util.List;
import manbo.task.Task;
import manbo.storage.Storage;
import manbo.ui.Ui;
import manbo.exceptions.*;

public class DeleteCommand extends Command {
    private final int index;
    public DeleteCommand(int index) { this.index = index; }

    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) throws ManboException {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfRangeException(index + 1, tasks.size());
        }
        Task removed = tasks.remove(index);
        storage.save(tasks);
        ui.info("Noted. I've removed this task:\n  " + removed
                + "\n Now you have " + tasks.size() + " tasks in the list.");
    }
}
