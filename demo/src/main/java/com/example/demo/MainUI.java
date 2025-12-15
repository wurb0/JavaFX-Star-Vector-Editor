package com.example.demo;


import javafx.scene.layout.*;


public class MainUI {
    private BorderPane root;

    public MainUI(StarView view) {
        root = new BorderPane();
        Pane centerPane = new Pane();
        centerPane.getChildren().addAll(view.getCanvas());
        root.setCenter(centerPane);
    }

    public BorderPane getRoot() {
        return root;
    }
}
