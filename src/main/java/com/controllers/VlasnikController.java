package com.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.model.BankovniRacun;
import com.model.Objekat;
import com.model.Vlasnik;
import com.util.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.util.StringConverter;

public class VlasnikController {

    static Vlasnik trenutniVlasnik;
    @FXML Label lblIme;
    @FXML Label lblPrezime;
    @FXML Label lblKorisnickoIme;
    @FXML Label lblStanje;
    @FXML ListView<String> lvObjekti;
    @FXML DatePicker dpDatum;

    @FXML
    private void initialize() {
        trenutniVlasnik = (Vlasnik) SceneManager.getKorisnik();

        // TODO: za svaki objekat posebno uzeti u obzir!!!
        setFormat();
        setUsedDates();

        printObjekti();

        lvObjekti.setEditable(false);
        lblIme.setText(trenutniVlasnik.getIme());
        lblPrezime.setText(trenutniVlasnik.getPrezime());
        lblKorisnickoIme.setText(trenutniVlasnik.getKorisnicko_ime());
        BankovniRacun racun = BankovniRacun.getByBrojRacuna(trenutniVlasnik.getBroj_racuna());
        lblStanje.setText(""+racun.getStanje());
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

    @FXML
    private void setUsedDates() {
        List<LocalDate> takenDates = List.of(
            LocalDate.of(2025, 9, 12), 
            LocalDate.of(2025, 9, 15), 
            LocalDate.of(2025, 9, 20)
        );
        dpDatum.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate datum, boolean empty) {
                super.updateItem(datum, empty);

                if (empty) {
                    setDisable(false);
                    setStyle("");
                    return;
                }

                if (takenDates.contains(datum)) {
                    Tooltip tooltip = new Tooltip("Datum je zauzet!");
                    setTooltip(tooltip);
                    setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white;");
                } else {
                    setStyle("");
                    setTooltip(null);
                }
            };
        });
    }

    @FXML
    private void printObjekti() {
        // for (Objekat objekat : Objekat.getSviObjekti()) {
        //     taObjekti.appendText(objekat.getNaziv());
        // }
        for (int i = 0; i < 6; i++) {
            lvObjekti.getItems().add("Moja mala gace nije prala");
        }
    }
}
