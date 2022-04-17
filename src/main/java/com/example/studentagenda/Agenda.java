package com.example.studentagenda;

import java.util.ArrayList;

public class Agenda {

    public static Agenda Instance = new Agenda();

    public static ArrayList<Category> categories = new ArrayList<>();

    public enum FilterInterval {
        Week,
        Month,
        All
    }

    public static void addCategory(String name) {
        categories.add(new Category(name));
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
                output = category.tasks;
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



}
