package com.example.studentagenda;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.Pane;

public class AddController {

    public static AddController Instance;

    public MenuButton menuButton;
    public Pane formPane;

    @FXML
    private void initialize() {
        if (Instance == null) {
            Instance = this;
        }
        formPane.getChildren().add(Main.LoadFXML("addClassView"));
    }




}
