package exceptions;
// this is used for wrong index after the task type (not out of range), such as mark two
public class InvalidIndexException extends ManboException {
    public InvalidIndexException(String input) {
        super("Please provide a valid task number for \"" + input+ "\" ");
    }
}