package com.example.studentagenda;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Tag {

    public String name;

    public Tag(String name) {
        this.name = name;
    }

    public Node view() {

        AnchorPane pane = new AnchorPane();

        Button overlayButton = new Button();
        overlayButton.setOpacity(0.25);
        overlayButton.setOnAction(event -> {
            Main.setSelectedTag(this);
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
