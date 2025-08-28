package manbo.task;

public abstract class Task {
    // this kind of break info hiding
    private boolean isDone;
    private String description;

    public String getDescription() {
        return description;
    }

    public boolean ifDone() {
        return isDone;
    }


    public Task(String description){
        this.description = description;
        this.isDone = false;
    }
    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
  }
    public abstract String toSaveFormat();// since we wont use task directly
    public void markAsDone(){
        this.isDone = true;
    }

    public void unmarkAsDone(){
        this.isDone = false;
    }

    public String getStatus() {
        return (isDone?"X": " ");
    }
    @Override
    public String toString() {
        return "[" + getStatus() + "] " + description;
    }
}
