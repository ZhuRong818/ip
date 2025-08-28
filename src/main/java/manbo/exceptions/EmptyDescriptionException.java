package manbo.exceptions;

/**
 * Exception thrown when a task description is empty or missing.
 * This exception occurs when commands like todo, deadline, or event
 * are used without providing the required task description.
 */
public class EmptyDescriptionException extends ManboException {
    /**
     * Constructs a new EmptyDescriptionException with a detailed message.
     *
     * @param command the command type that was missing a description
     */
    public EmptyDescriptionException(String command) {
        super("The description of a " + command + " cannot be empty.");
    }
}