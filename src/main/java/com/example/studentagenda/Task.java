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

    private List<Tag> tagList = new ArrayList<>();

    // properties
    public SimpleStringProperty name = new SimpleStringProperty(this, "name");
    public SimpleObjectProperty<Status> status = new SimpleObjectProperty<>(this, "status");
    public SimpleObjectProperty<Date> deadline = new SimpleObjectProperty<>(this, "deadline");
    public SimpleListProperty<Tag> tags = new SimpleListProperty<>(this, "tags", FXCollections.observableList(tagList));

    public Task() {
        name.set("new item");
        status.set(Status.InProgess);
    }

    public String getCategoryName() {
        return Agenda.getCategoryFromTask(this);
    }

}
