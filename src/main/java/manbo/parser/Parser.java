package manbo.parser;

import manbo.command.*;
import manbo.exceptions.*;

public class Parser {

    public static Command parse(String input) throws ManboException {
        if (input == null) throw new UnrecognisedInputException("null");
        String trimmed = input.trim();
        if (trimmed.isEmpty()) throw new UnrecognisedInputException("(empty)");

        String[] parts = trimmed.split("\\s+", 2);
        String keyword = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";

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

            case "event": {
                if (args.isBlank()) throw new EmptyDescriptionException("event");
                String[] seg = args.split("\\s+/from\\s+|\\s+/to\\s+");
                if (seg.length < 3) throw new ManboException("Please specify both /from and /to.");
                String desc = seg[0].trim();
                String from = seg[1].trim(); // yyyy-MM-dd HHmm
                String to   = normalizeTo(seg[2].trim(), from); // allow HHmm shortcut
                return new AddEventCommand(desc, from, to);
            }

            default:
                throw new UnrecognisedInputException(keyword);
        }
    }

    private static int parseIndex(String args, String cmd) throws ManboException {
        if (args == null || args.isBlank()) throw new EmptyDescriptionException(cmd);
        args = args.trim();
        if (!args.matches("\\d+")) throw new InvalidIndexException(cmd);
        return Integer.parseInt(args) - 1;
    }

    // If user typed only HHmm for /to, reuse the date from /from.
    private static String normalizeTo(String to, String from) throws ManboException {
        if (to.matches("\\d{4}")) {
            String datePart = from.substring(0, 10); // yyyy-MM-dd
            return datePart + " " + to;
        }
        return to;
    }
}
