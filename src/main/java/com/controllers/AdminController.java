package com.controllers;

import java.io.IOException;
import java.util.Optional;

import com.dao.ObavjestenjeDAO;
import com.dao.ObjekatDAO;
import com.model.Admin;
import com.model.Obavjestenje;
import com.model.Objekat;
import com.model.StanjeObjekta;
import com.util.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;

public class AdminController {

    private static Admin trenutniAdmin;
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
        String tekst = "Objekat je odobren!";
        Objekat objekat = lvObjekti.getSelectionModel().getSelectedItem();
        if (objekat == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Izaberite objekat...");
            alert.setContentText("Izaberite objekat koji treba da odobrite!");
            alert.showAndWait();
            return;
        }
        objekat.setStatus(StanjeObjekta.ODOBREN);

        Obavjestenje obavjestenje = new Obavjestenje(0, objekat, tekst);
        ObjekatDAO objekatDAO = new ObjekatDAO();
        ObavjestenjeDAO obavjestenjeDAO = new ObavjestenjeDAO();
        objekatDAO.updateObjekat(objekat, objekat.getNaziv());
        obavjestenjeDAO.createObavjestenje(obavjestenje);
        lvObjekti.getItems().remove(objekat);

        Alert alert = new Alert(AlertType.INFORMATION, "Objekat odobren!");
        alert.show();

        System.out.println(tekst);
    }

    @FXML
    private void handleOdbij() {
        String tekst = "Objekat je odbijen!\n";
        Objekat objekat = lvObjekti.getSelectionModel().getSelectedItem();
        if (objekat == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Izaberite objekat...");
            alert.setContentText("Izaberite objekat koji treba da odobrite!");
            alert.showAndWait();
            return;
        }

        TextInputDialog tidObavjestenje = new TextInputDialog();
        tidObavjestenje.setTitle("Obbijanje objekta");
        tidObavjestenje.setHeaderText("Navedite razlog odbijanja: ");
        tidObavjestenje.setContentText("Razlog: ");
        tidObavjestenje.setWidth(200);
        Optional<String> res = tidObavjestenje.showAndWait();

        if (res.isEmpty() || res.get().trim().isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Greska!");
            alert.setContentText("Unesite razlog!");
            alert.setContentText("Morate unijeti razlog odbijanja objekta!!!");
            alert.showAndWait();
            return;
        }

        objekat.setStatus(StanjeObjekta.ODBIJEN);
        Obavjestenje obavjestenje = new Obavjestenje(0, objekat, tekst + res.get().trim());
        ObjekatDAO objekatDAO = new ObjekatDAO();
        ObavjestenjeDAO obavjestenjeDAO = new ObavjestenjeDAO();
        objekatDAO.updateObjekat(objekat, objekat.getNaziv());
        obavjestenjeDAO.createObavjestenje(obavjestenje);
        lvObjekti.getItems().remove(objekat);

        System.out.println(tekst + res.get().trim());

    }

    private void setAllUserData() {
        trenutniAdmin = (Admin) SceneManager.getKorisnik();
        lblIme.setText(trenutniAdmin.getIme());
        lblPrezime.setText(trenutniAdmin.getPrezime());
        lblKorisnickoIme.setText(trenutniAdmin.getKorisnicko_ime());
        objektiNaCekanju();
    }

    private void objektiNaCekanju() {
        for (Objekat objekat : Objekat.getSviObjekti()) {
            if (objekat.getStatus().equals(StanjeObjekta.NA_CEKANJU)) {
                lvObjekti.getItems().add(objekat);
            }
        }
    }

}
