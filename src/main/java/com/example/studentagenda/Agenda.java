package com.example.studentagenda;

import java.util.ArrayList;

public class Agenda {

    public static Agenda Instance = new Agenda();

    public ArrayList<Category> categories = new ArrayList<>();

    public Agenda() {

    }
}
