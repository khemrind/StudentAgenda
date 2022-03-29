package com.example.studentagenda;

import java.util.ArrayList;

public class Category {
    public String name;
    public String color; // javafx.scene.paint.Color or hex
    public ArrayList<Task> tasks;

    public Category() {
        name = "new class";
        color = "BLUE";
        tasks = new ArrayList<>();
        tasks.add(new Task());
    }
}

