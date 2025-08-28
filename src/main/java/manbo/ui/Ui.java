package manbo.ui;

import java.util.List;
import java.util.Scanner;
import manbo.task.Task;

public class Ui {
    private final Scanner in = new Scanner(System.in);
    private final String logo =
            " __  __    _    _   _ ____   ___   \n" +
                    "|  \\/  |  / \\  | \\ | | __ ) / _ \\  \n" +
                    "| |\\/| | / _ \\ |  \\| |  _ \\| | | | \n" +
                    "| |  | |/ ___ \\| |\\  | |_) | |_| | \n" +
                    "|_|  |_/_/   \\_\\_| \\_|____/ \\___/  \n";

    public void showWelcome() {
        System.out.println(logo);
        showLine();
        System.out.println(" Hello! I'm Manbo.Manbo");
        System.out.println(" What can I do for you?");
        showLine();
    }

    public String readCommand() { return in.nextLine(); }
    public void showLine() { System.out.println("____________________________________________________________"); }

    public void showError(String msg) {
        showLine();
        System.out.println(" " + msg);
    }

    public void info(String msg) {
        showLine();
        System.out.println(" " + msg);
    }

    public void showList(List<Task> tasks) {
        showLine();
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        showLine();
    }

    // in manbo.ui.Ui
    public void showMatches(List<Task> matches) {
        showLine();
        if (matches.isEmpty()) {
            System.out.println(" No matching tasks found.");
            showLine();
            return;
        }
        System.out.println(" Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println(" " + (i + 1) + "." + matches.get(i));
        }
        showLine();
    }


    public void sayBye() {
        System.out.println(" Bye. Hope to see you again soon!");
    }
}
