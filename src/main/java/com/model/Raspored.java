package com.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Raspored {

    private int id;
    private Sto sto;
    private Proslava proslava;
    private List<String> gosti;

    private static List<Sto> sviStolovi = new ArrayList<>();

    public Raspored(int id, Sto sto, Proslava proslava, List<String> gosti) {
        this.id = id;
        this.sto = sto;
        this.proslava = proslava;
        this.gosti = gosti;
    }

    public Raspored() {
    }

    public Proslava getProslava() {
        return proslava;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public static List<Sto> getSviStolovi() {
        return sviStolovi;
    }

    public static void setSviStolovi(List<Sto> sviStolovi) {
        Raspored.sviStolovi = sviStolovi;
    }

    public static List<String> gostiFromString(String string) {
        List<String> gosti = new ArrayList<>(Arrays.asList(string.split(",")));

        return gosti;
    }

    public static String gostiFromList(List<String> list) {
        String gosti = String.join(", ", list);

        return gosti;
    }
}
