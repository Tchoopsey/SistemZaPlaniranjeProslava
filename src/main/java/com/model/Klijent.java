package com.model;

import java.util.ArrayList;
import java.util.List;

import com.dao.KlijentDAO;

public class Klijent extends Korisnik {

    private String jmbg;
    private BankovniRacun broj_racuna;
    private String password;

    private static List<Klijent> klijenti = new ArrayList<>();

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

    public static void createKlijentsList() {
        KlijentDAO dao = new KlijentDAO();
        klijenti = dao.getAllKlijent();
    }

    public static void addKlijentToList(Klijent klijent) {
        klijenti.add(klijent);
    }

    public static void updateKlijentsList(Klijent klijent, String jmbg) {
        for (int i = 0; i < klijenti.size(); i++) {
            if (klijenti.get(i).getJmbg().equals(jmbg)) {
                klijenti.set(i, klijent);
                break;
            }
        }
    }

    public static void removeKlijentFromList(String jmbg) {
        for (Klijent k : klijenti) {
            if (k.getJmbg().equals(jmbg)) {
                Klijent.klijenti.remove(k);
            }
        }
    }

}
