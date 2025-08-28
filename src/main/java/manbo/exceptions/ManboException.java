package manbo.exceptions;

/**
 * Base exception class for all Manbo application-specific exceptions.
 * Provides a common parent class for all custom exceptions in the Manbo system.
 */
public class ManboException extends Exception {
    /**
     * Constructs a new ManboException with the specified detail message.
     *
     * @param message the detail message explaining the exception
     */
    public ManboException(String message) {
        super(message);
    }
}