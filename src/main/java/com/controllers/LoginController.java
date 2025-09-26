package com.controllers;

import java.io.IOException;

import com.model.Admin;
import com.model.Klijent;
import com.model.Korisnik;
import com.model.Vlasnik;
import com.util.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class LoginController {

    @FXML private TextField tfKorisnickoIme;
    @FXML private PasswordField pfLozinka;
    @FXML private Button btnLogin;
    @FXML private Button btnKreiraj;

    @FXML
    private void initialize() {
        setAllUserData();
    }

    @FXML 
    private void handleLogin() {
        String korisnicko_ime = tfKorisnickoIme.getText();
        String lozinka = pfLozinka.getText();

        if (Korisnik.checkKorisnicko_ime(korisnicko_ime).equals("Admin")) {
            Admin admin = Admin.getAdmin(korisnicko_ime, lozinka);
            if (admin == null) return;

            try {
                System.out.println("Creating Admin scene...");
                SceneManager.showAdminScene(admin);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (Korisnik.checkKorisnicko_ime(korisnicko_ime).equals("Vlasnik")) {
            Vlasnik vlasnik = Vlasnik.getVlasnik(korisnicko_ime, lozinka);
            if (vlasnik == null) return;

            try {
                System.out.println("Creating Vlasnik scene...");
                SceneManager.showVlasnikScene(vlasnik);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (Korisnik.checkKorisnicko_ime(korisnicko_ime).equals("Klijent")) {
            Klijent klijent = Klijent.getKlijent(korisnicko_ime, lozinka);
            if (klijent == null) return;

            try {
                System.out.println("Creating Klijent scene...");
                SceneManager.showKlijentScene(klijent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText("Unijeli ste pogresno korisnicko ime ili lozinku!!!");
        }

    }

    @FXML
    private void handleSignUp() {
        try {
            SceneManager.showSignUpScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void setAllUserData() {
        tfKorisnickoIme.setUserData("");
        pfLozinka.setUserData("");
    }
}
