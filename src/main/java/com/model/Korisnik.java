package com.model;

import java.util.ArrayList;
import java.util.List;

public class Korisnik {

    private String ime;
    private String prezime;
    private String korisnicko_ime;
    private String password;

    private static List<Korisnik> korisnici = new ArrayList<>();

    public Korisnik(String ime, String prezime, String korisnicko_ime, String password) {
        this.ime = ime;
        this.prezime = prezime;
        this.korisnicko_ime = korisnicko_ime;
        this.password = password;
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

    public void setKorisnicko_ime(String korisnicko_ime) {
        this.korisnicko_ime = korisnicko_ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public static String checkKorisnicko_ime(String user) {
        for (Korisnik korisnik : korisnici) {
            if (korisnik.getKorisnicko_ime().equals(user)) {
                if (korisnik instanceof Admin) {
                    return "Admin";
                }
                if (korisnik instanceof Vlasnik) {
                    return "Vlasnik";
                }
                if (korisnik instanceof Klijent) {
                    return "Klijent";
                }
            }
        }
        System.out.println("None");
        return "None";
    }

    public static boolean isPresent(String user) {
        for (Korisnik korisnik : korisnici) {
            if (korisnik.getKorisnicko_ime().equals(user)) {
                return true;
            }
        }
        return false;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static List<Korisnik> getKorisnici() {
        return korisnici;
    }

    public static void setKorisnici(List<Korisnik> korisnici) {
        Korisnik.korisnici = korisnici;
    }

    public static void printKorisnici() {
        for (Korisnik korisnik : korisnici) {
            System.out.println(korisnik.getKorisnicko_ime());
        }
    }
}
