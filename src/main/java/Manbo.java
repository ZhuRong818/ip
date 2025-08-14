import java.util.Scanner;
import java.util.ArrayList;
public class Manbo {
    private static class Task {
        private boolean isDone;
        private String description;

        public Task(String description){
            this.description = description;
            this.isDone = false;
        }
        public void markAsDone(){
            this.isDone = true;
        }

        public void unmarkAsDone(){
            this.isDone = false;
        }

        public String getStatusIcon() {
            return (isDone?"X": " ");
        }
        @Override
        public String toString() {
            return "[" + getStatusIcon() + "] " + description;
        }
    }

    public static void main(String[] args) {
        Scanner in= new Scanner(System.in);
       // Task[] tasks= new Task[100];
        ArrayList<Task> tasks = new ArrayList<>();
        int taskCount= 0;
        String logo = " __  __    _    _   _ ____   ___   \n"
                + "|  \\/  |  / \\  | \\ | | __ ) / _ \\  \n"
                + "| |\\/| | / _ \\ |  \\| |  _ \\| | | | \n"
                + "| |  | |/ ___ \\| |\\  | |_) | |_| | \n"
                + "|_|  |_/_/   \\_\\_| \\_|____/ \\___/  \n";
        System.out.println("____________________________________________________________");
        System.out.println(logo);
        System.out.println(" Hello! I'm Manbo");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");


        while (true) {
            String input= in.nextLine();
            if (input.equals("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            } else if (input.equals("list")) {
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println(" " + (i + 1) + "." + tasks.get(i));// arraylist supports get
                }

            System.out.println("____________________________________________________________");
            } else if (input.startsWith("mark ")) //记得空格
            {
                int index= Integer.parseInt(input.substring(5)) - 1;//convert str to int
                tasks.get(index).markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("   " + tasks.get(index));
            } else if (input.startsWith("unmark ")) {
                int index = Integer.parseInt(input.substring(7)) - 1;
                tasks.get(index).unmarkAsDone();
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + tasks.get(index));
            } else {
                Task t = new Task(input);
                tasks.add(t);
            System.out.println("____________________________________________________________");
            System.out.println(" added: " + input);
            System.out.println("____________________________________________________________");
        }
    }

        in.close();
    }
}
