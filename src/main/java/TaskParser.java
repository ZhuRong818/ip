
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TaskParser {
    public static Task parseTask(String line) {

        String[] seperate = line.split("\\|");
        for (int i = 0; i < seperate.length; i++) {
            seperate[i] = seperate[i].trim(); // remove spaces with trim
        }

        String type = seperate[0];
        boolean isDone = seperate[1].equals("1");
        try {
        if (type.equals("T")) {// type=="T" check pointer use == for primitive only
            Task t = new Todo(seperate[2]);
            if (isDone) {
                t.markAsDone();
            }
            ;
            return t;
        } else if (type.equals("D")) {
            LocalDate by = LocalDate.parse(seperate[3]); // ISO yyyy-MM-dd
            Deadline d = new Deadline(seperate[2], by, isDone);

            if (isDone) {
                d.markAsDone();
            }

            return d;
        } else if (type.equals("E")) {
            LocalDateTime from = LocalDateTime.parse(seperate[3]);
            LocalDateTime to   = LocalDateTime.parse(seperate[4]);
            Event e = new Event(seperate[2], from, to, isDone);

            if (isDone) {
                e.markAsDone();
            };
            return e;
        } else {
            return null;
        }
    }
    catch (Exception e) {
            return null;
        }
    }
}
