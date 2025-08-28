package manbo.command;

import java.util.List;
import manbo.task.Task;
import manbo.storage.Storage;
import manbo.ui.Ui;
import manbo.exceptions.*;

public class MarkCommand extends Command {
    private final int index; // 0-based
    public MarkCommand(int index) { this.index = index; }

    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) throws ManboException {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfRangeException(index + 1, tasks.size());
        }
        Task t = tasks.get(index);
        t.markAsDone();
        storage.save(tasks);
        ui.info("Nice! I've marked this task as done:\n" + t);
    }
}
