package com.util;

import com.model.Sto;

public class StoWrapper {
    private int id;
    private Sto sto;

    public StoWrapper(int id, Sto sto) {
        this.id = id;
        this.sto = sto;
    }

    @Override
    public String toString() {
        return "Sto " + id + ": " + sto.getBroj_mjesta() + " mjesta";
    }

    public int getId() {
        return id;
    }

    public Sto getSto() {
        return sto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSto(Sto sto) {
        this.sto = sto;
    }

}

