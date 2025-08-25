import java.util.*;
import exceptions.ManboException;
import exceptions.UnrecognisedInputException;
import exceptions.InvalidIndexException;
import exceptions.IndexOutOfRangeException;
import exceptions.MissingWhitespaceException;
import exceptions.EmptyDescriptionException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Manbo {
    private static final Storage storage = new Storage("data/manbo.txt");
    private static final DateTimeFormatter IN_DATE = DateTimeFormatter.ISO_LOCAL_DATE;  // yyyy-MM-dd
    private static final DateTimeFormatter IN_DT   = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"); // yyyy-MM-dd HHmm

    static String logo = " __  __    _    _   _ ____   ___   \n"
            + "|  \\/  |  / \\  | \\ | | __ ) / _ \\  \n"
            + "| |\\/| | / _ \\ |  \\| |  _ \\| | | | \n"
            + "| |  | |/ ___ \\| |\\  | |_) | |_| | \n"
            + "|_|  |_/_/   \\_\\_| \\_|____/ \\___/  \n";// log is AI generated

    private static final Scanner in = new Scanner(System.in);
    private static final List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        printWelcome();
        tasks.addAll(storage.load());


        while (true) {
            try {
                String input = in.nextLine().trim();

                if (input.equals("bye")) {
                    printWithBorder("Bye. Hope to see you again soon!");
                    break;
                }

                if (input.equals("list")) {
                    printList();
                    continue;
                }

                if (input.startsWith("mark")) {
                    checkWhitespaceAfterCommand(input, "mark");//  should be  mark 1 not mark1
                    mark(input);
                } else if (input.startsWith("unmark")) {
                    checkWhitespaceAfterCommand(input, "unmark");
                    unmark(input);
                } else if (input.startsWith("todo")) {
                    checkWhitespaceAfterCommand(input, "todo");
                    addTodo(input);
                } else if (input.startsWith("deadline")) {
                    checkWhitespaceAfterCommand(input, "deadline");
                    addDeadline(input);
                } else if (input.startsWith("event")) {
                    checkWhitespaceAfterCommand(input, "event");
                    addEvent(input);
                } else if (input.startsWith("delete")) {
                    checkWhitespaceAfterCommand(input, "delete");
                    delete(input);
                } else {
                    throw new UnrecognisedInputException(input);// we only support a few inputs currently
                }

            } catch (ManboException e) {
                printWithBorder(e.getMessage());// catch any unexpected error
            }
        }

        in.close();
    }

    private static void checkWhitespaceAfterCommand(String input, String command) throws MissingWhitespaceException {
        if (input.startsWith(command) && !input.startsWith(command + " ")) {
            throw new MissingWhitespaceException(
                    "Please put a space and relevant command after \"" + command + "\".");
        }
    }


    private static void printWelcome() {
        System.out.println(logo);
        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Manbo");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");
    }
    // this method is AI generated
    private static void printWithBorder(String msg) {
        System.out.println("____________________________________________________________");
        System.out.println(" " + msg);
        System.out.println("____________________________________________________________");
    }
    private static void printList() {
        System.out.println("____________________________________________________________");
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        System.out.println("____________________________________________________________");
    }
    // trim remove whitespace after the str input but doesnot affect the whitespace in the middle
    private static int parseIndex(String input, int num) throws ManboException {
        String numberPart = input.substring(num).trim();// use trim to remove white space
        if (!numberPart.matches("\\d+")) {// need to be \\d+ which mean >=1 digit
            throw new InvalidIndexException(input.split(" ")[0]);
        }
        int index = Integer.parseInt(numberPart) - 1;// user input is str need to parse to int
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfRangeException(index + 1, tasks.size());
        }
        return index;
    }

    private static void mark(String input) throws ManboException {
        try {
            if (input.length() < 5) {// brutal force, but cannot think of better solution for now
                throw new EmptyDescriptionException("mark");

            }

            int index = Integer.parseInt(input.substring(5).trim()) - 1;
            if (index < 0 || index >= tasks.size()) {
                throw new IndexOutOfRangeException(index + 1, tasks.size());
            }
            tasks.get(index).markAsDone();
            storage.save(tasks);
            printWithBorder("Nice! I've marked this task as done:\n" + tasks.get(index));
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(input.substring(4).trim());
        }
    }

    private static void unmark(String input) throws ManboException {
        try {
            if (input.length() < 7) {
                throw new EmptyDescriptionException("unmark");

            }

            int index = Integer.parseInt(input.substring(7).trim()) - 1;
            if (index < 0 || index >= tasks.size()) {
                throw new IndexOutOfRangeException(index + 1, tasks.size());
            }
            tasks.get(index).unmarkAsDone();
            storage.save(tasks);
            printWithBorder("OK, I've marked this task as not done yet:\n" + tasks.get(index));
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(input.substring(7).trim());
        }
    }

    private static void addTodo(String input) throws ManboException {
        if (input.length() <= 5 || input.substring(5).trim().isEmpty()) {
            throw new EmptyDescriptionException("todo");

        }
        String detail = input.substring(5).trim();
        Task t = new Todo(detail);
        tasks.add(t);
        storage.save(tasks);
        printWithBorder("Got it. I've added this task:\n" + t + "\n Now you have "
                + tasks.size() + " tasks in the list.");
    }

    private static void addDeadline(String input) throws ManboException {
        if (input.length() <= 9) throw new EmptyDescriptionException("deadline");

        String[] parts = input.substring(9).split(" /by ");
        if (parts.length < 2) throw new ManboException("Please specify the /by date as yyyy-MM-dd.");

        String desc = parts[0].trim();
        String byStr = parts[1].trim();

        try {
            LocalDate by = LocalDate.parse(byStr, IN_DATE);
            Task t = new Deadline(desc, by);
            tasks.add(t);
            storage.save(tasks);
            printWithBorder("Got it. I've added this task:\n   " + t +
                    "\n Now you have " + tasks.size() + " tasks in the list.");
        } catch (DateTimeParseException e) {
            throw new ManboException("Invalid date. Use yyyy-MM-dd (e.g., 2019-12-02).");
        }

    }

    private static void addEvent(String input) throws ManboException {
        if (input.length() <= 6) throw new EmptyDescriptionException("event");

        // expected: event meeting /from 2019-12-02 1400 /to 1600   OR full datetime for both
        String[] parts = input.substring(6).split(" /from | /to ");
        if (parts.length < 3) throw new ManboException("Please specify both /from and /to.");

        String desc = parts[0].trim();
        String fromStr = parts[1].trim();
        String toStr   = parts[2].trim();

        try {
            LocalDateTime from = LocalDateTime.parse(fromStr, IN_DT);

            // If user gave only time for /to, allow smart completion (optional).
            LocalDateTime to;
            if (toStr.matches("\\d{4}")) { // e.g., "1600"
                to = LocalDateTime.parse(fromStr.substring(0, 10) + " " + toStr, IN_DT);
            } else {
                to = LocalDateTime.parse(toStr, IN_DT);
            }

            Task t = new Event(desc, from, to);
            tasks.add(t);
            storage.save(tasks);
            printWithBorder("Got it. I've added this task:\n   " + t +
                    "\n Now you have " + tasks.size() + " tasks in the list.");
        } catch (DateTimeParseException e) {
            throw new ManboException("Invalid date/time. Use yyyy-MM-dd HHmm (e.g., 2019-12-02 1800).");
        }
    }

    private static void delete(String input) throws ManboException {

        try {
            int index = parseIndex(input, 7);
            Task removed = tasks.remove(index);
            storage.save(tasks);
            printWithBorder("Noted. I've removed this task:\n  " + removed +
                    "\n Now you have " + tasks.size() + " tasks in the list.");
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(input.substring(6).trim());
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfRangeException(-1, tasks.size()); // -1 means
        }
    }


}