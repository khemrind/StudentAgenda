package com.example.studentagenda;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.Node;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Data {

    public static HashMap<String, Node> cache = new HashMap<>();
    public static String path = System.getProperty("user.home") + "\\Documents\\StudentAgenda.json";

    private static final Gson serializer = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    public static void initialize() {
        // save & load
        if (!new File(path).exists()) {
            save();
        } else { load(); }
    }

    public static void save() {
        try {
            Writer write = Files.newBufferedWriter(Paths.get(path));
            serializer.toJson(4, write);
            write.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static void load() {
        try {
            Reader read = Files.newBufferedReader(Paths.get(path));
            Agenda.Instance = serializer.fromJson(read, Agenda.class);
            read.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

}
