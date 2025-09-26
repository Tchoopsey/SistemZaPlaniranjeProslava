package com.util;

import com.model.Osoba;

public class OsobaWrapper {
    private Osoba osoba;
    private StoWrapper sto;

    public OsobaWrapper(Osoba osoba, StoWrapper sto) {
        this.osoba = osoba;
        this.sto = sto;
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public StoWrapper getSto() {
        return sto;
    }

    public void setSto(StoWrapper sto) {
        this.sto = sto;
    }

    @Override
    public String toString() {
        return osoba.prezimeIme() + ": Sto " + sto.getId();
    }
}
