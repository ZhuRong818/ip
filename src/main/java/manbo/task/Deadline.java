package manbo.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class Deadline extends Task {
    private final LocalDate by;
    private static final DateTimeFormatter Output = DateTimeFormatter.ofPattern("MMM d yyyy");
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }
    //this constructor is for tasks from storage to restore their isdone state
    public Deadline(String description, LocalDate by, boolean isDone) {
        super(description, isDone);
        this.by = by;
    }
    @Override
    public String toSaveFormat() {
        return "D | " + (ifDone() ? "1" : "0") + " | " + getDescription() + " | " + by;
    }
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(Output) + ")";
    }
}
