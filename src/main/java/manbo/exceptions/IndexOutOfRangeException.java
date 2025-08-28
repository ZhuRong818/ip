package manbo.exceptions;

/**
 * Exception thrown when a task index is outside the valid range of tasks.
 * This exception occurs when trying to access a task that doesn't exist
 * in the current task list.
 */
public class IndexOutOfRangeException extends ManboException {
    /**
     * Constructs a new IndexOutOfRangeException with specific index information.
     *
     * @param i the invalid index that was attempted to be accessed
     * @param size the current number of tasks in the list
     */
    public IndexOutOfRangeException(int i, int size) {
        super("Manbo.task.Task number " + i + " is out of range. You only have " + size + " tasks.");
    }
}