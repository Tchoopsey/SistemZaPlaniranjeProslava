package com.model;

import java.util.ArrayList;
import java.util.List;

import com.dao.ProslavaDAO;

public class Proslava {

    private int id;
    private Objekat objekat;
    private Klijent klijent;
    private Meni meni;
    private String datum;
    private int broj_gostiju;
    private double ukupna_cijena;
    private double uplacen_iznos;

    private static List<Proslava> sveProslave = new ArrayList<>();

    public Proslava(int id, Objekat objekat, Klijent klijent, Meni meni, 
        String datum, int broj_gostiju, double ukupna_cijena, double uplacen_iznos) {
        this.id = id;
        this.objekat = objekat;
        this.klijent = klijent;
        this.meni = meni;
        this.datum = datum;
        this.broj_gostiju = broj_gostiju;
        this.ukupna_cijena = ukupna_cijena;
        this.uplacen_iznos = uplacen_iznos;
    }

    public Proslava() {
    }

    public Objekat getObjekat() {
        return objekat;
    }

    public Klijent getKlijent() {
        return klijent;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setObjekat(Objekat objekat) {
        this.objekat = objekat;
    }

    public void setKlijent(Klijent klijent) {
        this.klijent = klijent;
    }

    public void setMeni(Meni meni) {
        this.meni = meni;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public void setBroj_gostiju(int broj_gostiju) {
        this.broj_gostiju = broj_gostiju;
    }

    public void setUkupna_cijena(double ukupna_cijena) {
        this.ukupna_cijena = ukupna_cijena;
    }

    public void setUplacen_iznos(double uplacen_iznos) {
        this.uplacen_iznos = uplacen_iznos;
    }

    public static List<Proslava> getSveProslave() {
        return sveProslave;
    }

    public static void setSveProslave(List<Proslava> sveProslave) {
        Proslava.sveProslave = sveProslave;
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

    public int getId() {
        return id;
    }

    public double getUkupna_cijena() {
        return ukupna_cijena;
    }

    public double getUplacen_iznos() {
        return uplacen_iznos;
    }

    public static void createProslavaList() {
        ProslavaDAO dao = new ProslavaDAO();
        sveProslave = dao.getAllProslave();
    }

    public static void addProslavaToList(Proslava proslava) {
        sveProslave.add(proslava);
    }

    public static void updateProslavaList(Proslava proslava, int id) {
        for (int i = 0; i < sveProslave.size(); i++) {
            if (sveProslave.get(i).getId() == id) {
                sveProslave.set(i, proslava);
                break;
            }
        }
    }

    public static void removeProslavaFromList(int id) {
        for (Proslava proslava : sveProslave) {
            if (proslava.getId() == id) {
                Proslava.sveProslave.remove(proslava);
            }
        }
    }

}
