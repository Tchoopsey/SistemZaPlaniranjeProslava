package com.model;

public class Proslava {

    private Objekat objekat;
    private Klijent klijent;
    private Meni meni;
    private String datum;
    private int broj_gostiju;
    // private double ukupna_cijena;
    // private double uplacen_iznos;
    public Proslava(Objekat objekat, Klijent klijent, Meni meni, String datum, int broj_gostiju) {
        this.objekat = objekat;
        this.klijent = klijent;
        this.meni = meni;
        this.datum = datum;
        this.broj_gostiju = broj_gostiju;
    }

    public Objekat getObjekat() {
        return objekat;
    }

    public Klijent getKlijent() {
        return klijent;
    }

    public Meni getMeni() {
        return meni;
    }

    public String getDatum() {
        return datum;
    }

    public int getBroj_gostiju() {
        return broj_gostiju;
    }
}
