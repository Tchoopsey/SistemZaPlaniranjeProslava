package com.controllers;

import java.io.IOException;

import com.model.BankovniRacun;
import com.model.Klijent;
import com.model.Objekat;
import com.model.Proslava;
import com.util.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class KlijentController {

    static Klijent trenutniKlijent;
    @FXML Label lblIme;
    @FXML Label lblPrezime;
    @FXML Label lblKorisnickoIme;
    @FXML Label lblStanje;
    @FXML ListView<Objekat> lvObjekti;
    @FXML ListView<Proslava> lvRezervacija;
    @FXML TextField tfBrojMjesta;
    @FXML TextField tfDatum;
    @FXML TextField tfGrad;

    @FXML
    private void initialize() {
        setAllUserData();

        printObjekti();
    }

    @FXML
    public void handleDodajRezervaciju() {
        Objekat objekat = lvObjekti.getSelectionModel().getSelectedItem();
        try {
            SceneManager.showNovaRezervacijaScene(trenutniKlijent, objekat);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleIzmjeniRezervaciju() {
        Objekat objekat = lvObjekti.getSelectionModel().getSelectedItem();
        try {
            SceneManager.showNovaRezervacijaScene(trenutniKlijent, objekat);
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    private void setAllUserData() {
        trenutniKlijent = (Klijent) SceneManager.getKorisnik();
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

    private void printObjekti() {
        for (Objekat objekat : Objekat.getSviObjekti()) {
            if (trenutniKlijent.getId() == objekat.getVlasnik().getId()) {
                lvObjekti.getItems().add(objekat);
            }
        }
    }
    
}
