package com.example.studentagenda;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

public class ItemController {

    public Label tagLabel;
    public Label descLabel;
    public Label nameLabel;
    public Rectangle tabRect;

    @FXML
    private void initialize() {

    }

    public void fill(String name, String description, String tag, String color) {
        nameLabel.setText(name);
        descLabel.setText(description);
        tagLabel.setText(tag);
        tabRect.setStyle("-fx-fill: " + color + ";");
    }

}
