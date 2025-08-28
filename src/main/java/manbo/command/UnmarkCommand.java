package manbo.command;

import java.util.List;
import manbo.task.Task;
import manbo.storage.Storage;
import manbo.ui.Ui;
import manbo.exceptions.*;

public class UnmarkCommand extends Command {
    private final int index;
    public UnmarkCommand(int index) { this.index = index; }

    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) throws ManboException {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfRangeException(index + 1, tasks.size());
        }
        Task t = tasks.get(index);
        t.unmarkAsDone();
        storage.save(tasks);
        ui.info("OK, I've marked this task as not done yet:\n" + t);
    }
}
