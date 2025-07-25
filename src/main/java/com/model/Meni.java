package com.model;

public class Meni {

    private int id;
    private Objekat objekat;
    private String opis;
    private double cijena_po_osobi;
    public Meni(int id, Objekat objekat, String opis, double cijena_po_osobi) {
        this.id = id;
        this.objekat = objekat;
        this.opis = opis;
        this.cijena_po_osobi = cijena_po_osobi;
    }

    public Objekat getObjekat() {
        return objekat;
    }

    public String getOpis() {
        return opis;
    }

    public double getCijena_po_osobi() {
        return cijena_po_osobi;
    }
}
