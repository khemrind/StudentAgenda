package com.example.studentagenda;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

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

    public Node view() {

        AnchorPane pane = new AnchorPane();
        Button overlayButton = new Button();
        overlayButton.setOpacity(0.25);
        overlayButton.setOnAction(event -> {
            Main.setSelectedCategory(this);
        });

        Label label = new Label();
        label.setText(name);
        label.setPadding(new Insets(0,4,0,4));
        label.setTextFill(Color.web("#4d4d4d"));
        label.setFont(Font.font("System", FontWeight.NORMAL, FontPosture.ITALIC, 12));
        label.setStyle("-fx-background-radius: 1; -fx-background-color: #d4d4d4;");

        pane.getChildren().add(label);
        pane.getChildren().add(overlayButton);

        AnchorPane.setLeftAnchor(label, 0d);
        AnchorPane.setRightAnchor(label, 0d);
        AnchorPane.setTopAnchor(label, 0d);
        AnchorPane.setBottomAnchor(label, 0d);

        AnchorPane.setLeftAnchor(overlayButton, 0d);
        AnchorPane.setRightAnchor(overlayButton, 0d);
        AnchorPane.setTopAnchor(overlayButton, 0d);
        AnchorPane.setBottomAnchor(overlayButton, 0d);

        return pane;

    }
}

