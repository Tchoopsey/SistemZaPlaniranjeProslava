package com.model;

public class Raspored {

    private Sto sto;
    private Proslava proslava;
    // gosti? broj? klijent?

    public Raspored(Proslava proslava) {
        this.proslava = proslava;
    }

    public Proslava getProslava() {
        return proslava;
    }
}
