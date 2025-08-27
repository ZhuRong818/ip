package manbo.exceptions;

public class EmptyDescriptionException extends ManboException {
    public EmptyDescriptionException(String command) {
        super("The description of a " + command + " cannot be empty.");
    }
}
