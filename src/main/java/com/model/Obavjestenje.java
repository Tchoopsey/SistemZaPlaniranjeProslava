package com.model;

import java.util.ArrayList;
import java.util.List;

public class Obavjestenje {

    private int id;
    private Objekat objekat;
    private String tekst;

    private static List<Obavjestenje> svaObavjestenja = new ArrayList<>();

    public Obavjestenje(int id, Objekat objekat, String tekst) {
        this.id = id;
        this.objekat = objekat;
        this.tekst = tekst;
    }

    public Objekat getObjekat() {
        return objekat;
    }

    public String getTekst() {
        return tekst;
    }
}
