package com.example.studentagenda;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task extends Model {

    public enum Status {
        Completed,
        InProgess,
        Missed
    }

    // properties
    public SimpleStringProperty name = new SimpleStringProperty(this, "name");
    public SimpleObjectProperty<Status> status = new SimpleObjectProperty<>(this, "status");
    public SimpleObjectProperty<Date> deadline = new SimpleObjectProperty<>(this, "deadline");
    public List<Tag> base_tags = new ArrayList<>();
    public transient SimpleListProperty<Tag> tags = new SimpleListProperty<>(
        this, "tags", FXCollections.observableList(base_tags));

    public Task(String name) {
        this.name.set("new item");
    }

    public String getCategoryName() {
        return Agenda.getCategoryFromTask(this);
    }

}
