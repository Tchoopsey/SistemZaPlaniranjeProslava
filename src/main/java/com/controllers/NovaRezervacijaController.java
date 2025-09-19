package com.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.model.BankovniRacun;
import com.model.Klijent;
import com.model.Meni;
import com.model.Objekat;
import com.model.Sto;
import com.util.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
    @FXML Label lblCijenaRezervacije;
    @FXML Label lblBrojMjesta;
    @FXML Label lblBrojStolova;

    @FXML TextField tfDatum;
    @FXML TextField tfBrojGostiju;
    @FXML ComboBox<StoWrapper> cbSto;
    @FXML ComboBox<MeniWrapper> cbMeni;
    @FXML DatePicker dpDatum;
    @FXML TextArea taGosti;

    static List<Sto> stolovi = new ArrayList<>();
    static List<Meni> meniji = new ArrayList<>();
    
    @FXML
    private void initialize() {
        setAllUserData();
        setFormat();
        setUsedDates();
    }

    @FXML
    private void handleNazad() {
        try {
            SceneManager.showKlijentScene(trenutniKlijent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDodajRezervaciju() {
        
    }

    @FXML
    private void handleDodajRaspored() {
        List<String> gosti = new ArrayList<>();
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
        lblNaziv.setUserData(trenutniObjekat.getNaziv());
        lblGrad.setUserData(trenutniObjekat.getGrad());
        lblCijenaRezervacije.setUserData(trenutniObjekat.getCijena_rezervacije());
        lblBrojMjesta.setUserData(trenutniObjekat.getBroj_mjesta());
        lblBrojStolova.setUserData(trenutniObjekat.getBroj_stolova());
        tfBrojGostiju.setUserData("");
        tfBrojGostiju.setUserData("");
        setStolovi();
        setMeniji();
    }

    private void setStolovi() {
        int id = 1;
        for (Sto sto : Sto.getSviStolovi()) {
            if (sto.getObjekat() == trenutniObjekat) {
                stolovi.add(sto);
                cbSto.getItems().add(new StoWrapper(id, sto));
                id++;
            }
        }
    }


    private void setMeniji() {
        int id = 1;
        for (Meni meni : Meni.getSviMeniji()) {
            if (meni.getObjekat() == trenutniObjekat) {
                meniji.add(meni);
                cbMeni.getItems().add(new MeniWrapper(id, meni));
                id++;
            }
        }

    }

}

class StoWrapper {
    private int id;
    private Sto sto;

    public StoWrapper(int id, Sto sto) {
        this.id = id;
        this.sto = sto;
    }

    @Override
    public String toString() {
        return "Sto " + id;
    }

    public int getId() {
        return id;
    }

    public Sto getSto() {
        return sto;
    }

}

class MeniWrapper {
    int id;
    Meni meni;

    public MeniWrapper(int id, Meni meni) {
        this.id = id;
        this.meni = meni;
    }

    public int getId() {
        return id;
    }

    public Meni getMeni() {
        return meni;
    }

    @Override
    public String toString() {
        return "Meni " + id + "\n"
            + meni.getCijena_po_osobi() + "KM po osobi\n"
            + meni.getOpis();
    }
}
