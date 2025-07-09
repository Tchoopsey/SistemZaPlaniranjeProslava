package com.model;

public class Obavjestenje {

    private Objekat objekat;
    private String tekst;
    public Obavjestenje(Objekat objekat, String tekst) {
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
