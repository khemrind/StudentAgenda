package com.example.studentagenda;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public static boolean activeClock = true;

    public static ArrayList<Action> onSecondChange = new ArrayList<>();

    public static FilterInterval interval = FilterInterval.All;

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
                try {
                    for (Action action: onSecondChange) {
                        action.run();
                    }
                    Thread.sleep(1000);
                } catch (Exception ignored) {}
            }
        });

        // update all tasks per second
        onSecondChange.add(() -> {
            for (Task task: getCurrentInterval()) {
                LocalDateTime time = LocalDateTime.now();
                if (task.time.get().isBefore(time) && task.status.get() != Task.Status.Completed) {
                    task.status.set(Task.Status.Missed);
                }
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

    public static void deleteTask(Task element) {
        for (Category category: categories.get()) {
            int index = 0;
            for (Task task: category.tasks.get()) {
                if (task.identifier.equals(element.identifier)) {
                    category.tasks.remove(index);
                    notifyTasksChanged();
                    return;
                }
                index++;
            }
        }
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

    public static ArrayList<Task> getCurrentInterval() {
        if (interval == FilterInterval.All) { return getTasks(); }
        ArrayList<Task> output = new ArrayList<>();
        ArrayList<Task> list = getTasks();

        // get interval
        LocalDate now = LocalDate.now();
        LocalDate later = LocalDate.now();
        if (interval == FilterInterval.Month) {
            later = later.plusDays(30);
        } else { // Week
            later = later.plusDays(7);
        }

        // fill list
        for (Task task: list) {
            LocalDate dl = task.deadline.get();
            if (dl.isAfter(now) && dl.isBefore(later)) {
                output.add(task);
            }
        }

        return output;
    }

    public static ArrayList<String> getCategoryNames() {
        ArrayList<String> output = new ArrayList<>();
        for (Category category: categories) {
            output.add(category.name);
        }
        return output;
    }

    public static ArrayList<Node> generate() {
        if (getCurrentInterval().size() == 0) { return null; }
        // + keep scroll placement
        ArrayList<Node> output = new ArrayList<>();
        ArrayList<Task> list = getCurrentInterval();
        Collections.sort(list);

        // base case
        LocalDate current = list.get(0).deadline.get();
        output.add(TaskView.separator(format(current)));
        output.add(list.get(0).view());

        int year = LocalDate.now().getYear();

        for (int index = 1; index < list.size(); index++) {
            LocalDate next = list.get(index).deadline.get();
            if (current.getDayOfYear() < next.getDayOfYear()) {
                // add next's date as a separator
                if (next.getYear() != year) {
                    output.add(TaskView.separator(format(next) + ", " + next.getYear()));
                } else {
                    output.add(TaskView.separator(format(next)));
                }

                // shift to the new date
                current = next;
            }
            output.add(list.get(index).view());
        }

        return output;

    }

    private static int getDayOfYear(Task task) {
        return task.deadline.get().getDayOfYear();
    }

    private static String format(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, LLLL d");
        return date.format(formatter);
    }

    public static boolean isEmpty() {
        return (categories.isEmpty());
    }

}
