package com.mobdeve.s17.TaskBuddy.mco1;

import java.util.Date;

public class task_rv {

    String name;
    String priority;
    String status;
    String date;
    String description;
    String imageUrl;

    public task_rv(String name, String priority, String status, String date, String description, String imageUrl){
        this.name = name;
        this.priority = priority;
        this.status = status;
        this.date = date;
        this.description = description;
        this.imageUrl = imageUrl;

    }

    public String getName() {
        return name;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Add getters for description and imageUrl if needed
    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }
}
