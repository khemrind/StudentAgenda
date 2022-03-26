package com.example.studentagenda;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

public class Main {

    public static Main Instance;

    public void initialize() {
        // https://kordamp.org/ikonli/cheat-sheet-fluentui.html
        Data.initialize();
        if (Instance == null) {
            Instance = this;
        }
        //Platform.runLater(() -> contentPane.requestFocus());
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
/*
    public void addButton_Clicked(ActionEvent actionEvent) {
        contentPane.getChildren().clear();
        Node loaded = CacheFXML("addView");
        contentPane.getChildren().add(loaded);
        setZeroAnchor(loaded);
        contentLabel.setText("?");
    }

    public void listButton_Clicked(ActionEvent actionEvent) {
        contentPane.getChildren().clear();
        Node loaded = CacheFXML("listView");
        contentPane.getChildren().add(loaded);
        setZeroAnchor(loaded);
        contentLabel.setText("Agenda");
    }

    public void settingsButton_Clicked(ActionEvent actionEvent) {
        contentPane.getChildren().clear();
        Node loaded = CacheFXML("settingsView");
        contentPane.getChildren().add(loaded);
        setZeroAnchor(loaded);
        contentLabel.setText("Settings");
    }*/

    public void setZeroAnchor(Node node) {
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
    }

}

interface Action {
    void run();
}