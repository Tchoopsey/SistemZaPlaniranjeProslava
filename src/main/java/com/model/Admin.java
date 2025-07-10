package com.model;

public class Admin extends Korisnik {

    private String password;

    public Admin(String ime, String prezime, String korisnicko_ime, String password) {
        super(ime, prezime, korisnicko_ime);
        this.password = password;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
