package manbo.command;

import java.util.List;
import manbo.task.Task;
import manbo.storage.Storage;
import manbo.ui.Ui;

public class ExitCommand extends Command {
    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) {
        ui.sayBye();
    }
    @Override
    public boolean isExit() { return true; }
}
