package manbo.command;

import java.util.ArrayList;
import java.util.List;
import manbo.task.Task;
import manbo.storage.Storage;
import manbo.ui.Ui;
import manbo.exceptions.EmptyDescriptionException;
import manbo.exceptions.ManboException;

/**
 * Finds tasks whose descriptions contain a given keyword (case-insensitive).
 * Prints a compact list of matches, numbered from 1..N.
 */
public class FindCommand extends Command {

    private final String keyword;

    /**
     * @param keyword search keyword (must be non-blank)
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) throws ManboException {
        if (keyword == null || keyword.isBlank()) {
            throw new EmptyDescriptionException("find");
        }

        String k = keyword.trim().toLowerCase();
        List<Task> matches = new ArrayList<>();

        for (Task t : tasks) {
            // assume Task has getDescription(); fall back to t.toString() if needed
            String desc = t.getDescription().toLowerCase();
            if (desc.contains(k)) {
                matches.add(t);
            }
        }

        ui.showMatches(matches);
    }
}
