package exceptions;

public class IndexOutOfRangeException extends ManboException {
    public  IndexOutOfRangeException(int i, int size) {
        super("Task number " + i + " is out of range. You only have " + size + " tasks.");
    }
}