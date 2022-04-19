package com.example.studentagenda;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Category extends Model {

    public String name;
    public String color; // javafx.scene.paint.Color (BLUE, RED) or hex (4d4d4d, ffffff)
    public ArrayList<Task> base_tasks = new ArrayList<>();


//tasks is the same as base_tasks but with notification to the user, rather than just accepting the data
    public transient SimpleListProperty<Task> tasks = new SimpleListProperty<>(
            this, "tasks", FXCollections.observableList(base_tasks));

    public Category(String name, String color) {
        this.name = name;
        this.color = color;
//addListner - means to notify closure (function) of any list changes to update UI, void

        tasks.get().addListener((ListChangeListener<? super Task>) event -> {
            Agenda.notifyTasksChanged();
        });

    }

    public Color getColor() { return Color.web(color); }

}

