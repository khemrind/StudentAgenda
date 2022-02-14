package com.example.studentagenda;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

public class Main {

    public static Main Instance;

    public Button addButton;
    public Button listButton;
    public Button settingsButton;
    public Label dateLabel;
    public Pane contentPane;

    public void initialize() {
        Data.initialize();
        if (Instance == null) {
            Instance = this;
        }
        contentPane.getChildren().clear();
        contentPane.getChildren().add(CacheFXML("listView"));
        Platform.runLater(() -> contentPane.requestFocus());

    }

    public static Node CacheFXML(String viewname) {
        if (Data.cache.get(viewname) == null) {
            try {
                Node node = new FXMLLoader().load(Instance.getClass()
                        .getResource(viewname + ".fxml").openStream());
                Data.cache.put(viewname, node);
                return node;
            } catch (Exception ex) {
                System.out.println(ex);
                return null;
            }
        } else {
            return Data.cache.get(viewname);
        }
    }

    public static <Type> Pair<Type, Node> InstanceFXML(String viewname) {
        try {
            FXMLLoader loader = new FXMLLoader();
            Node node = loader.load(Instance.getClass()
                    .getResource(viewname + ".fxml").openStream());
            return new Pair<>(loader.getController(), node);
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }

    public static void RunAsync(Action action) {
        Thread thread = new Thread(action::run);
        thread.start();
    }

    public void addButton_Clicked(ActionEvent actionEvent) {
        contentPane.requestFocus();
        contentPane.getChildren().clear();
        contentPane.getChildren().add(CacheFXML("addView"));
    }

    public void listButton_Clicked(ActionEvent actionEvent) {
        contentPane.requestFocus();
        contentPane.getChildren().clear();
        contentPane.getChildren().add(CacheFXML("listView"));
    }

    public void settingsButton_Clicked(ActionEvent actionEvent) {
        contentPane.requestFocus();
        contentPane.getChildren().clear();
        contentPane.getChildren().add(CacheFXML("settingsView"));
    }

}

interface Action {
    void run();
}