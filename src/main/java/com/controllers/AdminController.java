package com.controllers;

import java.io.IOException;

import com.model.Admin;
import com.model.Objekat;
import com.util.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class AdminController {

    static Admin trenutniAdmin;
    @FXML Label lblIme;
    @FXML Label lblPrezime;
    @FXML Label lblKorisnickoIme;
    @FXML ListView<Objekat> lvObjekti;
    @FXML Button btnNoviAdmin;
    @FXML Button btnOdobri;
    @FXML Button btnOdbij;


    @FXML
    private void initialize() {
        setAllUserData();
    }

    @FXML
    private void handleLogout() {
        try {
            trenutniAdmin = null;
            SceneManager.showLoginScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNoviAdmin() {
        try {
            SceneManager.showCreateAdminScene(trenutniAdmin);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    @FXML
    private void handleOdobri() {
        
    }

    @FXML
    private void handleOdbij() {
        
    }

    private void setAllUserData() {
        trenutniAdmin = (Admin) SceneManager.getKorisnik();
        lblIme.setText(trenutniAdmin.getIme());
        lblPrezime.setText(trenutniAdmin.getPrezime());
        lblKorisnickoIme.setText(trenutniAdmin.getKorisnicko_ime());
    }

}
