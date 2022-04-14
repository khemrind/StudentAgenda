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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Pair;
import org.kordamp.ikonli.javafx.FontIcon;

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

    public void initialize() {
        statusIcon.setIconSize(17);
    }

    public void assign(Task task) {
        model = task;

        // initial
        titleLabel.setText(model.name.get());
        timeLabel.setText("12:00 AM");
        categoryLabel.setText(model.getCategoryName());
        tagRect.setFill(Color.BLUEVIOLET);
        for (Tag tag: model.tags.get()) {
            tagHBox.getChildren().add(tag.view());
        }

        // binding
        model.name.addListener((observable, oldv, newv) -> titleLabel.setText(newv));
        model.status.addListener((observable, oldv, newv) -> updateStatus(newv));
        model.deadline.addListener((observable, oldv, newv) -> updateDeadline(newv));
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

    private void updateDeadline(Date deadline) {
        // implement
    }

    private void updateTags(List<Tag> tags) {
        // implement
        // edit fxml to make the box just one box; exclude title label
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

}
