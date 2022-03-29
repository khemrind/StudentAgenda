package com.example.studentagenda;

import java.util.Date;

public class Task {
    public String name;
    public String description;
    public Date time;

    public Task() {
        name = "new item";
        description = "new description";
        time = new Date();
    }
}
