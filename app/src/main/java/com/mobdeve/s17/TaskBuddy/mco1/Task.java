package com.mobdeve.s17.TaskBuddy.mco1;

public class Task {
    public String taskName;
    public String description;
    public String date;
    public String status;
    public String priority;

    public Task() {
    }

    public Task(String taskName, String description, String date, String status, String priority) {
        this.taskName = taskName;
        this.description = description;
        this.date = date;
        this.status = status;
        this.priority = priority;


    }
}
