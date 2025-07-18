package com.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        System.out.println("Starting application...");
        SceneManager.init(stage);
        SceneManager.showLoginScene();
    }
}
