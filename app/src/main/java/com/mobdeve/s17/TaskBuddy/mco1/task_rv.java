package com.mobdeve.s17.TaskBuddy.mco1;

import java.util.Date;

public class task_rv {

    String name;
    String priority;
    String status;
    String date;

    public task_rv(String name, String priority, String status, String date){
        this.name = name;
        this.priority = priority;
        this.status = status;
        this.date = date;
    }

    public String getName() {
        return name;
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
