package com.mobdeve.s17.TaskBuddy.mco1;

import android.view.View;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class task_rv {

    String taskName;
    String priority;
    String status;
    String date;
    String description;
    String imageUrl;
    String taskId;
    String uid;


    public task_rv(String taskName, String priority, String status, String date, String description, String imageUrl, String uid, String taskId){
        this.taskName = taskName;
        this.priority = priority;
        this.status = status;
        this.date = date;
        this.description = description;
        this.imageUrl = imageUrl;
        this.taskId = taskId;
        this.uid = uid;
    }
    public String getTaskId() {
        return taskId;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("taskName", taskName);
        map.put("priority", priority);
        map.put("status", status);
        map.put("date", date);
        map.put("description", description);
        map.put("imageUrl", imageUrl);
        map.put("taskId", taskId);
        map.put("uid",uid);
        return map;
    }
    public String getUid(){return uid;}

    public String getName() {
        return taskName;
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
