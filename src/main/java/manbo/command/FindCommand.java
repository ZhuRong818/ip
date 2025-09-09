package manbo.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
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
        assert tasks != null : "Task list must not be null";
        assert ui != null : "Ui must not be null";
        assert storage != null : "Storage must not be null";

        if (keyword == null || keyword.isBlank()) {
            throw new EmptyDescriptionException("find");
        }

        final String k = keyword.trim().toLowerCase();

        List<Task> matches = tasks.stream()
                .filter(t -> {
                    assert t != null : "Task list contains null";
                    String desc = t.getDescription();     // or t.toString() if that’s what you show
                    assert desc != null : "Task description must not be null";
                    return desc.toLowerCase(Locale.ROOT).contains(k);
                })
                .collect(Collectors.toList());

        ui.showMatches(matches);
    }
}
