package com.example.studentagenda;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Agenda {

    public static Agenda Instance = new Agenda();

    public static ArrayList<Category> base_categories = new ArrayList<>();
    public static ArrayList<Tag> base_tags = new ArrayList<>();
//transient- serilizer dont look, functionallity not data- thats why its action

    public static transient ArrayList<Action> onCategoriesChanged = new ArrayList<>();
    public static transient ArrayList<Action> onTasksChanged = new ArrayList<>();

    public static transient SimpleListProperty<Category> categories = new SimpleListProperty<>(
        Instance, "categories", FXCollections.observableList(base_categories));

    public static transient SimpleListProperty<Tag> tags = new SimpleListProperty<>(
        Instance, "tags", FXCollections.observableList(base_tags));

    public static boolean activeClock = true;

    public static ArrayList<Action> onSecondChange = new ArrayList<>();

    public static void update(Task task) {
        // if task.deadline is before now, request dimming
            // if task.deadline is also not completed, set to missed
    }


//enum(enumeration) - numbering of catergories

    public enum FilterInterval {
        Week,
        Month,
        All
    }

    public static void initialize() {

        // add registered actions to list's onChange
        //(?) means anytype(object) that extends some class
        categories.get().addListener((ListChangeListener<? super Category>) event -> {
            for (Action action: onCategoriesChanged) {
                action.run();
            }
        });

        // start clock in another thread
        Main.RunAsync(() -> {
            while(activeClock) {
                for (Action action: onSecondChange) {
                    action.run();
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ignored) {}
            }
        });

    }

    public static void notifyTasksChanged() {
        for (Action action: onTasksChanged) {
            action.run();
        }
    }

    public static ArrayList<Task> getTasks() {
        ArrayList<Task> output = new ArrayList<>();
        for (Category category: categories.get()) {
            for (Task task: category.tasks.get()) {
                output.add(task);
            }
        }
        return output;
    }

    public static Category addCategory(String name, String color) {
        Category category = new Category(name, color);
        categories.add(category);
        return category;
    }

    public static void deleteCategory(String name) {
        int index = 0;
        for (Category category: categories.get()) {
            if (name.equals(category.name)) {
                categories.remove(index);
            }
            index++;
        }
    }

    public static Category getCategory(String target) {
        for (Category category: categories) {
            if (category.name.equals(target)) {
                return category;
            }
        }
        return null;
    }

    public static String getCategoryFromTask(Task element) {
        for (Category category: categories) {
            for (Task task: category.tasks) {
                if (task.identifier.equals(element.identifier)) {
                    return category.name;
                }
            }
        }
        return null;
    }

    public static ArrayList<Task> filter(Tag element) {
        ArrayList<Task> output = new ArrayList<>();
        for (Category category: categories) {
            for (Task task: category.tasks) {
                if (task.tags.get().contains(element)) {
                    output.add(task);
                }
            }
        }
        return output;
    }

    public static ArrayList<Task> filter(Category element) {
        ArrayList<Task> output = new ArrayList<>();
        for (Category category: categories) {
            if (category.identifier.equals(element.identifier)) {
                output = category.base_tasks;
            }
        }
        return output;
    }

    public static ArrayList<Task> filter(FilterInterval interval) {
        ArrayList<Task> output = new ArrayList<>();
        // implement
        // filter by time
        return output;
    }

    public static ArrayList<String> getCategoryNames() {
        ArrayList<String> output = new ArrayList<>();
        for (Category category: categories) {
            output.add(category.name);
        }
        return output;
    }

    public static boolean isEmpty() {
        return (categories.isEmpty());
    }

}
