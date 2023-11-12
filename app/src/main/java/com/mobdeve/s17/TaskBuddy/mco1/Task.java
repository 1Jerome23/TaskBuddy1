package com.mobdeve.s17.TaskBuddy.mco1;

public class Task {
    public String taskName;
    public String description;
    public String date;
    public String status;
    public String priority;
    public String imageUrl;
    public String uid ;


    public Task() {
    }

    public Task(String taskName, String description, String date, String status, String priority, String imageUrl, String uid ) {
        this.taskName = taskName;
        this.description = description;
        this.date = date;
        this.status = status;
        this.priority = priority;
        this.imageUrl = imageUrl;
        this.uid = uid;

    }
    public String uid() {
        return uid;
    }

    public void setUserUid(String userUid) {
        this.uid = userUid;
    }

}
