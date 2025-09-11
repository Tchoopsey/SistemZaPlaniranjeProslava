package com.controllers;

import java.io.IOException;

import com.model.BankovniRacun;
import com.model.Klijent;
import com.util.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AdminController {

    static Klijent trenutniKlijent;
    @FXML Label lblIme;
    @FXML Label lblPrezime;
    @FXML Label lblKorisnickoIme;
    @FXML Label lblStanje;
    @FXML ListView<String> lvObjekti;
    @FXML TextField tfBrojMjesta;
    @FXML TextField tfDatum;
    @FXML TextField tfGrad;

    @FXML
    private void initialize() {
        setAllUserData();
    }

    @FXML
    private void setAllUserData() {
        tfBrojMjesta.setUserData("");
        tfDatum.setUserData("");
        tfGrad.setUserData("");
        lvObjekti.setEditable(false);
        lblIme.setText(trenutniKlijent.getIme());
        lblPrezime.setText(trenutniKlijent.getPrezime());
        lblKorisnickoIme.setText(trenutniKlijent.getKorisnicko_ime());
        BankovniRacun racun = BankovniRacun.getByBrojRacuna(trenutniKlijent.getBroj_racuna());
        lblStanje.setText(""+racun.getStanje());
    }

    @FXML
    private void handleLogout() {
        try {
            trenutniKlijent = null;
            SceneManager.showLoginScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
