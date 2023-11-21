package com.mobdeve.s17.TaskBuddy.mco1;

import android.view.View;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class task_rv {

    String name;
    String priority;
    String status;
    String date;
    String description;
    String imageUrl;
    String taskId;


    public task_rv(String name, String priority, String status, String date, String description, String imageUrl, String taskId){
        this.name = name;
        this.priority = priority;
        this.status = status;
        this.date = date;
        this.description = description;
        this.imageUrl = imageUrl;
        this.taskId = taskId;

    }
    public String getTaskId() {
        return taskId;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("priority", priority);
        map.put("status", status);
        map.put("dueDate", date);
        map.put("details", description);
        map.put("imageURL", imageUrl);
        return map;
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

    public void setOnClickListener(View.OnClickListener onClickListener) {
    }
}
