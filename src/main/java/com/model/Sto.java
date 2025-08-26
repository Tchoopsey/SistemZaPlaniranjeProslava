package com.model;

import java.util.ArrayList;
import java.util.List;

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

}
