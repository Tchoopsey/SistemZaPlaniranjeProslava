package com.controllers;

import java.io.IOException;

import com.model.BankovniRacun;
import com.model.Klijent;
import com.model.Meni;
import com.model.Objekat;
import com.model.Sto;
import com.util.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NovaRezervacijaController {

    static Klijent trenutniKlijent;
    @FXML Label lblIme;
    @FXML Label lblPrezime;
    @FXML Label lblKorisnickoIme;
    @FXML Label lblStanje;

    static Objekat trenutniObjekat;
    @FXML TextField tfNaziv;
    @FXML TextField tfGrad;
    @FXML TextField tfDatum;
    @FXML TextField tfCijenaRezervacije;
    @FXML TextField tfBrojMjesta;
    @FXML TextField tfBrojStolova;

    @FXML TextField tfBrojGostiju;
    @FXML ChoiceBox<Sto> cbSto;
    @FXML ChoiceBox<Meni> cbMeni;
    @FXML DatePicker dpDatum;
    @FXML TextArea taGosti;
    
    @FXML
    private void initialize() {
        setAllUserData();
    }

    @FXML
    private void handleNazad() {
        try {
            trenutniKlijent = null;
            SceneManager.showLoginScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDodaj() {
        
    }

    private void setAllUserData() {
        trenutniKlijent = (Klijent) SceneManager.getKorisnik();
        lblIme.setText(trenutniKlijent.getIme());
        lblPrezime.setText(trenutniKlijent.getPrezime());
        lblKorisnickoIme.setText(trenutniKlijent.getKorisnicko_ime());
        BankovniRacun racun = BankovniRacun.getByBrojRacuna(trenutniKlijent.getBroj_racuna());
        lblStanje.setText(""+racun.getStanje());
        tfNaziv.setUserData("");
        tfGrad.setUserData("");
        tfDatum.setUserData("");
        tfCijenaRezervacije.setUserData("");
        tfBrojMjesta.setUserData("");
        tfBrojStolova.setUserData("");
        tfBrojGostiju.setUserData("");
        tfBrojGostiju.setUserData("");
    }

}
