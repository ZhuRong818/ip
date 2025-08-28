package manbo.exceptions;

/**
 * Exception thrown when required whitespace is missing in command input.
 * This exception occurs when commands and their arguments are not properly
 * separated by whitespace as required by the command parser.
 */
public class MissingWhitespaceException extends ManboException {
    /**
     * Constructs a new MissingWhitespaceException with a specific error message.
     *
     * @param message the detail message explaining what whitespace is missing
     */
    public MissingWhitespaceException(String message) {
        super(message);
    }
}