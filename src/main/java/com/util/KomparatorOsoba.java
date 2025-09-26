package com.util;

import java.util.Comparator;

public class KomparatorOsoba implements Comparator<OsobaWrapper> {
    @Override
    public int compare(OsobaWrapper o1, OsobaWrapper o2) {
        int cmp = o1.getOsoba().getPrezime()
            .compareToIgnoreCase(o2.getOsoba().getPrezime());
        if (cmp != 0) {
            return cmp;
        }

        return o1.getOsoba().getIme().compareToIgnoreCase(o2.getOsoba().getIme());
    }
}
