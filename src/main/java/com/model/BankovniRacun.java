package com.model;

public class BankovniRacun {

    private String jmbg;
    private String broj_racuna;
    public BankovniRacun(String jmbg, String broj_racuna) {
        this.jmbg = jmbg;
        this.broj_racuna = broj_racuna;
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
}
