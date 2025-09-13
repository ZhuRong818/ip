package manbo.parser;

import manbo.command.*;
import manbo.exceptions.*;

/**
 * The {@code Parser} class is responsible for interpreting raw user input
 * strings and converting them into appropriate {@link Command} objects.
 *
 * <p>This is the first step of command execution in Manbo:
 * <ol>
 *   <li>User input (e.g., "deadline return book /by 2025-09-01") is passed to {@link #parse(String)}.</li>
 *   <li>{@code Parser} identifies the command keyword and delegates to a
 *       dedicated handler for validation and argument parsing.</li>
 *   <li>The corresponding {@code Command} object is returned to the caller
 *       (typically the main program loop), ready for execution.</li>
 * </ol>
 *
 * <p>Design notes:
 * <ul>
 *   <li>Strong SLAP (Single Level of Abstraction Principle): {@link #parse(String)} only dispatches,
 *       detailed parsing happens in per-command handlers.</li>
 *   <li>Guard clauses are used for clarity instead of deep nesting.</li>
 *   <li>Magic strings (regex patterns) are centralized as constants.</li>
 *   <li>Exceptions are thrown for invalid input to keep parsing robust.</li>
 * </ul>
 */
public final class Parser {

    // ===== Constants for splitting input =====
    private static final String SPLIT_ON_SPACES_ONCE = "\\s+"; // split keyword + args
    private static final String DEADLINE_SPLIT = "\\s+/by\\s+";
    private static final String EVENT_SPLIT = "\\s+/from\\s+|\\s+/to\\s+";
    private static final String HHMM_ONLY = "\\d{4}"; // e.g., "1830" = 6:30 PM

    private Parser() { } // Parser is utility class, not instantiable

    /**
     * Parses raw user input into a {@link Command}.
     *
     * <p>Supported keywords include {@code todo}, {@code deadline}, {@code event},
     * {@code mark}, {@code unmark}, {@code delete}, {@code list}, {@code bye}, {@code find}, and {@code stats}.
     *
     * @param input raw user input (must not be null or blank)
     * @return a concrete {@link Command} ready for execution
     * @throws ManboException if input is null, empty, or invalid
     */
    public static Command parse(String input) throws ManboException {
        if (input == null) throw new UnrecognisedInputException("null");
        final String trimmed = input.trim();
        if (trimmed.isEmpty()) throw new UnrecognisedInputException("(empty)");

        final String[] parts = trimmed.split(SPLIT_ON_SPACES_ONCE, 2);
        assert parts.length >= 1 : "Tokenizer must produce at least one token (keyword)";

        final String keyword = parts[0].toLowerCase();
        final String args = parts.length > 1 ? parts[1] : "";
        assert !keyword.isBlank() : "Keyword should be non-blank after pre-checks";

        // Dispatch based on command keyword
        switch (keyword) {
            case "bye":     return new ExitCommand();
            case "list":    return new ListCommand();
            case "stats":   return new StatsCommand();

            case "mark":    return new MarkCommand(parseIndex(args, "mark"));
            case "unmark":  return new UnmarkCommand(parseIndex(args, "unmark"));
            case "delete":  return new DeleteCommand(parseIndex(args, "delete"));

            case "todo":    return handleTodo(args);
            case "find":    return handleFind(args);
            case "deadline":return handleDeadline(args);
            case "event":   return handleEvent(args);

            default:        throw new UnrecognisedInputException(keyword);
        }
    }

    // ===== Command handlers =====

    /**
     * Parses a {@code todo} command.
     * @param args the description after "todo"
     * @return a {@link AddTodoCommand}
     * @throws ManboException if description is missing
     */
    private static Command handleTodo(String args) throws ManboException {
        if (args.isBlank()) throw new EmptyDescriptionException("todo");
        return new AddTodoCommand(args);
    }

    /**
     * Parses a {@code find} command.
     * @param args the search keyword
     * @return a {@link FindCommand}
     * @throws ManboException if keyword is missing
     */
    private static Command handleFind(String args) throws ManboException {
        if (args.isBlank()) throw new EmptyDescriptionException("find");
        return new FindCommand(args);
    }

    /**
     * Parses a {@code deadline} command.
     * Example: {@code deadline return book /by 2025-09-01}
     *
     * @param args description and deadline string
     * @return an {@link AddDeadlineCommand}
     * @throws ManboException if syntax is invalid
     */
    private static Command handleDeadline(String args) throws ManboException {
        if (args.isBlank()) throw new EmptyDescriptionException("deadline");
        final String[] seg = args.split(DEADLINE_SPLIT);
        if (seg.length < 2) throw new ManboException("Please specify /by as yyyy-MM-dd.");
        final String desc = seg[0].trim();
        final String by   = seg[1].trim();
        if (desc.isEmpty()) throw new EmptyDescriptionException("deadline");
        return new AddDeadlineCommand(desc, by);
    }

    /**
     * Parses an {@code event} command.
     * Example: {@code event workshop /from 2025-09-01 1800 /to 2100}
     *
     * @param args description and time ranges
     * @return an {@link AddEventCommand}
     * @throws ManboException if syntax is invalid
     */
    private static Command handleEvent(String args) throws ManboException {
        if (args.isBlank()) throw new EmptyDescriptionException("event");
        final String[] seg = args.split(EVENT_SPLIT);
        if (seg.length < 3) throw new ManboException("Please specify both /from and /to.");
        final String desc = seg[0].trim();
        final String from = seg[1].trim();
        final String to   = normalizeTo(seg[2].trim(), from);
        if (desc.isEmpty()) throw new EmptyDescriptionException("event");
        return new AddEventCommand(desc, from, to);
    }

    // ===== Utilities =====

    /**
     * Parses an integer task index (1-based in user input).
     * Converts to zero-based index for internal use.
     *
     * @param args string containing a positive integer
     * @param cmd command name for contextual error reporting
     * @return zero-based task index
     * @throws ManboException if missing or not a valid integer
     */
    private static int parseIndex(String args, String cmd) throws ManboException {
        if (args == null || args.isBlank()) throw new EmptyDescriptionException(cmd);
        final String s = args.trim();
        if (!s.matches("\\d+")) throw new InvalidIndexException(cmd);
        return Integer.parseInt(s) - 1; // convert 1-based → 0-based
    }

    /**
     * Normalizes the {@code /to} field in {@code event} commands.
     * If the user only provides a time (HHmm), the date from {@code /from} is prepended.
     *
     * @param to raw {@code /to} argument (either full datetime or HHmm only)
     * @param from full {@code /from} datetime (yyyy-MM-dd HHmm)
     * @return normalized datetime string for {@code /to}
     * @throws ManboException if {@code /from} is malformed
     */
    private static String normalizeTo(String to, String from) throws ManboException {
        if (to.matches(HHMM_ONLY)) {
            if (from.length() < 10) throw new ManboException("Invalid /from date.");
            final String datePart = from.substring(0, 10); // yyyy-MM-dd
            return datePart + " " + to;
        }
        return to;
    }
}
