package com.model;

public class Objekat {

    private Vlasnik vlasnik;
    private String naziv;
    private double cijena_rezervacije;
    private String grad;
    private String adresa;
    private int broj_mjesta;
    private int broj_stolova;
    // private float zarada;
    // private boolean status;
    public Objekat(Vlasnik vlasnik, String naziv, double cijena_rezervacije, String grad, String adresa,
            int broj_mjesta, int broj_stolova) {
        this.vlasnik = vlasnik;
        this.naziv = naziv;
        this.cijena_rezervacije = cijena_rezervacije;
        this.grad = grad;
        this.adresa = adresa;
        this.broj_mjesta = broj_mjesta;
        this.broj_stolova = broj_stolova;
    }

    public Vlasnik getVlasnik() {
        return vlasnik;
    }

    public String getNaziv() {
        return naziv;
    }

    public double getCijena_rezervacije() {
        return cijena_rezervacije;
    }

    public String getGrad() {
        return grad;
    }

    public String getAdresa() {
        return adresa;
    }

    public int getBroj_mjesta() {
        return broj_mjesta;
    }

    public int getBroj_stolova() {
        return broj_stolova;
    }
}
