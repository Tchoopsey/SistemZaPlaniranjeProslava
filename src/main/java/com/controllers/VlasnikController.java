package com.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dao.MeniDAO;
import com.dao.ObavjestenjeDAO;
import com.dao.ObjekatDAO;
import com.dao.StoDAO;
import com.dao.VlasnikDAO;
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
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;
import javafx.util.StringConverter;

public class VlasnikController {

    static Vlasnik trenutniVlasnik;
    static Obavjestenje obavjestenje;
    static Proslava trenutnaProslava;
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
        lastSelected();
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
    private void handleIzmjeniLozinku() {
        Optional<String> res = promjenaLozinke();
        Alert alert = new Alert(AlertType.INFORMATION);

        if (res.isEmpty() || res.get().trim().isEmpty()) {
            alert.setAlertType(AlertType.WARNING);
            alert.setHeaderText("GRESKA!");
            alert.setContentText("Nekorektan unos!");
            alert.showAndWait();
            return;
        } else if (res.get().equals("GRESKA1")) {
            alert.setAlertType(AlertType.WARNING);
            alert.setHeaderText("GRESKA!");
            alert.setContentText("Stara lozinka nije ispravna!");
            alert.showAndWait();
            return;
        } else if (res.get().equals("GRESKA2")) {
            alert.setAlertType(AlertType.WARNING);
            alert.setHeaderText("GRESKA!");
            alert.setContentText("Potvrdite novu lozinku!");
            alert.showAndWait();
            return;
        } else if (res.get().equals("GRESKA3")) {
            alert.setAlertType(AlertType.WARNING);
            alert.setHeaderText("GRESKA!");
            alert.setContentText("Unesite novu lozinku!");
            alert.showAndWait();
            return;
        }

        String novaLozinka = res.get().trim();

        trenutniVlasnik.setPassword(novaLozinka);
        VlasnikDAO dao = new VlasnikDAO();
        dao.updateVlasnik(trenutniVlasnik, trenutniVlasnik.getJmbg());

        alert.setHeaderText("Uspjesno ste promjenili lozinku!!!");
        alert.showAndWait();
    }

    private Optional<String> promjenaLozinke() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Promjena Lozinke");
        dialog.setHeaderText("Promjenite lozinku:");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        PasswordField pfStaraLozinka = new PasswordField();
        PasswordField pfNovaLozinka = new PasswordField();
        PasswordField pfPotvrda = new PasswordField();
        Label lblStaraLozinka = new Label("Stara Lozinka:");
        Label lblNovaLozinka = new Label("Nova Lozinka:");
        Label lblPotvrda = new Label("Potvrda Lozinke:");

        GridPane gridPane = new GridPane(3, 3);
        gridPane.setPadding(new Insets(4));
        gridPane.add(lblStaraLozinka, 0, 0);
        gridPane.add(lblNovaLozinka, 0, 1);
        gridPane.add(lblPotvrda, 0, 2);
        gridPane.add(pfStaraLozinka, 1, 0);
        gridPane.add(pfNovaLozinka, 1, 1);
        gridPane.add(pfPotvrda, 1, 2);

        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                if (!pfStaraLozinka.getText().equals(trenutniVlasnik.getPassword())) {
                    return "GRESKA1";
                } else if (!pfNovaLozinka.getText().equals(pfPotvrda.getText())) {
                    return "GRESKA2";
                } else if (pfNovaLozinka.getText().length() == 0) {
                    return "GRESKA3";
                }

                return pfNovaLozinka.getText();
            }
            return null;
        });
        
        return dialog.showAndWait();
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

    @FXML
    private void handlePogledajRezervaciju() {
        if (trenutnaProslava != null) {
            infoRezervacije();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Nema izabrane proslave!");
            alert.showAndWait();
        }
    }

    private void infoRezervacije() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Rezervacija");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);


        VBox vBox = new VBox(10);
        TextArea taRezervacija = new TextArea();
        taRezervacija.setEditable(false);
        String rezervacija = "";
        rezervacija += "Klijent: " + trenutnaProslava.getKlijent() + "\n";
        rezervacija += "Broj gostiju: " + trenutnaProslava.getBroj_gostiju() + "\n";
        rezervacija += "Zarada: " + trenutnaProslava.getUkupna_cijena() + "\n";
        taRezervacija.setText(rezervacija);

        vBox.getChildren().add(taRezervacija);
        dialog.getDialogPane().setContent(vBox);
        
        dialog.showAndWait();
    }

    private void lastSelected() {
        lvProslave.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Proslava>) c -> {
            if (!c.getList().isEmpty()) {
                trenutnaProslava = c.getList().get(0);
            }
        });

        lvProtekleProslave.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Proslava>) c -> {
            if (!c.getList().isEmpty()) {
                trenutnaProslava = c.getList().get(0);
            }
        });

        lvOtkazaneProslave.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Proslava>) c -> {
            if (!c.getList().isEmpty()) {
                trenutnaProslava = c.getList().get(0);
            }
        });
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
