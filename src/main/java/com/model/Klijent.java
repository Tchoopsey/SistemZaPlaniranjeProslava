package com.model;

public class Klijent extends Korisnik {

    private String jmbg;
    private BankovniRacun broj_racuna;
    private String password;

    public Klijent(String ime, String prezime, String korisnicko_ime, String jmbg, 
        BankovniRacun broj_racuna, String password) {
        super(ime, prezime, korisnicko_ime);
        this.jmbg = jmbg;
        this.broj_racuna = broj_racuna;
        this.password = password;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public BankovniRacun getBroj_racuna() {
        return broj_racuna;
    }

    public void setBroj_racuna(BankovniRacun broj_racuna) {
        this.broj_racuna = broj_racuna;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
