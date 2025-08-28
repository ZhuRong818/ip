package manbo.exceptions;

/**
 * Exception thrown when an invalid or malformed index is provided for task operations.
 * This exception is used for cases where the index format is incorrect (not just out of range),
 * such as non-numeric values or missing indices after task commands.
 */
public class InvalidIndexException extends ManboException {
    /**
     * Constructs a new InvalidIndexException with information about the problematic input.
     *
     * @param input the invalid input that caused the exception
     */
    public InvalidIndexException(String input) {
        super("Please provide a valid task number for \"" + input + "\" ");
    }
}