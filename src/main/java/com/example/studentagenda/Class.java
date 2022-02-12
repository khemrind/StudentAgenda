package com.example.studentagenda;

import java.util.ArrayList;

public class Class {
    public String name;
    public String color; // ref javafx.scene.paint.Color enumeration
    public ArrayList<Item> items;

    public Class() {
        name = "new class";
        color = "BLUE";
        items = new ArrayList<>();
        items.add(new Item());
    }
}

