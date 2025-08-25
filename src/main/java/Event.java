import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Event extends Task{
    private LocalDateTime from;
    private LocalDateTime to;
    private static final DateTimeFormatter output = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }
    public Event(String description, LocalDateTime from, LocalDateTime to, boolean isDone) {
        super(description, isDone);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {

        return "[E]" + super.toString() + " (from: " + from.format(output) + " to: " + to.format(output) + ")";
    }
    @Override
    public String toSaveFormat() {
        return "E | " + (ifDone() ? "1" : "0") + " | " + getDescription()+ " | " + from + " | " + to;
    }
}
