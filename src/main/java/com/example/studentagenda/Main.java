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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
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

    public HBox timeHBox;
    public MenuButton timeMenuButton;
    public TextField hourField;
    public TextField minuteField;

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
        populateAgenda();

        // reload agenda on task change
        Agenda.onTasksChanged.add(() -> {
            populateAgenda();
        });

        // delete task on delete button
        deleteButton.setOnAction(event -> {
            if (selectedTask != null) {
                Agenda.deleteTask(selectedTask.model);
            }
        });

    }

    public void setSelectedTask(TaskView view) {
        this.selectedTask = view;
        if (selectedTask != null) {
            System.out.println(selectedTask.model.name);
        }

    }

    public void populateAgenda() {
        taskVBox.getChildren().clear();
        if (Agenda.getCurrentInterval().size() != 0) {
            taskVBox.getChildren().addAll(Agenda.generate());
        }
    }


    // endregion < Agenda Tab View >

    // region < Add View >

    public void setupAddView() {

        // configure initial values
        nameBox.setText("");
        colorPicker.setValue(Color.VIOLET);
        deadlinePicker.setValue(null);
        newTaskRect.setFill(Color.VIOLET);
        addButton.setDisable(true);
        hide(timeHBox);

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
        hourField.textProperty().addListener((observable, oldv, newv) -> {
            addButton.setDisable(!checkCanAdd());
        });
        minuteField.textProperty().addListener((observable, oldv, newv) -> {
            addButton.setDisable(!checkCanAdd());
        });

        // configure add option changes
        String[] options = new String[] {"Category", "Task", "Tag"};
        makeStickyMenuButton(addSelectButton, new ArrayList<>(List.of(options)), 0);
        addSelectButton.textProperty().addListener((observable, oldv, newv) -> {
            if (newv.equals("Category")) {
                unhide(colorHBox, allCategoriesBox);
                hide(deadlineHBox, toLabel, addSelectCategoryButton, newTaskAnchor, allTagsBox, timeHBox);
                nameBox.setText("");
                // addButton function
                addButton.setOnAction(event -> {
                    Category cat = new Category(nameBox.getText(), colorPicker.getValue().toString());
                    Agenda.categories.add(cat);
                    // clear fields
                    RunAsync(() -> {
                        try { Thread.sleep(500); } catch (Exception ignored) {}
                        Queue(() -> {
                            nameBox.setText("");
                            colorPicker.setValue(Color.VIOLET);
                        });
                    });
                });
            } else if (newv.equals("Task")) {
                unhide(toLabel, addSelectCategoryButton, deadlineHBox, newTaskAnchor, timeHBox);
                hide(allTagsBox, colorHBox, allCategoriesBox);
                nameBox.setText("");
                // addButton function
                addButton.setOnAction(event -> {
                    Task task = new Task(nameBox.getText());
                    task.deadline.set(deadlinePicker.getValue());
                    task.time.set(LocalDateTime.of(
                        task.deadline.get(),
                        LocalTime.of(Integer.parseInt(hourField.getText()),Integer.parseInt(hourField.getText()))));
                    Agenda.getCategory(addSelectCategoryButton.getText()).tasks.add(task);
                    // clear fields
                    RunAsync(() -> {
                        try { Thread.sleep(500); } catch (Exception ignored) {}
                        Queue(() -> {
                            nameBox.setText("");
                            deadlinePicker.setValue(null);
                            hourField.setText("11");
                            minuteField.setText("59");
                        });
                    });
                });
            } else { // Tag
                unhide(allTagsBox);
                hide(colorHBox, deadlineHBox, newTaskAnchor, allCategoriesBox, toLabel, addSelectCategoryButton, timeHBox);
                nameBox.setText("");
                // addButton function
                addButton.setOnAction(event -> {
                    Tag tag = new Tag(nameBox.getText());
                    Agenda.tags.add(tag);
                    // clear fields
                    RunAsync(() -> {
                        try { Thread.sleep(500); } catch (Exception ignored) {}
                        Queue(() -> {
                            nameBox.setText("");
                        });
                    });
                });

            }
        });

        // configure AM/PM changes
        String[] timeOptions = new String[] {"PM", "AM"};
        makeStickyMenuButton(timeMenuButton, new ArrayList<>(List.of(timeOptions)), 0);

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
            boolean timeInRange = false;
            if (isNumeric(hourField.getText()) && isNumeric(minuteField.getText())) {
                int hour = Integer.parseInt(hourField.getText());
                int minute = Integer.parseInt(minuteField.getText());
                if (hour > 0 && hour < 13 && minute > -1 && minute < 61) {
                    timeInRange = true;
                }
            }
            return (timeInRange && nameBox.getText().length() >= 3 && nameBox.getText().length() <= 20)
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

    public static boolean isNumeric(String target) {
        if (target == null) {
            return false;
        }
        try {
            Double.parseDouble(target);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    // endregion

}