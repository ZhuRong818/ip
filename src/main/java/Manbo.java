import java.util.*;

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
            String input = in.nextLine();
            if (input.equals("bye")) {
                printWithBorder("Bye. Hope to see you again soon!");
                break;
            } else if (input.equals("list")) {
                printList();
            } else if (input.startsWith("mark ")) {
                markTask(input);
            } else if (input.startsWith("unmark ")) {
                unmarkTask(input);
            } else if (input.startsWith("todo ")) {
                addTodo(input);
            } else if (input.startsWith("deadline ")) {
                addDeadline(input);
            } else if (input.startsWith("event ")) {
                addEvent(input);
            } else {
                printWithBorder("Unknown command.");
            }
        }

        in.close();
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
        for (int i =0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        System.out.println("____________________________________________________________");
    }

    private static void markTask(String input) {
        int index = Integer.parseInt(input.substring(5)) - 1;
        tasks.get(index).markAsDone();
        printWithBorder("Nice! I've marked this task as done:\n" + tasks.get(index));
    }

    private static void unmarkTask(String input) {
        int index = Integer.parseInt(input.substring(7))- 1;
        tasks.get(index).unmarkAsDone();
        printWithBorder("OK, I've marked this task as not done yet:\n"+tasks.get(index));
    }

    private static void addTodo(String input) {
        String description = input.substring(5);
        Task t = new Todo(description);
        tasks.add(t);
        printWithBorder("Got it. I've added this task:\n" + t + "\n Now you have " + tasks.size() + " tasks in the list.");
    }

    private static void addDeadline(String input) {
        String[] parts = input.substring(9).split(" /by ");// first part before by second after by
        Task t = new Deadline(parts[0], parts[1]);
        tasks.add(t);
        printWithBorder("Got it. I've added this task:\n   " + t + "\n Now you have " + tasks.size() + " tasks in the list.");
    }

    private static void addEvent(String input) {
        String[] parts = input.substring(6).split(" /from | /to ");
        Task t = new Event(parts[0], parts[1], parts[2]);
        tasks.add(t);
        printWithBorder("Got it. I've added this task:\n   " + t + "\n Now you have " + tasks.size() + " tasks in the list.");
    }
}
