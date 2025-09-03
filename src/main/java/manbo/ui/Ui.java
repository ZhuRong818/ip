package manbo.ui;

import java.util.List;
import java.util.Scanner;
import manbo.task.Task;

public class Ui {
    private final Scanner in = new Scanner(System.in);
    private final StringBuilder buf = new StringBuilder();   // <-- NEW

    private static final String LINE = "____________________________________________________________";
    private final String logo =
            " __  __    _    _   _ ____   ___   \n" +
                    "|  \\/  |  / \\  | \\ | | __ ) / _ \\  \n" +
                    "| |\\/| | / _ \\ |  \\| |  _ \\| | | | \n" +
                    "| |  | |/ ___ \\| |\\  | |_) | |_| | \n" +
                    "|_|  |_/_/   \\_\\_| \\_|____/ \\___/  \n";

    // --- Buffer helpers (NEW) ---
    public void reset() { buf.setLength(0); }
    public String out() { return buf.toString(); }
    private void p(String s) { System.out.println(s); buf.append(s).append('\n'); }
    // -----------------------------

    public void showWelcome() {
        p(logo);
        showLine();
        p(" Hello! I'm Manbo");
        p(" What can I do for you?");
        showLine();
    }

    public String readCommand() { return in.nextLine(); }
    public void showLine() { p(LINE); }

    public void showError(String msg) {
        showLine();
        p(" " + msg);
        showLine();
    }

    public void info(String msg) {
        showLine();
        p(" " + msg);
        showLine();
    }

    public void showList(List<Task> tasks) {
        showLine();
        p(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            p(" " + (i + 1) + "." + tasks.get(i));
        }
        showLine();
    }

    public void showMatches(List<Task> matches) {
        showLine();
        if (matches.isEmpty()) {
            p(" No matching tasks found.");
            showLine();
            return;
        }
        p(" Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            p(" " + (i + 1) + "." + matches.get(i));
        }
        showLine();
    }

    public void sayBye() { p(" Bye. Hope to see you again soon!"); }
}
