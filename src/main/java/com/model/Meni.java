package com.model;

import java.util.ArrayList;
import java.util.List;

public class Meni {

    private int id;
    private Objekat objekat;
    private String opis;
    private double cijena_po_osobi;

    private static List<Meni> sviMeniji = new ArrayList<>();

    public Meni(int id, Objekat objekat, String opis, double cijena_po_osobi) {
        this.id = id;
        this.objekat = objekat;
        this.opis = opis;
        this.cijena_po_osobi = cijena_po_osobi;
    }

    public Meni() {
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

    public void setId(int id) {
        this.id = id;
    }

    public void setObjekat(Objekat objekat) {
        this.objekat = objekat;
    }

    public int getId() {
        return id;
    }

    public static List<Meni> getSviMeniji() {
        return sviMeniji;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public void setCijena_po_osobi(double cijena_po_osobi) {
        this.cijena_po_osobi = cijena_po_osobi;
    }

    public static void setSviMeniji(List<Meni> sviMeniji) {
        Meni.sviMeniji = sviMeniji;
    }
}
