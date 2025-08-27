import java.util.List;
import java.util.Scanner;

/**
 * Ui handles all user interaction: printing messages and reading input.
 */
public class UI {
    private final Scanner in = new Scanner(System.in);
    private static final String Line = "____________________________________________________________";


    public void showWelcome(String logo) {
        if (logo != null && !logo.isEmpty()) {
            System.out.println(logo);
        }
        showLine();
        System.out.println(" Hello! I'm Duke");
        System.out.println(" What can I do for you?");
        showLine();
    }

    public void showLine() {
        System.out.println(Line);
    }


    public String readCommand() {
        return in.nextLine();
    }

    public void showMessage(String msg) {
        showLine();
        System.out.println(" " + msg);
        showLine();
    }

    /** Generic error message (bordered). */
    public void showError(String message) {
        showLine();
        System.out.println(" " + message);
        showLine();
    }

    /** Called when loading from storage fails (as in the example). */
    public void showLoadingError() {
        showError("Error loading saved tasks. Starting with an empty list.");
    }

    public void showGoodbye() {
        showLine();
        System.out.println(" Bye. Hope to see you again soon!");
        showLine();
    }

    public void showList(List<Task> tasks) {
        showLine();
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        showLine();
    }


    public void showAdded(Task t, int total) {
        showLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + total + " tasks in the list.");
        showLine();
    }

    /** Confirms a removed task. */
    public void showRemoved(Task t, int total) {
        showLine();
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + total + " tasks in the list.");
        showLine();
    }

    /** Confirms a mark/unmark action. */
    public void showMarked(Task t, boolean done) {
        showLine();
        if (done) {
            System.out.println(" Nice! I've marked this task as done:");
        } else {
            System.out.println(" OK, I've marked this task as not done yet:");
        }
        showLine();
    }
}
