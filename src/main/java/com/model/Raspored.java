package com.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dao.RasporedDAO;

public class Raspored {

    private Sto sto;
    private Proslava proslava;
    private List<String> gosti;

    private static List<Raspored> sviRasporedi = new ArrayList<>();

    public Raspored(Sto sto, Proslava proslava, List<String> gosti) {
        this.sto = sto;
        this.proslava = proslava;
        this.gosti = gosti;
    }

    public Raspored() {
    }

    public Proslava getProslava() {
        return proslava;
    }

    public Sto getSto() {
        return sto;
    }

    public void setSto(Sto sto) {
        this.sto = sto;
    }

    public void setProslava(Proslava proslava) {
        this.proslava = proslava;
    }

    public List<String> getGosti() {
        return gosti;
    }

    public void setGosti(List<String> gosti) {
        this.gosti = gosti;
    }

    public static List<Raspored> getSviRasporedi() {
        return sviRasporedi;
    }

    public static void setSviStolovi(List<Raspored> sviRasporedi) {
        Raspored.sviRasporedi = sviRasporedi;
    }

    public static List<String> gostiFromString(String string) {
        List<String> gosti = new ArrayList<>(Arrays.asList(string.split(",")));

        return gosti;
    }

    public static String gostiFromList(List<String> list) {
        String gosti = String.join(",", list);

        return gosti;
    }

    public static void createRasporedList() {
        RasporedDAO dao = new RasporedDAO();
        sviRasporedi = dao.getAllRasporedi();
    }

    public static void addRasporedToList(Raspored raspored) {
        sviRasporedi.add(raspored);
    }

    public static void updateRasporedList(Raspored raspored, int idSto) {
        for (int i = 0; i < sviRasporedi.size(); i++) {
            if (sviRasporedi.get(i).getSto().getId() == idSto) {
                sviRasporedi.set(i, raspored);
                break;
            }
        }
    }

    public static void removeRasporedFromList(int idProslava) {
        sviRasporedi.removeIf(r -> r.getProslava().getId() == idProslava);
    }

}
