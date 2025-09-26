package com.controllers;

import java.io.IOException;
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
import com.model.StanjeObjekta;
import com.model.Sto;
import com.model.Vlasnik;
import com.util.MeniWrapper;
import com.util.SceneManager;
import com.util.StoWrapper;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ObjekatController {

    private static Vlasnik trenutniVlasnik;
    private static Objekat trenutniObjekat;

    private static int ukupanBrojMjesta = 0;
    private static List<StoWrapper> stolovi = new ArrayList<>();
    private static List<MeniWrapper> meniji  = new ArrayList<>();

    @FXML Label lblIme;
    @FXML Label lblPrezime;
    @FXML Label lblKorisnickoIme;
    @FXML Label lblStanje;
    
    @FXML TextField tfNaziv;
    @FXML TextField tfCijenaRezervacije;
    @FXML TextField tfGrad;
    @FXML TextField tfAdresa;
    @FXML TextField tfBrojMjesta;
    @FXML TextField tfStoBrojMjesta;
    @FXML TextField tfCijenaMenija;
    @FXML TextArea  taOpisMenija;
    @FXML ListView<StoWrapper> lvStolovi;
    @FXML ListView<MeniWrapper> lvMeniji;

    @FXML
    private void initialize() {
        setAllUserData();
        if (trenutniObjekat.getNaziv() != null) {
            fixObjekat();
        }
    }

    @FXML
    private void handleDodajSto() {
        int brojMjesta;
        try {
            brojMjesta = Integer.parseInt(tfStoBrojMjesta.getText());
        } catch (NumberFormatException e) {
            System.err.println("Unesena vrijendnost nije cijeli broj!");
            tfStoBrojMjesta.setText("");
            return;
        }

        StoWrapper sto = new StoWrapper(
            stolovi.size() + 1, new Sto(0, trenutniObjekat, brojMjesta));
        
        stolovi.add(sto);
        refreshStolovi();
    }

    @FXML
    private void handleIzbaciSto() {
        int index = lvStolovi.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            stolovi.remove(index);
            refreshStolovi();
        }
    }

    private void refreshStolovi() {
        lvStolovi.getItems().clear();
        ukupanBrojMjesta = 0;
        for (int i = 0; i < stolovi.size(); i++) {
            StoWrapper sto = stolovi.get(i);
            sto.setId(i+1);
            lvStolovi.getItems().add(sto);
            ukupanBrojMjesta += sto.getSto().getBroj_mjesta();
        }
    }

    @FXML
    private void handleDodajMeni() {
        double cijenaMenija;
        try {
            cijenaMenija = Double.parseDouble(tfCijenaMenija.getText());
        } catch (NumberFormatException e) {
            System.err.println("Unesena vrijendnost nije broj!");
            tfCijenaMenija.setText("");
            return;
        }
        String opisMenija = taOpisMenija.getText();

        if (opisMenija == "") {
            return;
        }

        Meni m = new Meni(0, trenutniObjekat, opisMenija, cijenaMenija);
        MeniWrapper meni = new MeniWrapper(meniji.size() + 1, m);
        meniji.add(meni);
        taOpisMenija.clear();
        refreshMeniji();
    }

    @FXML
    private void handleIzbaciMeni() {
        int index = lvMeniji.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            meniji.remove(index);
            refreshMeniji();
        }
    }

    private void refreshMeniji() {
        lvMeniji.getItems().clear();
        for (int i = 0; i < meniji.size(); i++) {
            MeniWrapper mw = meniji.get(i);
            mw.setId(i+1);
            lvMeniji.getItems().add(mw);
        }
    }

    @FXML
    private void handleNazad() {
        try {
            meniji = null;
            stolovi = null;
            trenutniObjekat = null;
            SceneManager.showVlasnikScene(trenutniVlasnik);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDodajObjekat() {
        String naziv = tfNaziv.getText();
        String cijenaRezervacije = tfCijenaRezervacije.getText();
        String grad = tfGrad.getText();
        String adresa = tfAdresa.getText();
        String brojMjesta = tfBrojMjesta.getText();
        Alert alert = new Alert(AlertType.WARNING);
        
        if (naziv == "" || cijenaRezervacije == "" || grad == "" ||
            adresa == "" || brojMjesta == "") {
            alert.setTitle("Kreiranje objekta neuspjesno!");
            alert.setHeaderText("Polako velmozo!");
            alert.setContentText("Da li ste unijeli sve informacije?!");
            alert.showAndWait();
            return;
        }

        if (Integer.parseInt(brojMjesta) < ukupanBrojMjesta) {
            alert.setTitle("Kreiranje objekta neuspjesno!");
            alert.setHeaderText("Malo li je, velmozo?!");
            alert.setContentText("Zadati broj mjesta: " + brojMjesta + "\n" +
                "Ukupan broj po stolicama: " + ukupanBrojMjesta);
            alert.showAndWait();
            return;
        }

        if (stolovi.size() == 0) {
            alert.setTitle("Kreiranje objekta neuspjesno!");
            alert.setHeaderText("Pa kako to, velmozo?!");
            alert.setContentText("Unesite stolove!");
            alert.showAndWait();
            return;
        }
        
        if (meniji.size() == 0) {
            alert.setTitle("Kreiranje objekta neuspjesno!");
            alert.setHeaderText("Pa kako to, velmozo?!");
            alert.setContentText("Unesite stolove!");
            alert.showAndWait();
            return;
        }

        Objekat o = new Objekat(
            0, 
            trenutniVlasnik, 
            naziv, 
            Double.parseDouble(cijenaRezervacije), 
            grad, 
            adresa, 
            Integer.parseInt(brojMjesta),
            stolovi.size(), 
            new ArrayList<>(),
            0, 
            StanjeObjekta.NA_CEKANJU
        );

        ObjekatDAO dao = new ObjekatDAO();
        dao.createObjekat(o);

        StoDAO stoDAO = new StoDAO();
        for (StoWrapper sto : stolovi) {
            sto.getSto().setObjekat(o);
            stoDAO.createSto(sto.getSto());
        }
        stolovi = null;

        MeniDAO meniDAO = new MeniDAO();
        for (MeniWrapper meni : meniji) {
            meni.getMeni().setObjekat(o);
            meniDAO.createMeni(meni.getMeni());
        }
        meniji = null;
        clearInput();

        try {
            SceneManager.showVlasnikScene(trenutniVlasnik);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setAllUserData() {
        trenutniVlasnik = (Vlasnik) SceneManager.getKorisnik();
        if (SceneManager.getObjekat() == null) {
            trenutniObjekat = new Objekat();
        } else {
            trenutniObjekat = SceneManager.getObjekat();
        }
        lblIme.setText(trenutniVlasnik.getIme());
        lblPrezime.setText(trenutniVlasnik.getPrezime());
        lblKorisnickoIme.setText(trenutniVlasnik.getKorisnicko_ime());
        BankovniRacun racun = BankovniRacun.getByBrojRacuna(trenutniVlasnik.getBroj_racuna());

        lblStanje.setText(""+racun.getStanje());
        tfNaziv.setUserData("");
        tfCijenaRezervacije.setUserData("");
        tfGrad.setUserData("");
        tfAdresa.setUserData("");
        tfBrojMjesta.setUserData("");
        tfStoBrojMjesta.setUserData("");
        tfCijenaMenija.setUserData("");
        taOpisMenija.clear();
        taOpisMenija.setWrapText(true);
        stolovi = new ArrayList<>();
        meniji = new ArrayList<>();
    }

    private void fixObjekat() {
        tfNaziv.setText(trenutniObjekat.getNaziv());
        tfCijenaRezervacije.setText(
            Double.toString(trenutniObjekat.getCijena_rezervacije()));
        tfGrad.setText(trenutniObjekat.getGrad());
        tfAdresa.setText(trenutniObjekat.getAdresa());
        tfBrojMjesta.setText(Integer.toString(trenutniObjekat.getBroj_mjesta()));
        Obavjestenje obavjestenje = Obavjestenje.getByObjekatId(trenutniObjekat.getId());
        ObavjestenjeDAO obavjestenjeDAO = new ObavjestenjeDAO();
        obavjestenjeDAO.removeObavjestenje(obavjestenje.getId());
        ObjekatDAO objekatDAO = new ObjekatDAO();
        objekatDAO.removeObjekat(trenutniObjekat.getNaziv());
    }

    private void clearInput() {
        tfNaziv.clear();
        tfCijenaRezervacije.clear();
        tfGrad.clear();
        tfAdresa.clear();
        tfBrojMjesta.clear();
        tfCijenaMenija.clear();
        tfStoBrojMjesta.clear();
        taOpisMenija.clear();
        lvMeniji.getItems().clear();
        lvStolovi.getItems().clear();
    }
}
