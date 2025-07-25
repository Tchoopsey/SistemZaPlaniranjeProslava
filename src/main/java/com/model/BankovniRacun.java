package com.model;

import java.util.ArrayList;
import java.util.List;

import com.dao.BankovniRacunDAO;

public class BankovniRacun {

    private int id;
    private String jmbg;
    private String broj_racuna;

    private static List<BankovniRacun> racuni = new ArrayList<>();

    public BankovniRacun(int id, String jmbg, String broj_racuna) {
        this.id = id;
        this.jmbg = jmbg;
        this.broj_racuna = broj_racuna;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public String getBroj_racuna() {
        return broj_racuna;
    }

    public void setBroj_racuna(String broj_racuna) {
        this.broj_racuna = broj_racuna;
    }

    public static void createBankovniRacunsList() {
        BankovniRacunDAO dao = new BankovniRacunDAO();
        racuni = dao.getAllBankovniRacun();
    }

    public static void addBankovniRacunToList(BankovniRacun racun) {
        racuni.add(racun);
    }

    public static void updateBankovniRacunsList(BankovniRacun racun, String korisnicko_ime) {
        for (int i = 0; i < racuni.size(); i++) {
            if (racuni.get(i).getKorisnicko_ime().equals(korisnicko_ime)) {
                racuni.set(i, racun);
                break;
            }
        }
    }

    public static void removeBankovniRacunFromList(String korisnicko_ime) {
        for (BankovniRacun v : racuni) {
            if (v.getKorisnicko_ime().equals(korisnicko_ime)) {
                BankovniRacun.racuni.remove(v);
            }
        }
    }
}
