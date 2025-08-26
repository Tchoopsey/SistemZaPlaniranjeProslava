package com.model;

import java.util.ArrayList;
import java.util.List;

import com.dao.VlasnikDAO;

public class Vlasnik extends Korisnik {

    private int id;
    private String broj_racuna;
    private String password;

    private static List<Vlasnik> vlasnici = new ArrayList<>();

    public Vlasnik(int id, String ime, String prezime,  String jmbg, 
        String korisnicko_ime, String broj_racuna, String password) {
        super(ime, prezime, jmbg, korisnicko_ime);
        this.id = id;
        this.broj_racuna = broj_racuna;
        this.password = password;
    }

    public Vlasnik() {
        super();
    }

    public int getId() {
        return id;
    }

    public static List<Vlasnik> getVlasnici() {
        return vlasnici;
    }

    public String getBroj_racuna() {
        return broj_racuna;
    }

    public void setBroj_racuna(String broj_racuna) {
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

    public static Vlasnik getById(int id) {
        for (Vlasnik vlasnik : vlasnici) {
            if (vlasnik.getId() == id) {
                return vlasnik;
            }
        }

        return null;
    }
}
