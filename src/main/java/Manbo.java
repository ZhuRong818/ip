import java.util.Scanner;
public class Manbo {
    public static void main(String[] args) {
        Scanner in= new Scanner(System.in);
        String[] tasks= new String[100];
        int taskCount= 0;
        String logo = " __  __    _    _   _ ____   ___   \n"
                + "|  \\/  |  / \\  | \\ | | __ ) / _ \\  \n"
                + "| |\\/| | / _ \\ |  \\| |  _ \\| | | | \n"
                + "| |  | |/ ___ \\| |\\  | |_) | |_| | \n"
                + "|_|  |_/_/   \\_\\_| \\_|____/ \\___/  \n";
        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Manbo");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");


        while (true) {
            String input = in.nextLine();
            if (input.equals("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            } else if (input.equals("list")) {
            System.out.println("____________________________________________________________");
            for (int i= 0; i<taskCount; i++) {
                System.out.println(" " + (i + 1) + ". " + tasks[i]);
            }
            System.out.println("____________________________________________________________");
        } else {
            tasks[taskCount] = input;
            taskCount++;
            System.out.println("____________________________________________________________");
            System.out.println(" added: " + input);
            System.out.println("____________________________________________________________");
        }
    }

        in.close();
    }
}
