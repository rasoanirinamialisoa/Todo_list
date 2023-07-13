package TodoList;

import java.sql.Timestamp;

public class Task {
    private int id;
    private String title;
    private String description;
    private Timestamp deadline;
    private int priority;
    private boolean done;

    public Task (int id, String title, String description, Timestamp deadline, int priority, boolean done) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String toString() {
        return "id : " + this.id + ", title : " + this.title + ", description : " + this.description + ",  deadline : " + this.deadline + ",  priority : " + this.priority + ", done : " + this.done;
    }
}
