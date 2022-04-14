package com.example.studentagenda;

import java.util.ArrayList;

public class Category extends Model {
    public String name;
    public String color; // javafx.scene.paint.Color or hex
    public ArrayList<Task> tasks;

    public Category(String name) {
        this.name = name;
        color = "BLUE";
        tasks = new ArrayList<>();
        tasks.add(new Task());
    }
}

