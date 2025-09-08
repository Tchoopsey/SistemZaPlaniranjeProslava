package com.model;

import java.util.ArrayList;
import java.util.List;

import com.dao.StoDAO;

public class Sto {

    private int id;
    private Objekat objekat;
    private int broj_mjesta;
    
    private static List<Sto> sviStolovi = new ArrayList<>();

    public Sto(int id, Objekat objekat, int broj_mjesta) {
        this.id = id;
        this.objekat = objekat;
        this.broj_mjesta = broj_mjesta;
    }

    public Sto() {
    }

    public Objekat getObjekat() {
        return objekat;
    }

    public int getId() {
        return id;
    }

    public static List<Sto> getSviStolovi() {
        return sviStolovi;
    }

    public int getBroj_mjesta() {
        return broj_mjesta;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setObjekat(Objekat objekat) {
        this.objekat = objekat;
    }

    public void setBroj_mjesta(int broj_mjesta) {
        this.broj_mjesta = broj_mjesta;
    }

    public static void setSviStolovi(List<Sto> sviStolovi) {
        Sto.sviStolovi = sviStolovi;
    }

    public static void createStoList() {
        StoDAO dao = new StoDAO();
        sviStolovi = dao.getAllStolovi();
    }

    public static void addStoToList(Sto raspored) {
        sviStolovi.add(raspored);
    }

    public static void updateStoList(Sto raspored, int id) {
        for (int i = 0; i < sviStolovi.size(); i++) {
            if (sviStolovi.get(i).getId() == id) {
                sviStolovi.set(i, raspored);
                break;
            }
        }
    }

    public static void removeStoFromList(int id) {
        for (Sto raspored : sviStolovi) {
            if (raspored.getId() == id) {
                Sto.sviStolovi.remove(raspored);
            }
        }
    }

}
