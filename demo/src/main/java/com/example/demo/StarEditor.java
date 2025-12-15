package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class StarEditor extends Application {
    public void start(Stage stage){
        StarModel model = new StarModel();
        InteractionModel iModel = new InteractionModel();
        StarView view = new StarView();

        view.setModel(model);
        model.addSub(view);
        view.setInteractionModel(iModel);
        iModel.setModel(model);


        StarController controller = new StarController(model, iModel, view);
        MainUI ui = new MainUI(view);
        Scene scene = new Scene(ui.getRoot());
        stage.setScene(scene);
        stage.setTitle("Star editor");
        stage.show();
        model.notifySubs();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
