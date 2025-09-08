package manbo.storage;

import manbo.task.Task;
import manbo.task.Todo;
import manbo.task.Deadline;
import manbo.task.Event;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Handles reading from and writing to the persistent storage file.
 * <p>
 * The storage file contains serialized {@link Task} objects
 * in a line-based format:
 * <ul>
 *     <li>Todo: {@code T | 1 | description}</li>
 *     <li>Deadline: {@code D | 0 | description | yyyy-MM-dd}</li>
 *     <li>Event: {@code E | 1 | description | yyyy-MM-dd HHmm | yyyy-MM-dd HHmm}</li>
 * </ul>
 * where the second field {@code 1/0} indicates done/not-done.
 */
public class Storage {
    /** Backing file for persistent task storage. */
    private final File file;

    /** Date formatter used for {@link Deadline} dates (yyyy-MM-dd). */
    private static final DateTimeFormatter DATE  = DateTimeFormatter.ISO_LOCAL_DATE;

    /** Date-time formatter used for {@link Event} start/end (yyyy-MM-dd HHmm). */
    private static final DateTimeFormatter DTTM  = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Creates a new {@code Storage} instance tied to the given file path.
     * Ensures the file and parent directories exist.
     *
     * @param path the file path to use for persistence (e.g., "data/manbo.txt")
     */
    public Storage(String path) {
        // Dev invariant: parser/framework should not pass null/blank paths
        assert path != null && !path.isBlank() : "Storage path must not be null/blank";

        this.file = new File(path);
        createNonExistentFile();

        // Postcondition: after creation, file should exist and be a regular file
        assert file.exists() : "Storage file should exist after initialization";
        assert file.isFile() : "Storage path should refer to a regular file, not a directory";
    }

    /**
     * Ensures that the storage file and its parent directories exist.
     * Creates them if necessary.
     */
    private void createNonExistentFile() {
        try {
            File directory = file.getParentFile();
            if (directory != null && !directory.exists()) {
                directory.mkdirs();

            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }

    /**
     * Loads all tasks from storage.
     *
     * @return a list of {@link Task} objects, possibly empty but never null
     */
    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        assert file.isFile() : "Storage file missing during load";

        try (Scanner s = new Scanner(file, "UTF-8")) {
            while (s.hasNextLine()) {
                String line = s.nextLine().trim();
                if (line.isEmpty()) continue;
                Task t = decodeLine(line);
                if (t != null) tasks.add(t);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Saves the given list of tasks to storage, overwriting existing contents.
     *
     * @param tasks the tasks to write
     */
    public void save(List<Task> tasks) {

        assert tasks != null : "Tasks list must not be null";
        try (FileWriter fw = new FileWriter(file);
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (Task t : tasks) {
                bw.write(t.toSaveFormat()); // delegate to Task serialization
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    /**
     * Decodes a single line from storage into a {@link Task}.
     *
     * <p>Expected formats:
     * <ul>
     *   <li>{@code T | 1 | description}</li>
     *   <li>{@code D | 0 | description | yyyy-MM-dd}</li>
     *   <li>{@code E | 1 | description | yyyy-MM-dd HHmm | yyyy-MM-dd HHmm}</li>
     * </ul>
     *
     * @param line the raw line from the storage file
     * @return the corresponding {@link Task}, or {@code null} if malformed
     */
    private Task decodeLine(String line) {
        assert line != null && !line.isBlank() : "decodeLine requires a non-blank line";

        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) return null;

        String tag = parts[0];
        boolean done = "1".equals(parts[1]);
        String desc = parts[2];

        try {
            switch (tag) {
                case "T": {
                    Task t = new Todo(desc);
                    if (done) t.markAsDone();
                    return t;
                }
                case "D": { // D | done | desc | yyyy-MM-dd
                    if (parts.length < 4) return null;
                    LocalDate by = LocalDate.parse(parts[3], DATE);
                    Task t = new Deadline(desc, by);
                    if (done) t.markAsDone();
                    return t;
                }
                case "E": { // E | done | desc | yyyy-MM-dd HHmm | yyyy-MM-dd HHmm
                    if (parts.length < 5) return null;
                    LocalDateTime from = LocalDateTime.parse(parts[3], DTTM);
                    LocalDateTime to   = LocalDateTime.parse(parts[4], DTTM);
                    Task t = new Event(desc, from, to);
                    if (done) t.markAsDone();
                    return t;
                }
                default:
                    return null; // unknown tag
            }
        } catch (Exception ex) {
            // Malformed line → skip or log
            return null;
        }
    }
}
