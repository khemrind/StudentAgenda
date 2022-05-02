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
    public Label newTaskCategory;
    public FontIcon newTaskIcon;
    public Rectangle newTaskRect;
    public Label newTaskHour;
    public Label newTaskMinute;
    public Label newTaskTimeHalf;

    public HBox otherBox;
    public Button clearButton;

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

    private static TaskView selectedTask;
    private static Tag selectedTag;
    private static Category selectedCategory;


    // icons: https://kordamp.org/ikonli/cheat-sheet-fluentui.html

    public void initialize() {
        if (Instance == null) {
            Instance = this;
        }

        setupAddView();
        setupAgendaView();

        // settings
        pathBox.setText(Data.path);

        Agenda.addCategory("Miscellaneous", "ORANGE");

    }

    // region < Agenda Tab View >

    public void setupAgendaView() {
        setSelectedTask(null);
        populateAgenda();

        // reload agenda on task change
        Agenda.onTasksChanged.add(this::populateAgenda);

        // delete task on delete button
        deleteButton.setOnAction(event -> {
            if (selectedTask != null) {
                Agenda.deleteTask(selectedTask.model);
            }
        });

        // complete task button
        completeButton.setOnAction(event -> {
            if (selectedTask != null) {
                selectedTask.model.status.set(Task.Status.Completed);
            }
        });

        // setup upper menu buttons
        String[] intervalOptions = new String[] {"All", "Week", "Month"};
        makeStickyMenuButton(filterIntervalButton, new ArrayList<>(List.of(intervalOptions)), 0);
        filterIntervalButton.textProperty().addListener((observable, oldv, newv) -> {
            if (newv.equals("All")) {
                Agenda.interval = Agenda.FilterInterval.All;
            } else if (newv.equals("Month")) {
                Agenda.interval = Agenda.FilterInterval.Month;
            } else { // Week
                Agenda.interval = Agenda.FilterInterval.Week;
            }
            populateAgenda();
        });

        String[] statusOptions = new String[] {"All", "Completed", "Missed"};
        makeStickyMenuButton(filterStatusButton, new ArrayList<>(List.of(statusOptions)), 0);
        filterStatusButton.textProperty().addListener((observable, oldv, newv) -> {
            // implement
        });

        makeStickyMenuButton(filterCategoryButton, new ArrayList<>(List.of(new String[] {"All"})), 0);
        Agenda.onCategoriesChanged.add(() -> {
            ArrayList<String> filters = Agenda.getCategoryNames();
            filters.add(0, "All");
            makeStickyMenuButton(filterCategoryButton, filters, 0);
        });
        filterCategoryButton.textProperty().addListener((observable, oldv, newv) -> {
            Agenda.categoryFilter = newv;
            populateAgenda();
        });

        makeStickyMenuButton(filterTagButton, new ArrayList<>(List.of(new String[] {"All"})), 0);
        Agenda.onTagsChanged.add(() -> {
            ArrayList<String> filters = Agenda.getTagNames();
            filters.add(0, "All");
            makeStickyMenuButton(filterTagButton, filters, 0);
        });
        filterTagButton.textProperty().addListener((observable, oldv, newv) -> {
            Agenda.tagFilter = newv;
            populateAgenda();
        });

    }

    public static void setSelectedTask(TaskView view) {
        selectedTask = view;
    }

    public static void setSelectedTag(Tag tag) {
        selectedTag = tag;
    }

    public static void setSelectedCategory(Category category) {
        selectedCategory = category;
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
        colorPicker.setValue(Color.VIOLET);
        deadlinePicker.setValue(null);
        newTaskRect.setFill(Color.VIOLET);
        addButton.setDisable(true);
        // setup initial category creation
        setupCategoryViewstate();

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
            newTaskHour.setText(newv);
        });
        minuteField.textProperty().addListener((observable, oldv, newv) -> {
            addButton.setDisable(!checkCanAdd());
            newTaskMinute.setText(newv);
        });

        // configure add option changes
        String[] options = new String[] {"Category", "Task", "Tag"};
        makeStickyMenuButton(addSelectButton, new ArrayList<>(List.of(options)), 0);
        addSelectButton.textProperty().addListener((observable, oldv, newv) -> {
            if (newv.equals("Category")) {
                setupCategoryViewstate();
            } else if (newv.equals("Task")) {
                unhide(toLabel, addSelectCategoryButton, deadlineHBox, newTaskAnchor, timeHBox, otherBox);
                hide(allTagsBox, colorHBox, allCategoriesBox, deleteTagButton);
                // addButton function
                addButton.setOnAction(event -> {
                    Task task = new Task(nameBox.getText());
                    task.deadline.set(deadlinePicker.getValue());
                    task.time.set(LocalDateTime.of(
                        task.deadline.get(),
                        LocalTime.of(Integer.parseInt(hourField.getText()),Integer.parseInt(minuteField.getText()))));
                    Agenda.getCategory(addSelectCategoryButton.getText()).tasks.add(task);
                    clearFields();
                });
            } else { // Tag
                unhide(allTagsBox, deleteTagButton);
                hide(colorHBox, deadlineHBox, newTaskAnchor, allCategoriesBox, toLabel, addSelectCategoryButton, timeHBox, otherBox);
                // addButton function
                addButton.setOnAction(event -> {
                    Tag tag = new Tag(nameBox.getText());
                    Agenda.tags.add(tag);
                    clearFields();
                });
                // deleteTagButton function
                deleteTagButton.setOnAction(event -> {
                    if (selectedTag != null) {
                        Agenda.tags.get().remove(selectedTag);
                        selectedTag = null;
                    }
                });
            }
        });

        // configure AM/PM changes
        String[] timeOptions = new String[] {"PM", "AM"};
        makeStickyMenuButton(timeMenuButton, new ArrayList<>(List.of(timeOptions)), 0);
        timeMenuButton.textProperty().addListener((observable, oldv, newv) -> {
            newTaskTimeHalf.setText(newv);
        });

        // configure hour, minute field contraints
        hourField.textProperty().addListener((observable, oldv, newv) -> {
            if (newv.length() > 2) {
                hourField.setText(newv.substring(0, 2));
            }
        });
        minuteField.textProperty().addListener((observable, oldv, newv) -> {
            if (newv.length() > 2) {
                minuteField.setText(newv.substring(0, 2));
            }
        });
        minuteField.focusedProperty().addListener((observable, oldv, newv) -> {
            if (!newv && minuteField.getText().length() == 1) {
                minuteField.setText("0" + minuteField.getText());
            } else if (!newv && minuteField.getText().length() == 0) {
                minuteField.setText("00");
            }
        });

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

        // setup tag
        applyTagButton.setText(" ");
        Agenda.onTagsChanged.add(() -> {
            applyTagButton.getItems().clear();
            if (Agenda.getTagNames().size() == 0) {
                applyTagButton.setText("  ");
                return;
            }
            for (String option: Agenda.getTagNames()) {
                MenuItem item = new MenuItem(option);
                applyTagButton.getItems().add(item);
                item.setOnAction(event -> {
                    ArrayList<String> labels = new ArrayList<>();
                    for (Node node: newTaskTagBox.getChildren()) {
                        labels.add(((Label) node).getText());
                    }
                    if (!labels.contains(option)) {
                        Tag tag = new Tag(option);
                        newTaskTagBox.getChildren().add(tag.minimal_view());
                    }
                });
            }
        });

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

        // clear button functionality
        clearButton.setOnAction(event -> clearFields());
    }

    public void setupCategoryViewstate() {
        unhide(colorHBox, allCategoriesBox, deleteTagButton);
        hide(deadlineHBox, toLabel, addSelectCategoryButton, newTaskAnchor, allTagsBox, timeHBox, otherBox);
        // addButton function
        addButton.setOnAction(event -> {
            Category cat = new Category(nameBox.getText(), colorPicker.getValue().toString());
            Agenda.categories.add(cat);
            clearFields();
        });
        // deleteTagButton function
        deleteTagButton.setOnAction(event -> {
            if (selectedCategory != null) {
                Agenda.categories.get().remove(selectedCategory);
                selectedCategory = null;
            }
        });
    }

    public void clearFields() {
        RunAsync(() -> {
            try { Thread.sleep(500); } catch (Exception ignored) {}
            Queue(() -> {
                nameBox.clear();
                colorPicker.setValue(Color.VIOLET);
                deadlinePicker.setValue(null);
                hourField.setText("11");
                minuteField.setText("59");
                newTaskTagBox.getChildren().clear();
            });
        });
    }

    public void makeStickyMenuButton(MenuButton button, ArrayList<String> options, int showIndex) {
        if (options.size() == 0) {
            button.getItems().clear();
            button.setText("   ");
            return;
        }
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
            return (nameBox.getText().length() >= 3 && nameBox.getText().length() <= 40)
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
            return (timeInRange && nameBox.getText().length() >= 3 && nameBox.getText().length() <= 40)
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