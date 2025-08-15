import java.util.*;
import exceptions.ManboException;
import exceptions.UnrecognisedInputException;
import exceptions.InvalidIndexException;
import exceptions.IndexOutOfRangeException;
import exceptions.MissingWhitespaceException;
import exceptions.EmptyDescriptionException;


public class Manbo {
    static String logo = " __  __    _    _   _ ____   ___   \n"
            + "|  \\/  |  / \\  | \\ | | __ ) / _ \\  \n"
            + "| |\\/| | / _ \\ |  \\| |  _ \\| | | | \n"
            + "| |  | |/ ___ \\| |\\  | |_) | |_| | \n"
            + "|_|  |_/_/   \\_\\_| \\_|____/ \\___/  \n";

    private static final Scanner in = new Scanner(System.in);
    private static final List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        printWelcome();

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
                    markTask(input);
                } else if (input.startsWith("unmark")) {
                    checkWhitespaceAfterCommand(input, "unmark");
                    unmarkTask(input);
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
                    deleteTask(input);
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

    private static void markTask(String input) throws ManboException {
        try {
            if (input.length() < 5) {// brutal force, but cannot think of better solution for now
                throw new EmptyDescriptionException("mark");

            }

            int index = Integer.parseInt(input.substring(5).trim()) - 1;
            if (index < 0 || index >= tasks.size()) {
                throw new IndexOutOfRangeException(index + 1, tasks.size());
            }
            tasks.get(index).markAsDone();
            printWithBorder("Nice! I've marked this task as done:\n" + tasks.get(index));
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(input.substring(4).trim());
        }
    }

    private static void unmarkTask(String input) throws ManboException {
        try {
            if (input.length() < 7) {
                throw new EmptyDescriptionException("unmark");

            }

            int index = Integer.parseInt(input.substring(7).trim()) - 1;
            if (index < 0 || index >= tasks.size()) {
                throw new IndexOutOfRangeException(index + 1, tasks.size());
            }
            tasks.get(index).unmarkAsDone();
            printWithBorder("OK, I've marked this task as not done yet:\n" + tasks.get(index));
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(input.substring(7).trim());
        }
    }

    private static void addTodo(String input) throws ManboException {
        if (input.length() <= 5 || input.substring(5).trim().isEmpty()) {
            throw new EmptyDescriptionException("todo");

        }
        String description = input.substring(5).trim();
        Task t = new Todo(description);
        tasks.add(t);
        printWithBorder("Got it. I've added this task:\n" + t + "\n Now you have "
                + tasks.size() + " tasks in the list.");
    }

    private static void addDeadline(String input) throws ManboException {
        if (input.length() <= 9) {
            throw new EmptyDescriptionException("addDeadline");

        }
        String[] seperate = input.substring(9).split(" /by ");
        if (seperate.length < 2) {
            throw new ManboException("Please specify by date for deadlines.");
        }//without this input like deadline 1 will break
        Task t = new Deadline(seperate[0].trim(), seperate[1].trim());
        tasks.add(t);
        printWithBorder("Got it. I've added this task:\n   " + t + "\n Now you have "
                + tasks.size() + " tasks in the list.");
    }

    private static void addEvent(String input) throws ManboException {
        if (input.length() <= 6) {
            throw new EmptyDescriptionException("addEvent");

        }
        String[] seperate = input.substring(6).split(" /from | /to ");
        if (seperate.length < 3) {
            throw new ManboException("Please specify from and to for events.");
        }
        Task t = new Event(seperate[0].trim(), seperate[1].trim(), seperate[2].trim());
        tasks.add(t);
        printWithBorder("Got it. I've added this task:\n   " + t + "\n Now you have "
                + tasks.size() + " tasks in the list.");
    }

    private static void deleteTask(String input) throws ManboException {

        try {
            int index = parseIndex(input, 7);
            Task removed = tasks.remove(index);
            printWithBorder("Noted. I've removed this task:\n  " + removed +
                    "\n Now you have " + tasks.size() + " tasks in the list.");
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(input.substring(6).trim());
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfRangeException(-1, tasks.size()); // -1 means invalid internal index
        }
    }


}