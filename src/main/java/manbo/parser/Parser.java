package manbo.parser;

import manbo.command.*;
import manbo.exceptions.*;

/**
 * The {@code Parser} is responsible for interpreting raw user input
 * and converting it into an appropriate {@link Command}.
 *
 * <p>It supports commands such as:
 * <ul>
 *   <li>{@code todo <description>}</li>
 *   <li>{@code deadline <description> /by yyyy-MM-dd}</li>
 *   <li>{@code event <description> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm}</li>
 *   <li>{@code mark <index>}</li>
 *   <li>{@code unmark <index>}</li>
 *   <li>{@code delete <index>}</li>
 *   <li>{@code list}</li>
 *   <li>{@code bye}</li>
 * </ul>
 *
 * <p>Invalid or unrecognized input results in a {@link ManboException}.
 */
public class Parser {

    /**
     * Parses a raw user input string into a corresponding {@link Command}.
     *
     * @param input raw user input (e.g., "deadline return book /by 2025-09-01")
     * @return a {@link Command} object representing the action
     * @throws ManboException if the input is null, empty, or invalid
     */
    public static Command parse(String input) throws ManboException {
        if (input == null) throw new UnrecognisedInputException("null");
        String trimmed = input.trim();
        if (trimmed.isEmpty()) throw new UnrecognisedInputException("(empty)");

        // Split input into command keyword + arguments
        String[] parts = trimmed.split("\\s+", 2);

        assert parts.length >= 1 : "Tokenizer must produce at least one token (keyword)";
        String keyword = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";

        assert !keyword.isBlank() : "Keyword should be non-blank after pre-checks";

        // Match keyword to supported commands
        switch (keyword) {
            case "bye":
                return new ExitCommand();

            case "list":
                return new ListCommand();

            case "mark":
                return new MarkCommand(parseIndex(args, "mark"));

            case "unmark":
                return new UnmarkCommand(parseIndex(args, "unmark"));

            case "delete":
                return new DeleteCommand(parseIndex(args, "delete"));

            case "todo":
                if (args.isBlank()) throw new EmptyDescriptionException("todo");
                return new AddTodoCommand(args);

            case "deadline": {
                if (args.isBlank()) throw new EmptyDescriptionException("deadline");
                String[] seg = args.split("\\s+/by\\s+");
                if (seg.length < 2) throw new ManboException("Please specify /by as yyyy-MM-dd.");
                return new AddDeadlineCommand(seg[0].trim(), seg[1].trim());
            }
            case "find":
                if (args.isBlank()) throw new EmptyDescriptionException("find");
                return new FindCommand(args);

            case "event": {
                if (args.isBlank()) throw new EmptyDescriptionException("event");
                String[] seg = args.split("\\s+/from\\s+|\\s+/to\\s+");
                if (seg.length < 3) throw new ManboException("Please specify both /from and /to.");
                String desc = seg[0].trim();
                String from = seg[1].trim(); // yyyy-MM-dd HHmm
                String to   = normalizeTo(seg[2].trim(), from); // allow HHmm shortcut
                return new AddEventCommand(desc, from, to);
            }
            case "stats":
                return new StatsCommand();

            default:
                throw new UnrecognisedInputException(keyword);
        }
    }

    /**
     * Parses a task index argument from a string.
     *
     * @param args raw argument string (expected to be a positive integer)
     * @param cmd  the command name (for error messages)
     * @return zero-based index into the task list
     * @throws ManboException if the argument is missing, not numeric, or invalid
     */
    private static int parseIndex(String args, String cmd) throws ManboException {
        if (args == null || args.isBlank()) throw new EmptyDescriptionException(cmd);
        args = args.trim();
        if (!args.matches("\\d+")) throw new InvalidIndexException(cmd);
        return Integer.parseInt(args) - 1;
    }

    /**
     * Normalizes the {@code /to} argument for events. If the user only provides
     * a time (HHmm), this method prepends the date from the {@code /from} datetime.
     *
     * @param to   the raw {@code /to} argument (either full datetime or just HHmm)
     * @param from the full {@code /from} datetime (yyyy-MM-dd HHmm)
     * @return a normalized {@code /to} datetime string
     * @throws ManboException if input is malformed
     */
    private static String normalizeTo(String to, String from) throws ManboException {
        if (to.matches("\\d{4}")) {
            String datePart = from.substring(0, 10); // yyyy-MM-dd
            return datePart + " " + to;
        }
        return to;
    }
}
