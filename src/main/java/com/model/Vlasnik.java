package com.model;

public class Vlasnik extends Korisnik {

    private String jmbg;
    private String broj_racuna;
    private String password;

    public Vlasnik(String korisnicko_ime, String jmbg, String broj_racuna, String password) {
        super(korisnicko_ime);
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

    public String getBroj_racuna() {
        return broj_racuna;
    }

    public void setBroj_racuna(String broj_racuna) {
        this.broj_racuna = broj_racuna;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
