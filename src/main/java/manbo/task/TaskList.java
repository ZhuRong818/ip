package manbo.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> existing) {
        this.tasks = new ArrayList<>(existing);
    }

    public int size() { return tasks.size(); }

    public Task get(int index0) { return tasks.get(index0); }

    public void add(Task t) { tasks.add(t); }

    public Task remove(int index0) {
        return tasks.remove(index0);
    }


    public void addAll(List<Task> more) {
        tasks.addAll(more);
    }

    public List<Task> all() {
        return Collections.unmodifiableList(tasks);
    }
}
