package com.example.studentagenda;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class ListController {

    public VBox listBox;
    public ScrollPane mainPane;

    public void initialize() {
        AnchorPane.setBottomAnchor(mainPane, 0d);
        AnchorPane.setTopAnchor(mainPane, 0d);
        AnchorPane.setLeftAnchor(mainPane, 0d);
        AnchorPane.setRightAnchor(mainPane, 0d);
        loadList();
    }

    private void loadList() {
        for (Class classItem: Data.classes) {
            for (Item item: classItem.items) {
                Pair<ItemController, Node> pair = Main.InstanceFXML("itemView");
                pair.getKey().fill(item.name, item.description, "new tag", classItem.color);
                listBox.getChildren().add(pair.getValue());
            }
        }
    }
}
