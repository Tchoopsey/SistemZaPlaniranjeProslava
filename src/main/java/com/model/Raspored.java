package com.model;

public class Raspored {

    private int id;
    private Sto sto;
    private Proslava proslava;
    // gosti? broj? klijent?

    public Raspored(int id, Proslava proslava) {
        this.id = id;
        this.proslava = proslava;
    }

    public Proslava getProslava() {
        return proslava;
    }
}
