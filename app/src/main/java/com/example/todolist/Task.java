package com.example.todolist;

public class Task {
    private int id;
    private String title;
    private String details;
    private String lastEdited;
    private String priority;

    public Task(int id, String title, String details, String lastEdited, String priority) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.lastEdited = lastEdited;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public String getLastEdited() {
        return lastEdited;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
