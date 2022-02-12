package com.example.studentagenda;

import java.util.Date;

public class Item {
    public String name;
    public String description;
    public Date time;

    public Item() {
        name = "new item";
        description = "new description";
        time = new Date();
    }
}
