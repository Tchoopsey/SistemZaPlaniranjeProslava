package com.model;

public class Osoba {

    private String ime, prezime;

    public Osoba(String ime, String prezime) {
        this.ime = ime;
        this.prezime = prezime;
    }

    public Osoba() {
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String prezimeIme() {
        return prezime + " " + ime;
    }

    @Override
    public String toString() {
        return ime + " " + prezime;
    }

}
