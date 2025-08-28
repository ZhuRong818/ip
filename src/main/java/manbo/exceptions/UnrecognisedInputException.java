package manbo.exceptions;

public class UnrecognisedInputException extends ManboException {
    public UnrecognisedInputException(String input) {
        super("I don't understand your input \"" + input + "\". " +
                "I only support the following instructions currently:" +
                " todo, deadline, event, list, mark, unmark, delete, bye, find.");
    }
}
