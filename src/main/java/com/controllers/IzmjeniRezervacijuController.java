package com.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.dao.BankovniRacunDAO;
import com.dao.ObjekatDAO;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;

public class IzmjeniRezervacijuController {

    static Proslava trenutnaProslava;

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

    @FXML ComboBox<StoWrapper> cbSto;
    @FXML ComboBox<MeniWrapper> cbMeni;
    @FXML TextArea taGosti;

    static List<Sto> stolovi = new ArrayList<>();
    static List<Meni> meniji = new ArrayList<>();
    static List<Raspored> rasporedi;
    
    @FXML
    private void initialize() {
        setAllUserData();
    }

    @FXML
    private void handleNazad() {
        try {
            trenutniObjekat = null;
            trenutnaProslava = null;
            rasporedi = null;
            SceneManager.showKlijentScene(trenutniKlijent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDodajRezervaciju() {
        Alert alert = new Alert(AlertType.WARNING);

        int broj_gostiju = 0;
        for (Raspored raspored : rasporedi) {
            broj_gostiju += raspored.getGosti().size();
        }

        Meni meni = cbMeni.getValue().getMeni();
        if (meni == null) {
            broj_gostiju = 0;
            alert.setTitle("Kreiranje proslave neuspjesno!");
            alert.setHeaderText("Polako velmozo!");
            alert.setContentText("Da li ste unijeli meni?!");
            alert.showAndWait();
            return;
        } 

        double ostatak_uplate = broj_gostiju * meni.getCijena_po_osobi();
        double ukupna_cijena = trenutnaProslava.getUplacen_iznos() 
            + ostatak_uplate;


        trenutnaProslava.setMeni(meni);
        trenutnaProslava.setUkupna_cijena(ukupna_cijena);
        trenutnaProslava.setUplacen_iznos(ostatak_uplate);

        trenutniObjekat.setZarada(trenutniObjekat.getZarada() + ostatak_uplate);
        ObjekatDAO objekatDAO = new ObjekatDAO();
        objekatDAO.updateObjekat(trenutniObjekat, trenutniObjekat.getNaziv());

        ProslavaDAO proslavaDAO = new ProslavaDAO();
        proslavaDAO.updateProslava(trenutnaProslava,
            trenutnaProslava.getId());


        RasporedDAO rasporedDAO = new RasporedDAO();
        for (Raspored raspored : rasporedi) {
            raspored.setProslava(trenutnaProslava);
            rasporedDAO.createRaspored(raspored);
        }

        oduzmiNovacKlijentu(ostatak_uplate);
        dodajNovacVlasniku(ostatak_uplate);

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
        Alert alert = new Alert(AlertType.WARNING);

        if (selectedSto != null) {
            String[] temp = taGosti.getText().split("\n");
            if (temp.length > selectedSto.getSto().getBroj_mjesta()) {
                alert.setTitle("Upozorenje!");
                alert.setContentText("Broj mjesta za goste je manji od unesenog broja gostiju!");
                alert.showAndWait();
                return;
            }

            for (String gost : temp) {
                gosti.add(gost.trim());
            }
        }

        for (Raspored raspored : rasporedi) {
            if (raspored.getSto().getId() == selectedSto.getSto().getId()) {
                alert.setTitle("Upozorenje!");
                alert.setContentText(selectedSto + " je vec zauzet, probajte opciju 'Izmjeni raspored'...");
                alert.showAndWait();
                return;
            }
        }

        Raspored raspored = new Raspored(selectedSto.getSto(), trenutnaProslava, gosti);
        rasporedi.add(raspored);
        RasporedDAO dao = new RasporedDAO();
        dao.createRaspored(raspored);
    }

    @FXML
    private void handleIzmjeniRaspored() {
        List<String> gosti = new ArrayList<>();
        StoWrapper selectedSto = cbSto.getValue();
        Alert alert = new Alert(AlertType.WARNING);

        if (selectedSto != null) {
            String[] temp = taGosti.getText().split("\n");
            if (temp.length > selectedSto.getSto().getBroj_mjesta()) {
                alert.setTitle("Upozorenje!");
                alert.setContentText("Broj mjesta za goste je manji od unesenog broja gostiju!");
                return;
            }

            for (String gost : temp) {
                gosti.add(gost.trim());
            }
        }

        Raspored raspored = new Raspored(selectedSto.getSto(), trenutnaProslava, gosti);
        rasporedi.add(raspored);
        RasporedDAO dao = new RasporedDAO();
        dao.updateRaspored(raspored, raspored.getSto().getId());
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
        lblNaziv.setText(trenutniObjekat.getNaziv());
        lblGrad.setText(trenutniObjekat.getGrad());
        lblAdresa.setText(trenutniObjekat.getAdresa());
        lblCijenaRezervacije.setText(Double.toString(trenutniObjekat.getCijena_rezervacije()));
        lblBrojMjesta.setText(Integer.toString(trenutniObjekat.getBroj_mjesta()));
        lblBrojStolova.setText(Integer.toString(trenutniObjekat.getBroj_stolova()));
        taGosti.setPromptText("Unesite imena gostiju jedne ispod drugih...");
        setStolovi();
        setMeniji();
        getRasporedi();
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

    private void getRasporedi() {
        rasporedi = new ArrayList<>();
        for (Raspored raspored : Raspored.getSviRasporedi()) {
            if (raspored.getProslava().getId() == trenutnaProslava.getId()) {
                rasporedi.add(raspored);
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
