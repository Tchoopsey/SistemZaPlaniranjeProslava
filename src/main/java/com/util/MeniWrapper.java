package com.util;

import com.model.Meni;

public class MeniWrapper {
    int id;
    Meni meni;

    public MeniWrapper(int id, Meni meni) {
        this.id = id;
        this.meni = meni;
    }

    public int getId() {
        return id;
    }


    public Meni getMeni() {
        return meni;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMeni(Meni meni) {
        this.meni = meni;
    }

    @Override
    public String toString() {
        return "Meni " + id + "\n"
            + meni.getCijena_po_osobi() + "KM po osobi\n"
            + meni.getOpis();
    }
}
