package com.model;

public class Admin extends Korisnik {

    private String password;

    public Admin(String korisnicko_ime, String password) {
        super(korisnicko_ime);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
