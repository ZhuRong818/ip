package manbo.command;

import java.util.List;
import manbo.task.Task;
import manbo.storage.Storage;
import manbo.ui.Ui;
import manbo.exceptions.ManboException;

public abstract class Command {
    public abstract void execute(List<Task> tasks, Ui ui, Storage storage) throws ManboException;
    public boolean isExit() { return false; }
}
