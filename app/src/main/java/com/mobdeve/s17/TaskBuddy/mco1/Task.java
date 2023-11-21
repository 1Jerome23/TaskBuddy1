package com.mobdeve.s17.TaskBuddy.mco1;

public class Task {
    public String taskName;
    public String description;
    public String date;
    public String status;
    public String priority;
    public String imageUrl;
    public String uid ;
    private String taskId;

    private String userUid;

    public Task() {
    }

    public Task(String taskName, String description, String date, String priority, String status, String imageUrl, String uid, String taskId ) {
        this.taskName = taskName;
        this.description = description;
        this.date = date;
        this.status = status;
        this.priority = priority;
        this.imageUrl = imageUrl;
        this.uid = uid;
        this.taskId = taskId;

    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
    public String getTaskId() {
        return taskId;
    }


}
