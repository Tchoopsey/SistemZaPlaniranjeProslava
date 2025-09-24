package com.model;

import java.util.ArrayList;
import java.util.List;

import com.dao.MeniDAO;

public class Meni {

    private int id;
    private Objekat objekat;
    private String opis;
    private double cijena_po_osobi;

    private static List<Meni> sviMeniji = new ArrayList<>();

    public Meni(int id, Objekat objekat, String opis, double cijena_po_osobi) {
        this.id = id;
        this.objekat = objekat;
        this.opis = opis;
        this.cijena_po_osobi = cijena_po_osobi;
    }

    public Meni() {
    }

    public Objekat getObjekat() {
        return objekat;
    }

    public String getOpis() {
        return opis;
    }

    public double getCijena_po_osobi() {
        return cijena_po_osobi;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setObjekat(Objekat objekat) {
        this.objekat = objekat;
    }

    public int getId() {
        return id;
    }

    public static List<Meni> getSviMeniji() {
        return sviMeniji;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public void setCijena_po_osobi(double cijena_po_osobi) {
        this.cijena_po_osobi = cijena_po_osobi;
    }

    public static void setSviMeniji(List<Meni> sviMeniji) {
        Meni.sviMeniji = sviMeniji;
    }

    public static void createMeniList() {
        MeniDAO dao = new MeniDAO();
        sviMeniji = dao.getAllMeni();
    }

    public static void addMeniToList(Meni meni) {
        sviMeniji.add(meni);
    }

    public static void updateMeniList(Meni meni, int id) {
        for (int i = 0; i < sviMeniji.size(); i++) {
            if (sviMeniji.get(i).getId() == id) {
                sviMeniji.set(i, meni);
                break;
            }
        }
    }

    public static void removeMeniFromList(int id) {
        sviMeniji.removeIf(m -> m.getId() == id);
    }

    public static Meni getById(int meni_id) {
        for (Meni meni : sviMeniji) {
            if (meni.getId() == meni_id) {
                return meni;
            }
        }

        return null;
    }
}
