package com.model;

public class Korisnik {

    private String ime;
    private String prezime;
    private String korisnicko_ime;
    public Korisnik(String ime, String prezime, String korisnicko_ime) {
        this.ime = ime;
        this.prezime = prezime;
        this.korisnicko_ime = korisnicko_ime;
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

    public void setKorisnicko_ime(String korisnicko_ime) {
        this.korisnicko_ime = korisnicko_ime;
    }
}
