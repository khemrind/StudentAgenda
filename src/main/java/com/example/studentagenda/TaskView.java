package com.example.studentagenda;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Pair;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskView {

    public Task model;

    public HBox tagHBox;
    public Label titleLabel;
    public Label timeLabel;
    public Label categoryLabel;
    public FontIcon statusIcon;
    public Rectangle tagRect;
    public Button overlayButton;
    public AnchorPane mainPane;
    public HBox tagBox;

    public void initialize() {

        statusIcon.setIconSize(17);
        overlayButton.setOnAction(event -> publishAsSelected());

        // register to Agenda
        Agenda.registeredTasks.add(this);
    }

    public void assign(Task task) {
        model = task;

        // initial
        titleLabel.setText(model.name.get());
        timeLabel.setText(format(model.time.get()));
        categoryLabel.setText(model.getCategoryName());
        updateStatus(model.status.get());
        tagRect.setFill(Agenda.getCategory(model.getCategoryName()).getColor());

        for (Tag tag: model.tags.get()) {
            tagHBox.getChildren().add(tag.minimal_view());
        }

        // binding
        model.name.addListener((observable, oldv, newv) -> titleLabel.setText(newv));
        model.status.addListener((observable, oldv, newv) -> updateStatus(newv));
        model.time.addListener((observable, oldv, newv) -> timeLabel.setText(format(model.time.get())));
        model.tags.addListener((observable, oldv, newv) -> updateTags(newv));
    }

    private void updateStatus(Task.Status status) {
        if (status == Task.Status.Completed) {
            statusIcon.setIconLiteral("fltfal-checkmark-16");
            statusIcon.setIconColor(Color.web("3a8d36"));
        } else if (status == Task.Status.InProgess) {
            statusIcon.setIconLiteral("fltrmz-timer-16");
            statusIcon.setIconColor(Color.web("545252"));
        } else { // Task.Status.Missed
            statusIcon.setIconLiteral("fltrmz-timer-off-24");
            statusIcon.setIconColor(Color.web("da4f4f"));
        }
    }

    private void publishAsSelected() {
        Main.setSelectedTask(this);
    }

    public void setPassed(boolean state) {
        if (state) {
            mainPane.setOpacity(0.75);
        } else { mainPane.setOpacity(1); }
    }

    private void updateTags(List<Tag> tags) {
        tagBox.getChildren().clear();
        for (Tag tag: tags) {
            tagBox.getChildren().add(tag.view());
        }
    }

    public static void generate(ObservableList<Node> container, ArrayList<Task> tasks) {
        for (Task task: tasks) {
            Pair<TaskView, Node> pair = Main.InstanceFXML("taskView");
            pair.getKey().assign(task);
            container.add(pair.getValue());
        }
    }

    public static HBox separator(String text) {
        HBox output = new HBox();
        output.setAlignment(Pos.CENTER_LEFT);
        output.setSpacing(5);

        Separator indent = new Separator(Orientation.HORIZONTAL);
        indent.setMaxWidth(20);

        Label label = new Label(text);
        HBox.setHgrow(label, Priority.ALWAYS);
        label.setFont(Font.font("System", 11));
        label.setTextFill(Color.web("#363636"));
        Separator line = new Separator(Orientation.HORIZONTAL);
        HBox.setHgrow(line, Priority.ALWAYS);

        output.getChildren().addAll(indent, label, line);
        return output;
    }

    private static String format(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        return time.format(formatter);
    }

}
