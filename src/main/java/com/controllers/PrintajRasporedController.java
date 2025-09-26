package com.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.model.BankovniRacun;
import com.model.Klijent;
import com.model.Objekat;
import com.model.Osoba;
import com.model.Proslava;
import com.model.Raspored;
import com.model.Sto;
import com.util.KomparatorOsoba;
import com.util.OsobaWrapper;
import com.util.SceneManager;
import com.util.StoWrapper;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class PrintajRasporedController {

    Klijent trenutniKlijent;
    Proslava trenutnaProslava;
    Objekat trenutniObjekat;
    List<Raspored> rasporedi;
    List<StoWrapper> stolovi;
    List<OsobaWrapper> osobe;

    @FXML TextArea taRasporedi;
    @FXML Label lblIme;
    @FXML Label lblPrezime;
    @FXML Label lblKorisnickoIme;
    @FXML Label lblStanje;


    @FXML
    private void initialize() {
        setAllUserData();
        printaj();
    }

    @FXML
    private void handleNazad() {
        try {
            rasporedi = null;
            SceneManager.showIzmjeniRezervacijuScene(trenutniKlijent, trenutnaProslava);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getRasporedi() {
        rasporedi = new ArrayList<>();
        for (Raspored raspored : Raspored.getSviRasporedi()) {
            if (raspored.getProslava().getId() == trenutnaProslava.getId()) {
                rasporedi.add(raspored);
            }
        }
    }

    private void setStolovi() {
        stolovi = new ArrayList<>();
        int id = 1;
        for (Sto sto : Sto.getSviStolovi()) {
            if (sto.getObjekat() == trenutniObjekat) {
                stolovi.add(new StoWrapper(id, sto));
                id++;
            }
        }
    }

    private void setOsobe() {
        osobe = new ArrayList<>();
        for (Raspored raspored : rasporedi) {
            List<Osoba> fromRasporedi = raspored.getGosti();
            for (StoWrapper sto : stolovi) {
                if (raspored.getSto().getId() == sto.getSto().getId()) {
                    for (Osoba osoba : fromRasporedi) {
                        osobe.add(new OsobaWrapper(osoba, sto));
                    }
                }
            }
        }
    }

    private Optional<String> unesiNacinPrintanja() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Nacin printanja");
        dialog.setHeaderText("Odaberite nacin printanja...");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ToggleGroup tgPrintanje = new ToggleGroup();
        RadioButton rbStolovi  = new RadioButton("Po stolovima");
        rbStolovi.setUserData("Stolovi");
        rbStolovi.setToggleGroup(tgPrintanje);
        RadioButton rbPrezimena = new RadioButton("Po prezimenima");
        rbPrezimena.setUserData("Prezimena");
        rbPrezimena.setToggleGroup(tgPrintanje);
        rbStolovi.setSelected(true);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(rbStolovi, rbPrezimena);
        dialog.getDialogPane().setContent(vBox);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return tgPrintanje.getSelectedToggle().getUserData().toString();
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void setAllUserData() {
        trenutniKlijent = (Klijent) SceneManager.getKorisnik();
        trenutnaProslava = SceneManager.getProslava();
        trenutniObjekat = trenutnaProslava.getObjekat();
        lblIme.setText(trenutniKlijent.getIme());
        lblPrezime.setText(trenutniKlijent.getPrezime());
        lblKorisnickoIme.setText(trenutniKlijent.getKorisnicko_ime());
        BankovniRacun racun = BankovniRacun.getByBrojRacuna(trenutniKlijent.getBroj_racuna());
        lblStanje.setText(Double.toString(racun.getStanje()));
        taRasporedi.setEditable(false);
        setStolovi();
        getRasporedi();
    }

    private void printPoStolovima() {
        String printaniRaspored = "";

        for (StoWrapper sto : stolovi) {
            Raspored raspored = Raspored.getByStoId(sto.getSto().getId());
            if (raspored == null) {
                continue;
            }

            printaniRaspored += sto + "\n";
            List<Osoba> gosti = raspored.getGosti();

            for (Osoba gost : gosti) {
                printaniRaspored += gost + "\n";
            }

            printaniRaspored += "\n";
        }

        taRasporedi.setText(printaniRaspored);
    }

    private void printPoPrezimenima() {
        setOsobe();
        osobe.sort(new KomparatorOsoba());
        String printaniRaspored = "";
        for (OsobaWrapper osoba : osobe) {
            printaniRaspored += osoba + "\n";
        }

        taRasporedi.setText(printaniRaspored);
    }

    private void printaj() {
        Optional<String> res = unesiNacinPrintanja();
        taRasporedi.clear();

        if (res.isEmpty() || res.get().trim().isEmpty()) {
            try {
                rasporedi = null;
                SceneManager.showIzmjeniRezervacijuScene(trenutniKlijent, trenutnaProslava);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String odabir = res.get().trim();

        if (odabir.equals("Stolovi")) {
            printPoStolovima();
        } else {
            printPoPrezimenima();
        }
    }
}
