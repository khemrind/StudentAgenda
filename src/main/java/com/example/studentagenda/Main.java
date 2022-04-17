package com.example.studentagenda;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import org.kordamp.ikonli.javafx.FontIcon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static Main Instance;

    public MenuButton startWeekSelect;
    public TextField pathBox;

    public MenuButton filterIntervalButton;
    public MenuButton filterStatusButton;
    public MenuButton filterCategoryButton;
    public MenuButton filterTagButton;

    public VBox taskVBox;

    public HBox selectedHBox;
    public MenuButton applyTagButton;
    public Button completeButton;
    public Button deleteButton;
    public Button editButton;

    public MenuButton addSelectButton;
    public MenuButton addSelectCategoryButton;
    public Label toLabel;

    public TextField nameBox;
    public ColorPicker colorPicker;
    public DatePicker deadlinePicker;

    public Button addButton;
    public HBox newTaskTagBox;
    public Label newTaskTitle;
    public Label newTaskTime;
    public Label newTaskCategory;
    public FontIcon newTaskIcon;
    public Rectangle newTaskRect;

    public AnchorPane configPane;
    public HBox allTagsBox;
    public HBox colorHBox;
    public HBox deadlineHBox;

    private TaskView selectedTask;

    // icons: https://kordamp.org/ikonli/cheat-sheet-fluentui.html

    public HashMap<String, Action> actionMap = new HashMap<>();

    public void initialize() {
        if (Instance == null) {
            Instance = this;
        }

        setupAddView();


        selectedHBox.setDisable(true);

        // initial testing
        taskVBox.getChildren().add(TaskView.separator("Saturday, April 22, 2022"));

        Agenda.addCategory("PHIL 2010 Intro to Philosophy");
        Category category = Agenda.getCategory("PHIL 2010 Intro to Philosophy");

        Task first = new Task();
        first.name.set("Writing Assignment 1");
        first.tags.get().add(new Tag("stupid asf"));
        first.status.set(Task.Status.Completed);
        category.tasks.add(first);

        TaskView.generate(taskVBox.getChildren(), category.tasks);

    }

    public void setupAddView() {

        // configure add task/category view switching
        String[] options = new String[] {"Category", "Task", "Tag"};
        Action addOptionChanged = () -> {
            if (addSelectButton.getText().equals("Category")) {
                unhide(colorHBox);
                hide(allTagsBox, deadlineHBox, toLabel, addSelectCategoryButton);
            } else if (addSelectButton.getText().equals("Task")) {
                unhide(toLabel, addSelectCategoryButton, deadlineHBox);
                hide(allTagsBox, colorHBox);
            } else { // Tag
                unhide(toLabel, addSelectCategoryButton);
                hide(allTagsBox, colorHBox, deadlineHBox);
            }
        };
        actionMap.put("addOptionChanged", addOptionChanged);
        makeStickyMenuButton(addSelectButton, new ArrayList<>(List.of(options)), 0);


        // configure req fields binding to example task
        nameBox.setText("");
        colorPicker.setValue(Color.VIOLET);
        newTaskRect.setFill(Color.VIOLET);
        nameBox.textProperty().addListener(
            (observable, oldv, newv) -> newTaskTitle.setText(newv));
        colorPicker.setOnAction(event -> newTaskRect.setFill(colorPicker.getValue()));
    }

    public void setSelectedTask(TaskView view) {
        this.selectedTask = view;
        selectedHBox.setDisable(view == null);
    }

    public void makeStickyMenuButton(MenuButton button, ArrayList<String> options, int showIndex) {
        // clear and set show index
        button.getItems().clear();
        button.setText(options.get(showIndex));
        // fill the rest
        int index = 0;
        for (String option: options) {
            if (index != showIndex) {
                MenuItem item = new MenuItem(option);
                button.getItems().add(item);
                final int location = index;
                item.setOnAction(event -> makeStickyMenuButton(button, options, location));
                actionMap.get("addOptionChanged").run();
            }
            index++;
        }
    }

    public void hide(Node... elements) {
        for (Node element: elements) {
            element.setManaged(false);
            element.setVisible(false);
        }
    }

    public void unhide(Node... elements) {
        for (Node element: elements) {
            element.setManaged(true);
            element.setVisible(true);
        }

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