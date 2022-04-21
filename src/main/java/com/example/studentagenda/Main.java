package com.example.studentagenda;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static Main Instance;

    // region < FXML UI Elements >

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
    public AnchorPane newTaskAnchor;
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
    public VBox allCategoriesBox;
    public Button deleteTagButton;

    // endregion

    private TaskView selectedTask;

    // icons: https://kordamp.org/ikonli/cheat-sheet-fluentui.html

    public void initialize() {
        if (Instance == null) {
            Instance = this;
        }

        setupAddView();
        setupAgendaView();

        Agenda.addCategory("Miscellaneous", "ORANGE");
        Agenda.addCategory("CLASS 2212 Some Specific Course Title", "NAVY");
        Agenda.addCategory("CLASS 1225 Some Specific Course Title", "4d4d4d");

    }

    // region < Agenda Tab View >

    public void setupAgendaView() {
        setSelectedTask(null);

    }

    public void setSelectedTask(TaskView view) {
        this.selectedTask = view;
        selectedHBox.setDisable(view == null);
    }

    // endregion

    // region < Add View >

    public void setupAddView() {

        // configure initial values
        nameBox.setText("");
        colorPicker.setValue(Color.VIOLET);
        deadlinePicker.setValue(null);
        newTaskRect.setFill(Color.VIOLET);
        addButton.setDisable(true);

        // configure fields' binding to example task
        nameBox.textProperty().addListener((observable, oldv, newv) -> {
            newTaskTitle.setText(newv);
            addButton.setDisable(!checkCanAdd());
        });
        deadlinePicker.setOnAction(event -> {
            addButton.setDisable(!checkCanAdd());
        });
        colorPicker.setOnAction(event -> {
            addButton.setDisable(!checkCanAdd());
        });

        // configure add option changes
        String[] options = new String[] {"Category", "Task", "Tag"};
        addSelectButton.textProperty().addListener((observable, oldv, newv) -> {
            if (addSelectButton.getText().equals("Category")) {
                unhide(colorHBox, allCategoriesBox);
                hide(deadlineHBox, toLabel, addSelectCategoryButton, newTaskAnchor, allTagsBox);
                nameBox.setText("");
                // addButton function
                addButton.setOnAction(event -> {
                    Category cat = new Category(nameBox.getText(), colorPicker.getValue().toString());
                    Agenda.categories.add(cat);
                });
            } else if (addSelectButton.getText().equals("Task")) {
                unhide(toLabel, addSelectCategoryButton, deadlineHBox, newTaskAnchor);
                hide(allTagsBox, colorHBox, allCategoriesBox);
                nameBox.setText("");
                // addButton function
                addButton.setOnAction(event -> {
                    Task task = new Task(nameBox.getText());
                    Agenda.getCategory(addSelectCategoryButton.getText()).tasks.add(task);
                });
            } else { // Tag
                unhide(allTagsBox);
                hide(colorHBox, deadlineHBox, newTaskAnchor, allCategoriesBox, toLabel, addSelectCategoryButton);
                nameBox.setText("");
                // addButton function
                addButton.setOnAction(event -> {
                    Tag tag = new Tag(nameBox.getText());
                    Agenda.tags.add(tag);
                });
            }
        });
        makeStickyMenuButton(addSelectButton, new ArrayList<>(List.of(options)), 0);

        // configure category changes
        addSelectCategoryButton.textProperty().addListener((observable, oldv, newv) -> {
            if (Agenda.isEmpty()) { return; }
            Category category = Agenda.getCategory(addSelectCategoryButton.getText());
            newTaskRect.setFill(category.getColor());
            newTaskCategory.setText(category.name);
        });
        if (Agenda.isEmpty()) {
            addSelectCategoryButton.setText("");
            addSelectCategoryButton.getItems().clear();
        } else {
            makeStickyMenuButton(addSelectCategoryButton, Agenda.getCategoryNames(), 0);
        }

        // recompute after category list changes
        Agenda.onCategoriesChanged.add(() -> {
            if (Agenda.isEmpty()) { return; }
            ArrayList<String> names = Agenda.getCategoryNames();
            int location = names.indexOf(addSelectCategoryButton.getText());
            location = (location == -1) ? 0: location;
            makeStickyMenuButton(addSelectCategoryButton, names, location);
            // update box
            allCategoriesBox.getChildren().clear();
            for (Category category: Agenda.categories.get()) {
                allCategoriesBox.getChildren().add(category.view());
            }

        });

        // reload tasks on list change
        Agenda.onTasksChanged.add(() -> {
            TaskView.generate(taskVBox.getChildren(), Agenda.getTasks());
        });

        // reload tags on list change
        Agenda.tags.get().addListener((ListChangeListener<? super Tag>) event -> {
            allTagsBox.getChildren().clear();
            for (Tag tag: Agenda.tags.get()) {
                allTagsBox.getChildren().add(tag.view());
            }
        });
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
            }
            index++;
        }
    }

    public boolean checkCanAdd() {
        if (addSelectButton.getText().equals("Category")) {
            return (nameBox.getText().length() >= 3 && nameBox.getText().length() <= 20)
                    && (colorPicker.getValue() != null);
        } else if (addSelectButton.getText().equals("Task")) {
            return (nameBox.getText().length() >= 3 && nameBox.getText().length() <= 20)
                    && (deadlinePicker.getValue() != null);
        } else { // Tag
            return (nameBox.getText().length() >= 3 && nameBox.getText().length() <= 20);
        }
    }

    // endregion

    // region < UI Functions >
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

    // endregion

    // region < Base Functionality >

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

    // endregion

}