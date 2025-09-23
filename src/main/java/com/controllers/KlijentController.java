package com.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.model.BankovniRacun;
import com.model.Klijent;
import com.model.Objekat;
import com.model.Proslava;
import com.model.StanjeObjekta;
import com.util.SceneManager;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class KlijentController {

    static Klijent trenutniKlijent;
    static List<Objekat> objekti;
    ObservableList<Objekat> obsObjekti;
    @FXML Label lblIme;
    @FXML Label lblPrezime;
    @FXML Label lblKorisnickoIme;
    @FXML Label lblStanje;
    @FXML ListView<Objekat> lvObjekti;
    @FXML ListView<Proslava> lvProslave;
    @FXML TextField tfBrojMjesta;
    @FXML TextField tfDatum;
    @FXML TextField tfGrad;

    @FXML
    private void initialize() {
        setAllUserData();
        filterObjekti(obsObjekti, lvObjekti);
        getRezervacije();
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
        Proslava proslava = lvProslave.getSelectionModel().getSelectedItem();
        try {
            SceneManager.showIzmjeniRezervacijuScene(trenutniKlijent, proslava);
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
        lvProslave.setEditable(false);
        lblIme.setText(trenutniKlijent.getIme());
        lblPrezime.setText(trenutniKlijent.getPrezime());
        lblKorisnickoIme.setText(trenutniKlijent.getKorisnicko_ime());
        BankovniRacun racun = BankovniRacun.getByBrojRacuna(trenutniKlijent.getBroj_racuna());
        lblStanje.setText(Double.toString(racun.getStanje()));
    }

    private void getObjekti() {
        objekti = new ArrayList<>();
        for (Objekat objekat : Objekat.getSviObjekti()) {
            if (objekat.getStatus() == StanjeObjekta.ODOBREN) {
                objekti.add(objekat);
            }
        }
    }

    private void filterObjekti(
        ObservableList<Objekat> obsObjekti,
        ListView<Objekat> lvObjekti
    ) {
        getObjekti();
        obsObjekti = FXCollections.observableArrayList(objekti);
        FilteredList<Objekat> filteredObjekti = new FilteredList<>(obsObjekti, o -> true);
        lvObjekti.setItems(filteredObjekti);
        ChangeListener<String> objektiListener = (obs, oldVal, newVal) -> {
            filteredObjekti.setPredicate(objekat -> {
                String brojMjesta = tfBrojMjesta.getText();
                String datum = tfDatum.getText();
                String grad = tfGrad.getText().toLowerCase();

                boolean matchBrojMjesta;
                try {
                    matchBrojMjesta = brojMjesta.isEmpty() ||
                        objekat.getBroj_mjesta() <= Integer.parseInt(tfBrojMjesta.getText());
                } catch (NumberFormatException e) {
                    matchBrojMjesta = true;
                }

                boolean matchDatum = datum.isEmpty() ||
                    !objekat.getDatumi().contains(datum);
                boolean matchGrad = grad.isEmpty() ||
                    objekat.getGrad().toLowerCase().contains(grad);

                return matchBrojMjesta && matchDatum && matchGrad;
            });
        };

        tfBrojMjesta.textProperty().addListener(objektiListener);
        tfDatum.textProperty().addListener(objektiListener);
        tfGrad.textProperty().addListener(objektiListener);
    }

    private void getRezervacije() {
        for (Proslava proslava : Proslava.getSveProslave()) {
            if (proslava.getKlijent().getId() == trenutniKlijent.getId()) {
                System.out.println(proslava);
                lvProslave.getItems().add(proslava);
            }
        }
    }
}
