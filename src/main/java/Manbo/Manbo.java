package Manbo;

import java.util.*;
import Manbo.exceptions.ManboException;
import Manbo.exceptions.UnrecognisedInputException;
import Manbo.exceptions.InvalidIndexException;
import Manbo.exceptions.IndexOutOfRangeException;
import Manbo.exceptions.MissingWhitespaceException;
import Manbo.exceptions.EmptyDescriptionException;
import Manbo.storage.Storage;
import Manbo.task.Deadline;
import Manbo.task.Event;
import Manbo.task.Task;
import Manbo.task.Todo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class Manbo {
    private static final Storage storage = new Storage("data/manbo.txt");
    private static final DateTimeFormatter IN_DATE = DateTimeFormatter.ISO_LOCAL_DATE;  // yyyy-MM-dd
    private static final DateTimeFormatter IN_DT_TIME   = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"); // yyyy-MM-dd HHmm

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
        System.out.println(" Hello! I'm Manbo.Manbo");
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

        String[] parts = input.substring(9).split("\\s+/by\\s+");
        if (parts.length < 2) throw new ManboException("Please specify the /by date as yyyy-MM-dd.");

        String desc = parts[0].trim();
        String byStr = parts[1].trim();

        if (!byStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new ManboException("Invalid date format. Use yyyy-MM-dd (e.g., 2019-12-02).");
        }


        int year = Integer.parseInt(byStr.substring(0, 4));
        int month = Integer.parseInt(byStr.substring(5, 7));
        int day = Integer.parseInt(byStr.substring(8,10));
        if (month < 1 || month > 12) {
            throw new ManboException("Invalid month: " + month + ". Month must be 1-12.");
        }
        if (day < 1 || day > 31) {
            throw new ManboException("Invalid day: " + day + ". Day must be 1-31.");
        }

        try {
            LocalDate by = LocalDate.parse(byStr, IN_DATE);

            Task t = new Deadline(desc, by);
            tasks.add(t);
            storage.save(tasks);
            printWithBorder("Got it. I've added this task:\n   " + t +
                    "\n Now you have " + tasks.size() + " tasks in the list.");
        } catch (DateTimeParseException e) {
            throw new ManboException("Invalid date value: please check the whether the given date exist for the given month.");
        }
    }


    private static void addEvent(String input) throws ManboException {
        if (input.length() <= 6) throw new EmptyDescriptionException("event");

        String[] parts = input.substring(6).split("\\s+/from\\s+|\\s+/to\\s+");
        if (parts.length < 3) throw new ManboException("Please specify both /from and /to.");

        String desc    = parts[0].trim();
        String fromStr = parts[1].trim();
        String toStr   = parts[2].trim();

        if (!fromStr.matches("\\d{4}-\\d{2}-\\d{2}\\s\\d{4}")) {
            throw new ManboException("Invalid /from format. Use yyyy-MM-dd HHmm (e.g., 2019-12-02 0900).");
        }
        // the one after || is for time shortcut
        if (!(toStr.matches("\\d{4}-\\d{2}-\\d{2}\\s\\d{4}") || toStr.matches("\\d{4}"))) {
            throw new ManboException("Invalid /to format. Use yyyy-MM-dd HHmm or HHmm.");
        }

        String fromDate = fromStr.substring(0, 10);
        String fromTime = fromStr.substring(11, 15);
        checkDateRanges(fromDate);
        checkTimeRanges(fromTime);

        String toDate = toStr.matches("\\d{4}") ? fromDate : toStr.substring(0, 10);
        String toTime = toStr.matches("\\d{4}") ? toStr : toStr.substring(11, 15);
        checkDateRanges(toDate);
        checkTimeRanges(toTime);

        try {
            LocalDateTime from = LocalDateTime.parse(fromDate + " " + fromTime, IN_DT_TIME);

            LocalDateTime to = LocalDateTime.parse(toDate + " " + toTime, IN_DT_TIME);

            Task t = new Event(desc, from, to);
            tasks.add(t);
            storage.save(tasks);
            printWithBorder("Got it. I've added this task:\n   " + t +
                    "\n Now you have " + tasks.size() + " tasks in the list.");
        } catch (DateTimeParseException e) {
            // Format was OK; the value itself is impossible (e.g., 2025-02-30 1200)
            throw new ManboException("Invalid date/time value (e.g., Feb 30 or 24:61). Please check the calendar and time range.");
        }
    }

    private static void checkDateRanges(String ymd) throws ManboException {
        int month = Integer.parseInt(ymd.substring(5, 7));
        int day = Integer.parseInt(ymd.substring(8,10));
        if (month < 1 || month > 12) {
            throw new ManboException("Invalid month: " + month + ". Month must be 1-12.");
        }
        if (day < 1 || day > 31) {
            throw new ManboException("Invalid day: " + day + ". Day must be 1-31.");
        }
    }

    private static void checkTimeRanges(String hhmm) throws ManboException {
        if (!hhmm.matches("\\d{4}")) {
            throw new ManboException("Invalid time format. Use HHmm (e.g., 0930).");
        }
        int hour = Integer.parseInt(hhmm.substring(0,2));
        int min= Integer.parseInt(hhmm.substring(2,4));
        if (hour < 0 || hour > 23) {
            throw new ManboException("Invalid hour: " + hour + ". Hour must be 00-23.");
        }
        if (min < 0 || min > 59) {
            throw new ManboException("Invalid minute: " + min + ". Minute must be 00-59.");
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