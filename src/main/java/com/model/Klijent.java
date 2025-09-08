package com.model;

import java.util.ArrayList;
import java.util.List;

import com.dao.KlijentDAO;

public class Klijent extends Korisnik {

    private int id;
    private String broj_racuna;
    private String jmbg;
    private String broj_telefona;

    private static List<Klijent> klijenti = new ArrayList<>();

    public Klijent(int id, String ime, String prezime, String jmbg, String korisnicko_ime, 
        String broj_racuna, String password, String broj_telefona) {
        super(ime, prezime, korisnicko_ime, password);
        this.id = id;
        this.jmbg = jmbg;
        this.broj_racuna = broj_racuna;
        this.broj_telefona = broj_telefona;
    }

    public Klijent() {
        super();
    }

    public String getBroj_racuna() {
        return broj_racuna;
    }

    public void setBroj_racuna(String broj_racuna) {
        this.broj_racuna = broj_racuna;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static List<Klijent> getKlijenti() {
        return klijenti;
    }

    public static void setKlijenti(List<Klijent> klijenti) {
        Klijent.klijenti = klijenti;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public String getBroj_telefona() {
        return broj_telefona;
    }

    public void setBroj_telefona(String broj_telefona) {
        this.broj_telefona = broj_telefona;
    }

    public static Klijent getKlijent(String user, String pass) {
        for (Klijent klijent : klijenti) {
            if (klijent.getPassword().equals(pass) &&
                klijent.getKorisnicko_ime().equals(user)) {
                return klijent;
            }
        }
        return null;
    }
}
