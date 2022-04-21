package com.example.studentagenda;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

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

    public static void generate() {
        // keep scroll placement
        ArrayList<Node> output = new ArrayList<Node>();
        ArrayList<Task> list = getTasks();
        Collections.sort(list);
        int current = getDayOfYear(list.get(0));
        for (int index = 1; index < list.size(); index++) {
            int next = getDayOfYear(list.get(index));
            if (current < next) {
                
            }
        }

    }

    private static int getDayOfYear(Task task) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(task.deadline.get());
        return calendar.get(Calendar.DAY_OF_YEAR);
    }


    public static boolean isEmpty() {
        return (categories.isEmpty());
    }

}
