import java.util.Scanner;
public class Manbo {
    public static void main(String[] args) {
        String logo = " __  __    _    _   _ ____   ___   \n"
                + "|  \\/  |  / \\  | \\ | | __ ) / _ \\  \n"
                + "| |\\/| | / _ \\ |  \\| |  _ \\| | | | \n"
                + "| |  | |/ ___ \\| |\\  | |_) | |_| | \n"
                + "|_|  |_/_/   \\_\\_| \\_|____/ \\___/  \n";
        Scanner in = new Scanner(System.in);
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
            } else {
                System.out.println("____________________________________________________________");
                System.out.println(" " + input);
                System.out.println("____________________________________________________________");
            }
        }

        in.close();

    }
}
