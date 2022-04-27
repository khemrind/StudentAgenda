package com.example.studentagenda;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.util.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task extends Model implements Comparable<Task> {

    @Override
    public int compareTo(Task task) {
        return deadline.get().compareTo(task.deadline.get());
    }

    public enum Status {
        Completed,
        InProgess,
        Missed
    }

    // properties
    public SimpleStringProperty name = new SimpleStringProperty(this, "name");
    public SimpleObjectProperty<Status> status = new SimpleObjectProperty<>(this, "status");
    public SimpleObjectProperty<LocalDate> deadline = new SimpleObjectProperty<>(this, "deadline");
    public SimpleObjectProperty<LocalDateTime> time = new SimpleObjectProperty<>(this, "time");
    public List<Tag> base_tags = new ArrayList<>();
    public transient SimpleListProperty<Tag> tags = new SimpleListProperty<>(
        this, "tags", FXCollections.observableList(base_tags));

    public Task(String name) {
        this.name.set(name);
    }

    public String getCategoryName() {
        return Agenda.getCategoryFromTask(this);
    }

    public Node view() {
        Pair<TaskView, Node> pair = Main.InstanceFXML("taskView");
        pair.getKey().assign(this);
        return pair.getValue();
    }

}
