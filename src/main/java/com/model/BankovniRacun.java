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

    public BankovniRacun() {
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

    public static BankovniRacun getByBrojRacuna(String broj_racuna) {
        for (BankovniRacun bankovniRacun : racuni) {
            if (bankovniRacun.getBroj_racuna().equals(broj_racuna)) {
                return bankovniRacun;
            }
        }

        return null;
    }

    public static void updateBankovniRacunsList(BankovniRacun racun, String jmbg) {
        for (int i = 0; i < racuni.size(); i++) {
            if (racuni.get(i).getJmbg().equals(jmbg)) {
                racuni.set(i, racun);
                break;
            }
        }
    }

    public static void removeBankovniRacunFromList(String jmbg) {
        for (BankovniRacun v : racuni) {
            if (v.getJmbg().equals(jmbg)) {
                BankovniRacun.racuni.remove(v);
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getJmbg() {
        return jmbg;
    }

    public static List<BankovniRacun> getRacuni() {
        return racuni;
    }
}
