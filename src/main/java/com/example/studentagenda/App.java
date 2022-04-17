package com.example.studentagenda;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("mainView.fxml"));
        double fixed_width = 500;
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, fixed_width, 660);
        stage.setTitle("Student Agenda");
        stage.setMaxWidth(fixed_width);
        stage.setMinWidth(fixed_width);
        stage.setMinHeight(500);
        stage.setScene(scene);
        stage.show();
        root.requestFocus();

        // load data
        Data.initialize();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop(){ // application close
        Data.save();
    }
}

// extensions

interface Action {
    void run();
}