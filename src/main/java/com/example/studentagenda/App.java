package com.example.studentagenda;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("mainView.fxml"));
        double fixed_width = 500;
        Scene scene = new Scene(fxmlLoader.load(), fixed_width, 600);
        stage.setTitle("Student Agenda");
        stage.setMaxWidth(fixed_width);
        stage.setMinWidth(fixed_width);
        stage.setMinHeight(500);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop(){ // application close
        Data.save();
    }
}