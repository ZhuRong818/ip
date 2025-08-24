import com.sun.source.util.TaskEvent;

import java.io.*;
import java.util.*;

public class Storage {
    private File file;// file defined in io package

    public Storage(String path) {
        this.file = new File(path);
        createNonExistentFile();
    }

    private void createNonExistentFile() {
        try {// directory== /data/
            File directory = file.getParentFile();// to handle the folder doesnot exist initially
            if (directory != null && !directory.exists()) {//need the first as if the dir is working dir parent folder does not exist
                directory.mkdirs(); // create ./data folder
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }

    public void save(List<Task> tasks) {
    //used filewriter initially but gpt suggest to use bufferedwriter to improve performance
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Task task : tasks) {
                writer.write(task.toSaveFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {


            String line;
            while ((reader.readLine()) != null) {

                if (reader.readLine().isEmpty()) continue;

                Task task = TaskParser.parseTask(reader.readLine());
                if (task != null) {
                    tasks.add(task);
                }
            }


        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
        return tasks;
    }
}
