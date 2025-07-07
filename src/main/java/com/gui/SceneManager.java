package com.gui;

import javafx.stage.Stage;

public class SceneManager {

    private static Stage mainStage;

    public static void init(Stage stage) {
        mainStage = stage;
        mainStage.setTitle("Sistem Rezervacija");
    }

    public static void showLoginScene() {
        mainStage.setScene(LoginScene.getScene());
        mainStage.show();
    }

    public static void showMainScene() {
        mainStage.setScene(MainScene.getScene());
        mainStage.show();
    }

    public static void showAdminScene() {
        mainStage.setScene(AdminScene.getScene());
        mainStage.show();
    }
}
