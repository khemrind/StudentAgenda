package com.example.studentagenda;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class Main {

    public static Main Instance;

    public MenuButton startWeekSelect;
    public TextField pathBox;
    public MenuButton addSelectButton;
    public MenuButton addSelectCategoryButton;
    public TextField nameBox;
    public ColorPicker colorPicker;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    public MenuButton filterIntervalButton;
    public MenuButton filterStatusButton;
    public MenuButton filterCategoryButton;
    public MenuButton filterTagButton;
    public MenuButton applyTagButton;
    public Label deleteButton;
    public Label editButton;
    public VBox taskVBox;

    // icons: https://kordamp.org/ikonli/cheat-sheet-fluentui.html

    public void initialize() {
        Data.initialize();
        if (Instance == null) {
            Instance = this;
        }

        // initial testing
        taskVBox.getChildren().add(TaskView.separator("Saturday, April 22, 2022"));

        Agenda.addCategory("misc");
        Category category = Agenda.getCategory("misc");

        Task first = new Task();
        first.name.set("some assignment 1");
        first.tags.get().add(new Tag("some tag"));
        first.status.set(Task.Status.InProgess);
        category.tasks.add(first);

        TaskView.generate(taskVBox.getChildren(), category.tasks);

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

    public static void Queue(Action action) {
        Platform.runLater(action::run);
    }

    public static void RunAsync(Action action) {
        Thread thread = new Thread(action::run);
        thread.start();
    }

}