package com.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AdminScene {

    public static Scene getScene() {
        System.out.println("Creating AdminScene...");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Label lbl = new Label("This is the AdminScene...");
        Button btnLogout = new Button("Logout");

        btnLogout.setOnAction(e -> {
            System.out.println("Logging out from AdminScene...");
            SceneManager.showLoginScene();
        });

        layout.getChildren().addAll(lbl, btnLogout);
        
        return new Scene(layout, 1200, 900);
    }
}
