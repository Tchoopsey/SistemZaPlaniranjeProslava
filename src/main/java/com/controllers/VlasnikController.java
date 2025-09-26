package com.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.dao.MeniDAO;
import com.dao.ObavjestenjeDAO;
import com.dao.ObjekatDAO;
import com.dao.StoDAO;
import com.model.BankovniRacun;
import com.model.Meni;
import com.model.Obavjestenje;
import com.model.Objekat;
import com.model.Proslava;
import com.model.StanjeObjekta;
import com.model.Sto;
import com.model.Vlasnik;
import com.util.SceneManager;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.util.StringConverter;

public class VlasnikController {

    static Vlasnik trenutniVlasnik;
    static Obavjestenje obavjestenje;
    @FXML Label lblIme;
    @FXML Label lblPrezime;
    @FXML Label lblKorisnickoIme;
    @FXML Label lblStanje;
    @FXML ComboBox<Objekat> cbObjekti;
    @FXML TextArea taObjekat;
    @FXML DatePicker dpDatum;
    @FXML ListView<Proslava> lvProslave;
    @FXML ListView<Proslava> lvOtkazaneProslave;
    @FXML ListView<Proslava> lvProtekleProslave;

    @FXML
    private void initialize() {
        setAllUserData();
    }

    @FXML
    private void handleLogout() {
        try {
            trenutniVlasnik = null;
            SceneManager.showLoginScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDodajObjekat() {
        try {
            SceneManager.showObjekatScene(trenutniVlasnik, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSelectedObjekat() {
        Objekat selected = cbObjekti.getValue();
        taObjekat.clear();

        if (selected == null) {
            taObjekat.clear();
            return;
        }

        setFormat();
        setZauzetiDatumi();

        String taText = "Naziv: " +  selected.getNaziv() + "\n"
            + "Grad: " +  selected.getGrad() + "\n"
            + "Adresa: " +  selected.getAdresa() + "\n"
            + "Zarada: " +  selected.getZarada() + "\n"
            + "Cijena rezervacije: " +  selected.getCijena_rezervacije() + "\n"
            + "Broj mjesta: " +  selected.getBroj_mjesta() + "\n"
            + "Broj stolova: " +  selected.getBroj_stolova() + "\n";
        taObjekat.setText(taText);

        getRezervacije(selected);
    }

    private void setAllUserData() {
        trenutniVlasnik = (Vlasnik) SceneManager.getKorisnik();
        lblIme.setText(trenutniVlasnik.getIme());
        lblPrezime.setText(trenutniVlasnik.getPrezime());
        lblKorisnickoIme.setText(trenutniVlasnik.getKorisnicko_ime());
        BankovniRacun racun = BankovniRacun.getByBrojRacuna(trenutniVlasnik.getBroj_racuna());
        lblStanje.setText(Double.toString(racun.getStanje()));
        taObjekat.setEditable(false);
        getObjekti();
        Objekat objekat = checkObavjestenja();
        if (objekat != null) {
            Platform.runLater(() -> rjesiOdbijanje(objekat));
        }
    }

    private void rjesiOdbijanje(Objekat objekat) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setHeaderText("Objekat je odbijen!!!");
        alert.setContentText(obavjestenje.getTekst());
        ButtonType fix = new ButtonType("Popravi objekat");
        ButtonType obrisi = new ButtonType("Obrisi objekat");
        alert.getButtonTypes().setAll(fix, obrisi);
        MeniDAO meniDAO = new MeniDAO();
        for (Meni meni : new ArrayList<>(Meni.getSviMeniji())) {
            if (meni.getObjekat().getId() == objekat.getId()) {
                meniDAO.removeMeni(meni.getId());
            }
        }
        StoDAO stoDAO = new StoDAO();
        for (Sto sto : new ArrayList<>(Sto.getSviStolovi())) {
            if (sto.getObjekat().getId() == objekat.getId()) {
                stoDAO.removeSto(sto.getId());
            }
        }

        alert.showAndWait().ifPresent(res -> {
            if (res.equals(fix)) {
                try {
                    SceneManager.showObjekatScene(trenutniVlasnik, objekat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (res.equals(obrisi)) {
                ObavjestenjeDAO obavjestenjeDAO = new ObavjestenjeDAO();
                obavjestenjeDAO.removeObavjestenje(obavjestenje.getId());
                ObjekatDAO objekatDAO = new ObjekatDAO();
                objekatDAO.removeObjekat(objekat.getNaziv());
                cbObjekti.getItems().remove(objekat);
            }
        });
    }

    private Objekat checkObavjestenja() {
        for (Objekat objekat : Objekat.getSviObjekti()) {
            if (objekat.getStatus() == StanjeObjekta.ODBIJEN
                && objekat.getVlasnik().getId() == trenutniVlasnik.getId()) {
                obavjestenje = Obavjestenje.getByObjekatId(objekat.getId());
                return objekat;
            }
        }
        return null;
    }
    
    private void setFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        dpDatum.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate datum) {
                return (datum != null) ? formatter.format(datum) : "";
            };
            
            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(string, formatter);
            }

        });

        dpDatum.setPromptText("dd.MM.yyyy");
    }

    private void setZauzetiDatumi() {
        Objekat objekat = cbObjekti.getValue();
        List<LocalDate> zauzetiDatumi = objekat.getDatumi()
            .stream().filter(s -> s != null && !s.isBlank())
            .map(LocalDate::parse)
            .toList();

        dpDatum.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate datum, boolean empty) {
                super.updateItem(datum, empty);

                if (empty) {
                    setDisable(false);
                    setStyle("");
                    return;
                }

                if (datum.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: lightgray;");
                    if (zauzetiDatumi.contains(datum)) {
                        setStyle("-fx-background-color: darkgray; -fx-text-fill: white;");
                    }
                } else if (zauzetiDatumi.contains(datum)) {
                    setDisable(true);
                    setStyle("-fx-background-color: red; -fx-text-fill: white;");
                    setTooltip(new Tooltip("Datum je zauzet!"));
                } else {
                    setDisable(false);
                    setStyle("");
                    setTooltip(null);
                }
            };
        });
    }

    private void getObjekti() {
        for (Objekat objekat : Objekat.getSviObjekti()) {
            if (trenutniVlasnik.getId() == objekat.getVlasnik().getId()) {
                cbObjekti.getItems().add(objekat);
            }
        }
    }

    private void getRezervacije(Objekat objekat) {
        for (Proslava proslava : Proslava.getSveProslave()) {
            if (proslava.getObjekat().getId() == objekat.getId()) {
                if (proslava.getDatum().isBefore(LocalDate.now())) {
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
