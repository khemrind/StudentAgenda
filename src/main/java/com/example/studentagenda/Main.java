package com.example.studentagenda;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class Main {

    public static Main Instance;

    public Button addButton;
    public Button listButton;
    public Button settingsButton;
    public Label dateLabel;
    public Pane contentPane;

    public void initialize() {
        if (Instance == null) {
            Instance = this;
        }
        contentPane.getChildren().add(LoadFXML("listView"));
    }

    public static javafx.scene.Node LoadFXML(String viewname) {
        try {
            return new FXMLLoader(Instance.getClass()
                    .getResource(viewname + ".fxml")).load();
        } catch (Exception ex) {
            System.out.println(ex);
        } return null;
    }

    public void addButton_Clicked(ActionEvent actionEvent) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(LoadFXML("addView"));
    }

    public void listButton_Clicked(ActionEvent actionEvent) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(LoadFXML("listView"));
    }

    public void settingsButton_Clicked(ActionEvent actionEvent) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(LoadFXML("settingsView"));
    }


}