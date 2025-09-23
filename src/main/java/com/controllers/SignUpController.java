package com.controllers;

import java.io.IOException;

import com.dao.KlijentDAO;
import com.dao.VlasnikDAO;
import com.model.BankovniRacun;
import com.model.Klijent;
import com.model.Korisnik;
import com.model.Vlasnik;
import com.util.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class SignUpController {

    @FXML private TextField tfIme;
    @FXML private Label lblImeError;
    @FXML private TextField tfPrezime;
    @FXML private Label lblPrezimeError;
    @FXML private TextField tfBrojTelefona;
    @FXML private Label lblBrojTelefonaError;
    @FXML private TextField tfJMBG;
    @FXML private Label lblJMBGError;
    @FXML private TextField tfBrojRacuna;
    @FXML private Label lblBrojRacunaError;
    @FXML private TextField tfKorisnickoIme;
    @FXML private Label lblKorisnickoImeError;
    @FXML private PasswordField pfLozinka;
    @FXML private Label lblLozinkaError;
    @FXML private PasswordField pfPotvrda;
    @FXML private Label lblPotvrdaError;
    @FXML private Button btnKreiraj;
    @FXML private Button btnOtkazi;
    @FXML private ToggleGroup tgKorisnik;
    @FXML private RadioButton rbVlasnik;
    @FXML private RadioButton rbKlijent;

    @FXML
    private void initialize() {
        System.out.println("Kreiranje korisnika...");
        setAllUserData();
        clearLabels();
    }

    @FXML
    private void handleSignUp() {
        clearLabels();
        boolean uslov = true;
        String ime = tfIme.getText();
        String prezime = tfPrezime.getText();
        String broj_telefona = tfBrojTelefona.getText();
        String broj_racuna = tfBrojRacuna.getText();
        String jmbg = tfJMBG.getText();
        String korisnicko_ime = tfKorisnickoIme.getText();
        String password = pfLozinka.getText();
        String pw_confirm = pfPotvrda.getText();
        String selected = tgKorisnik.getSelectedToggle().getUserData().toString();

        if (!password.equals(pw_confirm)) {
            System.err.println("Kreiranje korisnika neuspjesno!");
            lblPotvrdaError.setText("Lozinke se ne poklapaju!");
        }
        
        if (Korisnik.isPresent(korisnicko_ime)) {
            System.err.println("Kreiranje korisnika neuspjesno!");
            lblKorisnickoImeError.setText("Korisnicko ime je zauzeto");
        }

        if (ime == "") {
            lblImeError.setText("Niste unijeli ime!");
            System.err.println("Kreiranje korisnika neuspjesno!");
            uslov = false;
        }
        if (prezime == "") {
            lblPrezimeError.setText("Niste unijeli prezime!");
            System.err.println("Kreiranje korisnika neuspjesno!");
            uslov = false;
        }
        if (broj_telefona == "") {
            lblBrojTelefonaError.setText("Niste unijeli broj telefona!");
            System.err.println("Kreiranje korisnika neuspjesno!");
            uslov = false;
        }
        if (jmbg == "") {
            lblJMBGError.setText("Niste unijeli jmbg!");
            System.err.println("Kreiranje korisnika neuspjesno!");
            uslov = false;
        }
        if (broj_racuna == "") {
            lblBrojRacunaError.setText("Niste unijeli broj racuna!");
            System.err.println("Kreiranje korisnika neuspjesno!");
            uslov = false;
        }
        if (korisnicko_ime == "") {
            lblKorisnickoImeError.setText("Niste unijeli korisnicko ime!");
            System.err.println("Kreiranje korisnika neuspjesno!");
            uslov = false;
        }
        if (password == "") {
            lblLozinkaError.setText("Niste unijeli lozinku!");
            System.err.println("Kreiranje korisnika neuspjesno!");
            uslov = false;
        }

        if (!BankovniRacun.jmbgExists(jmbg)) {
            System.err.println("Kreiranje korisnika neuspjesno!");
            lblJMBGError.setText("JMBG ne postoji u bazi banke!");
            uslov = false;
        }

        if (!BankovniRacun.broj_racunaExists(broj_racuna)) {
            System.err.println("Kreiranje korisnika neuspjesno!");
            lblBrojRacunaError.setText("Racun ne postoji u bazi banke!");
            uslov = false;
        }

        if (!uslov) return;

        if (selected.equals("Klijent") && uslov == true) {
            Klijent klijent = new Klijent(0, ime, prezime, jmbg, korisnicko_ime, broj_racuna, password, broj_telefona);
            KlijentDAO dao = new KlijentDAO();
            dao.createKlijent(klijent);
        } else {
            Vlasnik vlasnik = new Vlasnik(0, ime, prezime, jmbg, korisnicko_ime, broj_racuna, password, broj_telefona);
            VlasnikDAO dao = new VlasnikDAO();
            dao.createVlasnik(vlasnik);
        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Kreiranje uspjesno!");
        alert.setHeaderText("Good boy/girl!");
        alert.setContentText("Uspjesno ste kreirali novog korisnika!");
        alert.showAndWait();

        try {
            SceneManager.showLoginScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOtkazi() {
        try {
            SceneManager.showLoginScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setAllUserData() {
        tfIme.setUserData("");
        tfPrezime.setUserData("");
        tfBrojTelefona.setUserData("");
        tfKorisnickoIme.setUserData("");
        tfJMBG.setUserData("");
        tfBrojRacuna.setUserData("");
        pfLozinka.setUserData("");
        pfPotvrda.setUserData("");
        rbKlijent.setUserData("Klijent");
        rbVlasnik.setUserData("Vlasnik");
    }

    private void clearLabels() {
        lblImeError.setText("");
        lblPrezimeError.setText("");
        lblBrojTelefonaError.setText("");
        lblKorisnickoImeError.setText("");
        lblJMBGError.setText("");
        lblBrojRacunaError.setText("");
        lblLozinkaError.setText("");
        lblPotvrdaError.setText("");
    }

}
