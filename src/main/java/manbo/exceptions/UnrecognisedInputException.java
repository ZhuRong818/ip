package manbo.exceptions;

/**
 * Exception thrown when the input command is not recognized by the system.
 * This exception occurs when users enter commands that are not supported
 * by the Manbo application.
 */
public class UnrecognisedInputException extends ManboException {
    /**
     * Constructs a new UnrecognisedInputException with information about
     * the unrecognized input and lists supported commands.
     *
     * @param input the unrecognized input that caused the exception
     */
    public UnrecognisedInputException(String input) {
        super("I don't understand your input \"" + input + "\". " +
                "I only support the following instructions currently:" +
                " todo, deadline, event, list, mark, unmark, delete, bye.");
    }
}