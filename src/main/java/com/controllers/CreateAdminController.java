package com.controllers;

import java.io.IOException;

import com.dao.AdminDAO;
import com.model.Admin;
import com.model.Korisnik;
import com.util.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CreateAdminController {

    Admin trenutniAdmin;
    @FXML private TextField tfIme;
    @FXML private Label lblImeError;
    @FXML private TextField tfPrezime;
    @FXML private Label lblPrezimeError;
    @FXML private TextField tfKorisnickoIme;
    @FXML private Label lblKorisnickoImeError;
    @FXML private PasswordField pfLozinka;
    @FXML private Label lblLozinkaError;
    @FXML private PasswordField pfPotvrda;
    @FXML private Label lblPotvrdaError;
    @FXML private Button btnKreiraj;
    @FXML private Button btnOtkazi;

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
        String korisnicko_ime = tfKorisnickoIme.getText();
        String password = pfLozinka.getText();
        String pw_confirm = pfPotvrda.getText();

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

        if (!uslov) return;

        if (uslov) {
            Admin admin = new Admin(0, ime, prezime, korisnicko_ime, password);
            AdminDAO dao = new AdminDAO();
            dao.createAdmin(admin);
        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Kreiranje uspjesno!");
        alert.setHeaderText("Good boy/girl!");
        alert.setContentText("Uspjesno ste kreirali novog korisnika!");
        alert.showAndWait();

        try {
            SceneManager.showAdminScene(trenutniAdmin);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOtkazi() {
        try {
            SceneManager.showAdminScene(trenutniAdmin);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setAllUserData() {
        trenutniAdmin = (Admin) SceneManager.getKorisnik();
        tfIme.setUserData("");
        tfPrezime.setUserData("");
        tfKorisnickoIme.setUserData("");
        pfLozinka.setUserData("");
        pfPotvrda.setUserData("");
    }

    private void clearLabels() {
        lblImeError.setText("");
        lblPrezimeError.setText("");
        lblKorisnickoImeError.setText("");
        lblLozinkaError.setText("");
        lblPotvrdaError.setText("");
    }

}

