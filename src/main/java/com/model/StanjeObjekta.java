package com.model;

public enum StanjeObjekta {
    NA_CEKANJU("NA CEKANJU"), 
    ODOBREN("ODOBREN"), 
    ODBIJEN("ODBIJEN");

    private final String text;

    StanjeObjekta(String text) {
        this.text = text;
    }

    public static StanjeObjekta fromString(String string) {
        for (StanjeObjekta so : StanjeObjekta.values()) {
            if (so.text.equalsIgnoreCase(string)) {
                return so;
            }
        }

        throw new IllegalArgumentException("Zadatostanje ne postoji!!!!");
    }

    public static String toString(StanjeObjekta stanjeObjekta) {
        switch (stanjeObjekta) {
            case NA_CEKANJU:
                return "NA CEKANJU";
            case ODOBREN:
                return "ODOBREN";
            case ODBIJEN:
                return "ODBIJEN";
            default:
                return null;
        }
    }
}
