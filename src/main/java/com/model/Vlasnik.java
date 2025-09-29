package com.model;

import java.util.ArrayList;
import java.util.List;

import com.dao.VlasnikDAO;

public class Vlasnik extends Korisnik {

    private int id;
    private String broj_racuna;
    private String jmbg;
    private String broj_telefona;

    private static List<Vlasnik> vlasnici = new ArrayList<>();

    public Vlasnik(int id, String ime, String prezime,  String jmbg, 
        String korisnicko_ime, String broj_racuna, String password, String broj_telefona) {
        super(ime, prezime, korisnicko_ime, password);
        this.id = id;
        this.broj_racuna = broj_racuna;
        this.jmbg = jmbg;
        this.broj_telefona = broj_telefona;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public static void setVlasnici(List<Vlasnik> vlasnici) {
        Vlasnik.vlasnici = vlasnici;
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
        vlasnici.removeIf(v -> v.getJmbg().equals(jmbg));
    }

    public static Vlasnik getById(int id) {
        for (Vlasnik vlasnik : vlasnici) {
            if (vlasnik.getId() == id) {
                return vlasnik;
            }
        }

        return null;
    }

    public String getBroj_telefona() {
        return broj_telefona;
    }

    public void setBroj_telefona(String broj_telefona) {
        this.broj_telefona = broj_telefona;
    }

    public static Vlasnik getVlasnik(String user) {
        for (Vlasnik vlasnik : vlasnici) {
            if (vlasnik.getKorisnicko_ime().equals(user)) {
                return vlasnik;
            }
        }
        return null;
    }
}
