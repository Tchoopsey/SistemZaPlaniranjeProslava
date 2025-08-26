package com.model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.dao.ObavjestenjeDAO;

public class Obavjestenje {

    private int id;
    private Objekat objekat;
    private String tekst;

    private static List<Obavjestenje> svaObavjestenja = new ArrayList<>();

    public Obavjestenje(int id, Objekat objekat, String tekst) {
        this.id = id;
        this.objekat = objekat;
        this.tekst = tekst;
    }

    public Obavjestenje() {
    }

    public Objekat getObjekat() {
        return objekat;
    }

    public String getTekst() {
        return tekst;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setObjekat(Objekat objekat) {
        this.objekat = objekat;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public static List<Obavjestenje> getSvaObavjestenja() {
        return svaObavjestenja;
    }

    public static void setSvaObavjestenja(List<Obavjestenje> svaObavjestenja) {
        Obavjestenje.svaObavjestenja = svaObavjestenja;
    }

    public static void createObavjestenjaList(Connection conn) {
        ObavjestenjeDAO dao = new ObavjestenjeDAO();
        svaObavjestenja = dao.getAllObavjestenja(conn);
    }

    public static void addObavjestenjeToList(Obavjestenje obavjestenje) {
        svaObavjestenja.add(obavjestenje);
    }

    public static void updateObavjestenjeList(Obavjestenje obavjestenje, int id) {
        for (int i = 0; i < svaObavjestenja.size(); i++) {
            if (svaObavjestenja.get(i).getId() == id) {
                svaObavjestenja.set(i, obavjestenje);
                break;
            }
        }
    }

    public static void removeObavjestenjeFromList(int id) {
        for (Obavjestenje obavjestenje : svaObavjestenja) {
            if (obavjestenje.getId() == id) {
                Obavjestenje.svaObavjestenja.remove(obavjestenje);
            }
        }
    }
}
