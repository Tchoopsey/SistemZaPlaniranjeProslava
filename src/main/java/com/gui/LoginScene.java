package com.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginScene {

    /**
     * @return new Scene
     *  returns new MainScene asociated with the type of user thats using the app
     */

    public static Scene getScene() {
        System.out.println("Creating LoginScene...");
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Label lblUsername = new Label("Korisnicko ime:");
        TextField tfUsername = new TextField();
        tfUsername.setPromptText("Korisnicko ime...");
        tfUsername.setMaxWidth(200);

        Label lblPassword = new Label("Lozinka:");
        TextField tfPassword = new TextField();
        tfPassword.setPromptText("Lozinka...");
        tfPassword.setMaxWidth(200);

        // Label lblLogin = new Label("Login");

        // TODO: needs to log in to specific main page based on Username and Password
        Button btnLogin = new Button("Login");
        btnLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                String username = tfUsername.getText();
                if (username.equals("asd")) {
                    SceneManager.showMainScene();
                    return;
                }
                SceneManager.showAdminScene();
            }
        });


        layout.getChildren().addAll(lblUsername, tfUsername, 
            lblPassword, tfPassword, btnLogin);
        
        return new Scene(layout, 900, 600);
    }
}
