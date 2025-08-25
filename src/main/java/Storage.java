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

    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        try (Scanner s = new Scanner(file)) {
            while (s.hasNextLine()) {
                String line = s.nextLine().trim();
                if (line.isEmpty()) continue;
                Task t = TaskParser.parseTask(line);
                if (t != null) tasks.add(t);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
        return tasks;
    }

    public void save(List<Task> tasks) {
        try (FileWriter fw = new FileWriter(file);
             // chatgpt suggested use bufferedwriter to improve performance
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (Task t : tasks) {
                bw.write(t.toSaveFormat());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

}
