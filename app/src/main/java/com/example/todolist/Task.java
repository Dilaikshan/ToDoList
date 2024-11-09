package com.example.todolist;

public class Task {
    private int id;
    private String title;
    private String details;
    private String lastEdited;

    public Task(int id, String title, String details, String lastEdited) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.lastEdited = lastEdited;
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
}
