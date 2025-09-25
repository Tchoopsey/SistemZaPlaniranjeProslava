package com.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.dao.BankovniRacunDAO;
import com.dao.ObjekatDAO;
import com.dao.ProslavaDAO;
import com.dao.RasporedDAO;
import com.model.BankovniRacun;
import com.model.Klijent;
import com.model.Objekat;
import com.model.Proslava;
import com.model.Raspored;
import com.model.StanjeObjekta;
import com.util.SceneManager;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class KlijentController {

    static Klijent trenutniKlijent;
    static List<Objekat> objekti;
    static List<Proslava> sveProslave;
    ObservableList<Objekat> obsObjekti;
    @FXML Label lblIme;
    @FXML Label lblPrezime;
    @FXML Label lblKorisnickoIme;
    @FXML Label lblStanje;
    @FXML ListView<Objekat> lvObjekti;
    @FXML ListView<Proslava> lvProslave;
    @FXML ListView<Proslava> lvProtekleProslave;
    @FXML ListView<Proslava> lvOtkazaneProslave;
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
    public void handleOtkaziRezervaciju() {
        Proslava proslava = lvProslave.getSelectionModel().getSelectedItem();
        Objekat objekat = proslava.getObjekat();
        LocalDate limit = proslava.getDatum().minusDays(3);

        if (!LocalDate.now().isBefore(limit)) {
            Alert alert = new Alert(AlertType.WARNING, "Proslavu mozete otkazati najkasnije 3 dana prije proslave!");
            alert.showAndWait();
            return;
        }

        List<String> datumi = objekat.getDatumi();
        datumi.remove(proslava.getDatum().toString());
        objekat.setDatumi(datumi);
        ObjekatDAO objekatDAO = new ObjekatDAO();
        objekatDAO.updateObjekat(objekat, objekat.getNaziv());

        RasporedDAO rasporedDAO = new RasporedDAO();
        for (Raspored raspored : new ArrayList<>(Raspored.getSviRasporedi())) {
            rasporedDAO.removeRaspored(proslava.getId());
        }

        if (proslava.getUkupna_cijena() > proslava.getUplacen_iznos()) {
            oduzmiNovacVlasniku(proslava.getUplacen_iznos(), objekat);
            dodajNovacKlijentu(proslava.getUplacen_iznos());
        }

        proslava.setStatus("Otkazana");
        ProslavaDAO proslavaDAO = new ProslavaDAO();
        proslavaDAO.updateProslava(proslava, proslava.getId());

        getRezervacije();
    }


    private void oduzmiNovacVlasniku(double uplacen_iznos, Objekat trenutniObjekat) {
        String broj_racuna = trenutniObjekat.getVlasnik().getBroj_racuna();

        for (BankovniRacun racun : BankovniRacun.getRacuni()) {
            if (racun.getBroj_racuna().equals(broj_racuna)) {
                racun.setStanje(racun.getStanje() - uplacen_iznos);
                BankovniRacunDAO brDao = new BankovniRacunDAO();
                brDao.updateBankovniRacun(racun);
                return;
            } 
        }

    }

    private void dodajNovacKlijentu(double uplacen_iznos) {
        String broj_racuna = trenutniKlijent.getBroj_racuna();

        for (BankovniRacun racun : BankovniRacun.getRacuni()) {
            if (racun.getBroj_racuna().equals(broj_racuna)) {
                racun.setStanje(racun.getStanje() + uplacen_iznos);
                BankovniRacunDAO brDao = new BankovniRacunDAO();
                brDao.updateBankovniRacun(racun);
                return;
            } 
        }
    }

    @FXML
    public void handleIzmjeniRezervaciju() {
        Proslava proslava = lvProslave.getSelectionModel().getSelectedItem();
        if (LocalDate.now().equals(proslava.getDatum().minusDays(3))) {
            Alert alert = new Alert(AlertType.WARNING, "Proslavu mozete otkazati najkasnije 3 dana prije proslave!");
            alert.showAndWait();
            return;
        }
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
        sveProslave = Proslava.getSveProslave();
        lvProslave.getItems().clear();
        lvOtkazaneProslave.getItems().clear();
        lvProtekleProslave.getItems().clear();
        for (Proslava proslava : sveProslave) {
            if (proslava.getKlijent().getId() == trenutniKlijent.getId()) {
                if (proslava.getStatus().equals("Protekla")) {
                    lvProtekleProslave.getItems().add(proslava);
                    continue;
                } else if (proslava.getStatus().equals("Otkazana")) {
                    lvOtkazaneProslave.getItems().add(proslava);
                    continue;
                }
                lvProslave.getItems().add(proslava);
            }
        }
    }
}
