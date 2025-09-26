package com.util;

import java.util.Comparator;

import com.model.Objekat;

public class KomparatorObjekata implements Comparator<Objekat> {

    @Override
    public int compare(Objekat o1, Objekat o2) {
        return o1.getNaziv().compareToIgnoreCase(o2.getNaziv());
    }
    
}
