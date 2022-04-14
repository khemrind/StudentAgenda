package com.example.studentagenda;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Tag {

    public String name;

    public Tag(String name) {
        this.name = name;
    }

    public Label view() {
        Label output = new Label();
        output.setText(name);
        output.setTextFill(Color.web("#4d4d4d"));
        output.setFont(Font.font("System", FontWeight.NORMAL, FontPosture.ITALIC, 10));
        output.setStyle("-fx-background-radius: 1; -fx-background-color: #d4d4d4;");
        return output;
    }
}
