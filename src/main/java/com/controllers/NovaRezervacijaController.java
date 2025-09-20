package com.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.dao.BankovniRacunDAO;
import com.dao.ProslavaDAO;
import com.dao.RasporedDAO;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
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
    @FXML TextField tfBrojGostiju;
    @FXML ComboBox<StoWrapper> cbSto;
    @FXML ComboBox<MeniWrapper> cbMeni;
    @FXML DatePicker dpDatum;
    @FXML TextArea taGosti;

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
            rasporedi = new ArrayList<>();
            SceneManager.showKlijentScene(trenutniKlijent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDodajRezervaciju() {
        int broj_gostiju;
        try {
            broj_gostiju = Integer.parseInt(tfBrojGostiju.getText());
        } catch (NumberFormatException e) {
            broj_gostiju = 0;
        }

        Meni meni;
        double uplacen_iznos = Double.parseDouble(lblCijenaRezervacije.getText());
        double ukupna_cijena;
        try {
            meni = cbMeni.getValue().getMeni();
            ukupna_cijena = 
                uplacen_iznos + (broj_gostiju * meni.getCijena_po_osobi());
        } catch (NullPointerException e) {
            meni = null;
            ukupna_cijena = uplacen_iznos;
        }

        LocalDate datum = dpDatum.getValue();
        Alert alert = new Alert(AlertType.WARNING);

        try {
            datum = dpDatum.getValue();
            System.out.println(datum);
        } catch (NullPointerException e) {
            alert.setTitle("Kreiranje proslave neuspjesno!");
            alert.setHeaderText("Polako velmozo!");
            alert.setContentText("Da li ste unijeli datum?!");
            alert.showAndWait();
            return;
        }

        Proslava proslava = new Proslava(
            0, 
            trenutniObjekat, 
            trenutniKlijent, 
            meni, 
            datum, 
            broj_gostiju, 
            ukupna_cijena, 
            uplacen_iznos
        );

        ProslavaDAO proslavaDAO = new ProslavaDAO();
        proslavaDAO.createProslava(proslava);


        RasporedDAO rasporedDAO = new RasporedDAO();
        for (Raspored raspored : rasporedi) {
            raspored.setProslava(proslava);
            rasporedDAO.createRaspored(raspored);
        }

        oduzmiNovacKlijentu(uplacen_iznos);
        dodajNovacVlasniku(uplacen_iznos);

        setAllUserData();

        taGosti.clear();
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
    private void handleSelectedSto() {
        StoWrapper selected = cbSto.getValue();
        if (selected == null) {
            taGosti.clear();
            return;
        }

        rasporedi.stream()
            .filter(r -> r.getSto().equals(selected.getSto()))
            .findFirst()
            .ifPresentOrElse(
                raspored -> {
                    String gosti = String.join("\n", raspored.getGosti());
                    taGosti.setText(gosti);
                }, 
                () -> taGosti.clear()
            );
    }

    @FXML
    private void handleDodajRaspored() {
        List<String> gosti = new ArrayList<>();
        StoWrapper selectedSto = cbSto.getValue();

        if (selectedSto != null) {
            String[] temp = taGosti.getText().split("\n");
            if (temp.length > selectedSto.getSto().getBroj_mjesta()) {
                System.out.println("Broj mjesta za goste je manji od unesenog broja gostiju!");
            }

            for (String gost : temp) {
                gosti.add(gost.trim());
            }
        }

        rasporedi.add(new Raspored(0, selectedSto.getSto(), null, gosti));
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
        lblNaziv.setText(trenutniObjekat.getNaziv());
        lblGrad.setText(trenutniObjekat.getGrad());
        lblAdresa.setText(trenutniObjekat.getAdresa());
        lblCijenaRezervacije.setText(Double.toString(trenutniObjekat.getCijena_rezervacije()));
        lblBrojMjesta.setText(Integer.toString(trenutniObjekat.getBroj_mjesta()));
        lblBrojStolova.setText(Integer.toString(trenutniObjekat.getBroj_stolova()));
        tfBrojGostiju.setUserData("");
        taGosti.setPromptText("Unesite imena gostiju jedne ispod drugih...");
        dpDatum.setEditable(false);
        // dpDatum.getEditor().setText("");
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
        return "Sto " + id + ": " + sto.getBroj_mjesta() + " mjesta";
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
