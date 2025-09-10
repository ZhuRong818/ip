package manbo.command;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import manbo.storage.Storage;
import manbo.task.Deadline;
import manbo.task.Event;
import manbo.task.Task;
import manbo.task.Todo;
import manbo.ui.Ui;

/**
 * Shows high-level statistics about tasks.
 *
 * Summary printed (example):
 * === Statistics ===
 * Total tasks      : 25
 * Completed        : 15 (60.0%)
 * Incomplete       : 10
 * By type:
 *   Todos          : 12 (done 7)
 *   Deadlines      : 8  (done 5)
 *   Events         : 5  (done 3)
 */
public class StatsCommand extends Command {

    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) {
        // Developer invariants
        assert tasks != null : "Task list must not be null";
        assert ui != null : "Ui must not be null";
        assert storage != null : "Storage must not be null";

        final int total = tasks.size();

        final long done = tasks.stream()
                .filter(Task::ifDone)          // Task has ifDone()
                .count();

        final long notDone = total - done;

        double pct = total == 0 ? 0.0 : (done * 100.0) / total;

        final long todos = tasks.stream().filter(t -> t instanceof Todo).count();
        final long todosDone = tasks.stream().filter(t -> t instanceof Todo && t.ifDone()).count();

        final long deadlines = tasks.stream().filter(t -> t instanceof Deadline).count();
        final long deadlinesDone = tasks.stream().filter(t -> t instanceof Deadline && t.ifDone()).count();

        final long events = tasks.stream().filter(t -> t instanceof Event).count();
        final long eventsDone = tasks.stream().filter(t -> t instanceof Event && t.ifDone()).count();

        String msg = ""
                + "=== Statistics ===\n"
                + String.format(Locale.ROOT, "Total tasks      : %d%n", total)
                + String.format(Locale.ROOT, "Completed        : %d (%.1f%%)%n", done, pct)
                + String.format(Locale.ROOT, "Incomplete       : %d%n", notDone)
                + "By type:\n"
                + String.format(Locale.ROOT, "  Todos          : %d (done %d)%n", todos, todosDone)
                + String.format(Locale.ROOT, "  Deadlines      : %d (done %d)%n", deadlines, deadlinesDone)
                + String.format(Locale.ROOT, "  Events         : %d (done %d)%n", events, eventsDone);

        ui.info(msg);
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
