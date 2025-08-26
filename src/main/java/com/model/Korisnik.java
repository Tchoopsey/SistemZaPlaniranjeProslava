package com.model;

import java.util.ArrayList;
import java.util.List;

public class Korisnik {

    private String ime;
    private String prezime;
    private String jmbg;
    private String korisnicko_ime;

    private static List<Korisnik> korisnici = new ArrayList<>();

    public Korisnik(String ime, String prezime, String jmbg, String korisnicko_ime) {
        this.ime = ime;
        this.prezime = prezime;
        this.jmbg = jmbg;
        this.korisnicko_ime = korisnicko_ime;
        korisnici.add(this);
    }

    public Korisnik() {
    }

    public String getKorisnicko_ime() {
        return korisnicko_ime;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setKorisnicko_ime(String korisnicko_ime) {
        this.korisnicko_ime = korisnicko_ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public static Korisnik getByJmbg(String jmbg) {
        for (Korisnik korisnik : korisnici) {
            if (korisnik.getJmbg().equals(jmbg)) {
                return korisnik;
            }
        }

        return null;
    }

}
