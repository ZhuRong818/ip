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

public class Storage {
    private final File file;

    // Formatters used for event/deadline fields
    private static final DateTimeFormatter DATE  = DateTimeFormatter.ISO_LOCAL_DATE;           // yyyy-MM-dd
    private static final DateTimeFormatter DTTM  = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    public Storage(String path) {
        this.file = new File(path);
        createNonExistentFile();
    }

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

    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        try (Scanner s = new Scanner(file, "UTF-8")) {
            while (s.hasNextLine()) {
                String line = s.nextLine().trim();
                if (line.isEmpty()) continue;
                Task t = decodeLine(line); // ⬅️ no Parser dependency
                if (t != null) tasks.add(t);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
        return tasks;
    }

    public void save(List<Task> tasks) {
        try (FileWriter fw = new FileWriter(file);
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (Task t : tasks) {
                // If you already have Task.toSaveFormat(), keep using it:
                bw.write(t.toSaveFormat());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    /**
     * Decode a single line from storage into a Task instance.
     * Expected formats:
     *  T | 1 | description
     *  D | 0 | description | yyyy-MM-dd
     *  E | 1 | description | yyyy-MM-dd HHmm | yyyy-MM-dd HHmm
     */
    private Task decodeLine(String line) {
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
