package manbo.exceptions;

public class IndexOutOfRangeException extends ManboException {
    public  IndexOutOfRangeException(int i, int size) {
        super("Manbo.task.Task number " + i + " is out of range. You only have " + size + " tasks.");
    }
}