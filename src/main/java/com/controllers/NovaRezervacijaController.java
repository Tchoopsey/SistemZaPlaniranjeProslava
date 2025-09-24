package com.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dao.BankovniRacunDAO;
import com.dao.ObjekatDAO;
import com.dao.ProslavaDAO;
import com.model.BankovniRacun;
import com.model.Klijent;
import com.model.Meni;
import com.model.Objekat;
import com.model.Proslava;
import com.model.Raspored;
import com.model.Sto;
import com.util.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class NovaRezervacijaController {

    static Klijent trenutniKlijent;
    @FXML Label lblIme;
    @FXML Label lblPrezime;
    @FXML Label lblKorisnickoIme;
    @FXML Label lblStanje;

    static Objekat trenutniObjekat;
    @FXML Label lblNaziv;
    @FXML Label lblGrad;
    @FXML Label lblAdresa;
    @FXML Label lblCijenaRezervacije;
    @FXML Label lblBrojMjesta;
    @FXML Label lblBrojStolova;

    @FXML TextField tfDatum;
    @FXML DatePicker dpDatum;

    static List<Sto> stolovi = new ArrayList<>();
    static List<Meni> meniji = new ArrayList<>();
    static List<Raspored> rasporedi = new ArrayList<>();
    
    @FXML
    private void initialize() {
        setAllUserData();
        setFormat();
        setUsedDates();
    }

    @FXML
    private void handleNazad() {
        try {
            trenutniObjekat = null;
            SceneManager.showKlijentScene(trenutniKlijent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDodajRezervaciju() {
        double uplacen_iznos = Double.parseDouble(lblCijenaRezervacije.getText());

        LocalDate datum = dpDatum.getValue();
        Alert alert = new Alert(AlertType.WARNING);
        if (datum == null) {
            alert.setTitle("Kreiranje proslave neuspjesno!");
            alert.setHeaderText("Polako velmozo!");
            alert.setContentText("Da li ste unijeli datum?!");
            alert.showAndWait();
            return;
        }

        Optional<String> res = unesiLozinku();

        if (res.isEmpty() || res.get().trim().isEmpty()) {
            alert.setTitle("Greska!");
            alert.setContentText("Unesite lozinku!");
            alert.setContentText("Morate unijeti lozinku da biste potvrdili rezervaciju!!!");
            alert.showAndWait();
            return;
        }

        if (!res.get().trim().equals(trenutniKlijent.getPassword())) {
            alert.setTitle("Greska!");
            alert.setContentText("Pogresna lozinka!");
            alert.setContentText("Morate unijeti ispravnu lozinku da biste potvrdili rezervaciju!!!");
            alert.showAndWait();
            return;
        }

        Proslava proslava = new Proslava(
            0, 
            trenutniObjekat, 
            trenutniKlijent, 
            null, 
            "Trenutna",
            datum, 
            0, 
            uplacen_iznos, 
            uplacen_iznos
        );

        ProslavaDAO proslavaDAO = new ProslavaDAO();
        proslavaDAO.createProslava(proslava);

        List<String> datumi = trenutniObjekat.getDatumi();
        datumi.add(datum.toString());
        trenutniObjekat.setDatumi(datumi);
        trenutniObjekat.setZarada(trenutniObjekat.getZarada() + uplacen_iznos);
        ObjekatDAO objekatDAO = new ObjekatDAO();
        objekatDAO.updateObjekat(trenutniObjekat, trenutniObjekat.getNaziv());

        oduzmiNovacKlijentu(uplacen_iznos);
        dodajNovacVlasniku(uplacen_iznos);

        try {
            trenutniObjekat = null;
            SceneManager.showKlijentScene(trenutniKlijent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Optional<String> unesiLozinku() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Potvrda Rezervacije");
        dialog.setHeaderText("Potvrdite rezervaciju lozinkom!");
        dialog.setContentText("Lozinka: ");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        PasswordField pfLozinka = new PasswordField();

        VBox vBox = new VBox(10);
        vBox.getChildren().add(pfLozinka);
        dialog.getDialogPane().setContent(vBox);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return pfLozinka.getText();
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void dodajNovacVlasniku(double uplacen_iznos) {
        String broj_racuna = trenutniObjekat.getVlasnik().getBroj_racuna();

        for (BankovniRacun racun : BankovniRacun.getRacuni()) {
            if (racun.getBroj_racuna().equals(broj_racuna)) {
                racun.setStanje(racun.getStanje() + uplacen_iznos);
                BankovniRacunDAO brDao = new BankovniRacunDAO();
                brDao.updateBankovniRacun(racun);
                return;
            } 
        }

    }

    private void oduzmiNovacKlijentu(double uplacen_iznos) {
        String broj_racuna = trenutniKlijent.getBroj_racuna();

        for (BankovniRacun racun : BankovniRacun.getRacuni()) {
            if (racun.getBroj_racuna().equals(broj_racuna)) {
                racun.setStanje(racun.getStanje() - uplacen_iznos);
                BankovniRacunDAO brDao = new BankovniRacunDAO();
                brDao.updateBankovniRacun(racun);
                return;
            } 
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
        List<LocalDate> zauzetiDatumi = trenutniObjekat.getDatumi()
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
                    setStyle("-fx-background-color: lightgray; -fx-text-fill: white;");
                    if (zauzetiDatumi.contains(datum)) {
                        setStyle("-fx-background-color: gray; -fx-text-fill: white;");
                    }
                } else if (zauzetiDatumi.contains(datum)) {
                    setDisable(true);
                    setStyle("-fx-background-color: red; -fx-text-fill: white;");
                } else {
                    setStyle("");
                    setTooltip(null);
                }
            };
        });
    }

    private void setAllUserData() {
        trenutniKlijent = (Klijent) SceneManager.getKorisnik();
        trenutniObjekat = (Objekat) SceneManager.getObjekat();
        lblIme.setText(trenutniKlijent.getIme());
        lblPrezime.setText(trenutniKlijent.getPrezime());
        lblKorisnickoIme.setText(trenutniKlijent.getKorisnicko_ime());
        BankovniRacun racun = BankovniRacun.getByBrojRacuna(trenutniKlijent.getBroj_racuna());
        lblStanje.setText(""+racun.getStanje());
        lblNaziv.setText(trenutniObjekat.getNaziv());
        lblGrad.setText(trenutniObjekat.getGrad());
        lblAdresa.setText(trenutniObjekat.getAdresa());
        lblCijenaRezervacije.setText(Double.toString(trenutniObjekat.getCijena_rezervacije()));
        lblBrojMjesta.setText(Integer.toString(trenutniObjekat.getBroj_mjesta()));
        lblBrojStolova.setText(Integer.toString(trenutniObjekat.getBroj_stolova()));
        dpDatum.setEditable(false);
    }

}
