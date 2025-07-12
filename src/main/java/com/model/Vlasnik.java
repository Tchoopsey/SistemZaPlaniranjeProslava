package com.model;

import java.util.ArrayList;
import java.util.List;

import com.dao.VlasnikDAO;

public class Vlasnik extends Korisnik {

    private String jmbg;
    private BankovniRacun broj_racuna;
    private String password;

    private static List<Vlasnik> vlasnici = new ArrayList<>();

    public Vlasnik(String ime, String prezime, String korisnicko_ime, String jmbg, 
        BankovniRacun broj_racuna, String password) {
        super(ime, prezime, korisnicko_ime);
        this.jmbg = jmbg;
        this.broj_racuna = broj_racuna;
        this.password = password;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public BankovniRacun getBroj_racuna() {
        return broj_racuna;
    }

    public void setBroj_racuna(BankovniRacun broj_racuna) {
        this.broj_racuna = broj_racuna;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void createVlasniksList() {
        VlasnikDAO dao = new VlasnikDAO();
        vlasnici = dao.getAllVlasnik();
    }

    public static void addVlasnikToList(Vlasnik vlasnik) {
        vlasnici.add(vlasnik);
    }

    public static void updateVlasniksList(Vlasnik vlasnik, String jmbg) {
        for (int i = 0; i < vlasnici.size(); i++) {
            if (vlasnici.get(i).getJmbg().equals(jmbg)) {
                vlasnici.set(i, vlasnik);
                break;
            }
        }
    }

    public static void removeVlasnikFromList(String jmbg) {
        for (Vlasnik v : vlasnici) {
            if (v.getJmbg().equals(jmbg)) {
                Vlasnik.vlasnici.remove(v);
            }
        }
    }


}
