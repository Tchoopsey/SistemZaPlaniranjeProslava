package com.model;

public enum StanjeObjekta {
    NA_CEKANJU, ODOBREN, ODBIJEN;

    public static StanjeObjekta fromString(String string) {
        for (StanjeObjekta so : StanjeObjekta.values()) {
            if (so.name().equalsIgnoreCase(string)) {
                return so;
            }
        }

        throw new IllegalArgumentException("Zadatostanje ne postoji!!!!");
    }

    public static String toString(StanjeObjekta stanjeObjekta) {
        switch (stanjeObjekta) {
            case NA_CEKANJU:
                return "NA_CEKANJU";
            case ODOBREN:
                return "ODOBREN";
            case ODBIJEN:
                return "ODBIJEN";
            default:
                return null;
        }
    }
}
