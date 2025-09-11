package com.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.dao.MeniDAO;
import com.dao.ObjekatDAO;
import com.dao.StoDAO;
import com.model.BankovniRacun;
import com.model.Meni;
import com.model.Objekat;
import com.model.StanjeObjekta;
import com.model.Sto;
import com.model.Vlasnik;
import com.util.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ObjekatController {

    private static Vlasnik trenutniVlasnik;
    private static int ukupanBrojMjesta = 0;
    private static List<String> stolovi = new ArrayList<>();
    private static List<String> meniji  = new ArrayList<>();

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
    @FXML ListView<String> lvStolovi;
    @FXML ListView<String> lvMeniji;
    @FXML Button btnDodajSto;
    @FXML Button btnIzbaciSto;
    @FXML Button btnDodajMeni;
    @FXML Button btnIzbaciMeni;
    @FXML Button btnDodajObjekat;
    @FXML Button btnNazad;

    @FXML
    private void initialize() {
        trenutniVlasnik = (Vlasnik) SceneManager.getKorisnik();
        setAllUserData();
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
        
        String sto = "Sto " + (stolovi.size() + 1) + ": " + brojMjesta;
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
            String[] dijelovi = stolovi.get(i).split(":");
            int brojMjesta = Integer.parseInt(dijelovi[1].trim());
            lvStolovi.getItems().add("Sto " + (i + 1) + " : " + brojMjesta);
            ukupanBrojMjesta += brojMjesta;
        }
    }

    @FXML
    private void handleDodajMeni() {
        int cijenaMenija;
        try {
            cijenaMenija = Integer.parseInt(tfCijenaMenija.getText());
        } catch (NumberFormatException e) {
            System.err.println("Unesena vrijendnost nije cijeli broj!");
            tfCijenaMenija.setText("");
            taOpisMenija.clear();
            return;
        }
        String opisMenija = taOpisMenija.getText();

        if (opisMenija == "") {
            return;
        }

        String meni = "Meni " + (meniji.size() + 1) + ": " + cijenaMenija + 
            ": \n" + opisMenija;
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
            String[] dijelovi = meniji.get(i).split(":");
            int cijenaMenija = Integer.parseInt(dijelovi[1].trim());
            String opisMenija = dijelovi[2].trim();
            lvMeniji.getItems().add("Meni " + (i + 1) + " : " + cijenaMenija +
                ": \n" + opisMenija);
        }
    }

    @FXML
    private void handleNazad() {
        try {
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
            "", 
            0, 
            StanjeObjekta.NA_CEKANJU
        );

        ObjekatDAO dao = new ObjekatDAO();
        dao.createObjekat(o);

        StoDAO stoDAO = new StoDAO();
        for (String sto : stolovi) {
            String[] dijelovi = sto.split(":");
            Sto s = new Sto(0, o, Integer.parseInt(dijelovi[1].trim()));
            stoDAO.createSto(s);
        }

        MeniDAO meniDAO = new MeniDAO();
        for (String meni : meniji) {
            String[] dijelovi = meni.split(":");
            Meni m = new Meni(0, o, dijelovi[2].trim(), Double.parseDouble(dijelovi[1].trim()));
            meniDAO.createMeni(m);
        }

        clearInput();
    }
    
    private void setAllUserData() {
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
    }
}
