package com.example.studentagenda;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class AddController {

    public VBox mainBox;
    public MenuButton menuButton;
    public ListView itemList;

    @FXML
    private void initialize() {
        AnchorPane.setBottomAnchor(mainBox, 0d);
        AnchorPane.setTopAnchor(mainBox, 0d);
        AnchorPane.setLeftAnchor(mainBox, 0d);
        AnchorPane.setRightAnchor(mainBox, 0d);
    }


}
