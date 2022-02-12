package com.example.studentagenda;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class SettingsController {

    public VBox mainBox;

    public void initialize() {
        AnchorPane.setBottomAnchor(mainBox, 0d);
        AnchorPane.setTopAnchor(mainBox, 0d);
        AnchorPane.setLeftAnchor(mainBox, 0d);
        AnchorPane.setRightAnchor(mainBox, 0d);
    }
}
