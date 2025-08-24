public class Deadline extends Task{
    private String by;
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }
    //this constructor is for tasks from storage to restore their isdone state
    public Deadline(String description, String by, boolean isDone) {
        super(description, isDone);
        this.by = by;
    }
    @Override
    public String toSaveFormat() {
        return "D | " + (ifDone() ? "1" : "0") + " | " + getDescription() + " | " + by;
    }
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
