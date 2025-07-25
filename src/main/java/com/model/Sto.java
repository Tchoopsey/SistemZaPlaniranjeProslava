package com.model;

public class Sto {

    private int id;
    private Objekat objekat;
    private int broj_mjesta;
    public Sto(int id, Objekat objekat, int broj_mjesta) {
        this.id = id;
        this.objekat = objekat;
        this.broj_mjesta = broj_mjesta;
    }

    public Objekat getObjekat() {
        return objekat;
    }

    public int getBroj_mjesta() {
        return broj_mjesta;
    }
}
