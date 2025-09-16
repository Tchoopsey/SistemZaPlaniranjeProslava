package com.model;

import java.util.ArrayList;
import java.util.List;

import com.dao.ObjekatDAO;

public class Objekat {

    int id;
    private Vlasnik vlasnik;
    private String naziv;
    private double cijena_rezervacije;
    private String grad;
    private String adresa;
    private int broj_mjesta;
    private int broj_stolova;
    private String datumi;
    private double zarada;
    private StanjeObjekta status;

    private static List<Objekat> sviObjekti = new ArrayList<>();

    public Objekat(int id, Vlasnik vlasnik, String naziv, double cijena_rezervacije, 
            String grad, String adresa, int broj_mjesta, int broj_stolova, String datumi,
            double zarada, StanjeObjekta status) {
        this.id = id;
        this.vlasnik = vlasnik;
        this.naziv = naziv;
        this.cijena_rezervacije = cijena_rezervacije;
        this.grad = grad;
        this.adresa = adresa;
        this.broj_mjesta = broj_mjesta;
        this.broj_stolova = broj_stolova;
        this.datumi = datumi;
        this.status = status;
    }

    public Objekat() {
    }

    public Vlasnik getVlasnik() {
        return vlasnik;
    }

    public String getNaziv() {
        return naziv;
    }

    public double getCijena_rezervacije() {
        return cijena_rezervacije;
    }

    public String getGrad() {
        return grad;
    }

    public String getAdresa() {
        return adresa;
    }

    public int getBroj_mjesta() {
        return broj_mjesta;
    }

    public int getBroj_stolova() {
        return broj_stolova;
    }

    public int getId() {
        return id;
    }

    public String getDatumi() {
        return datumi;
    }

    public double getZarada() {
        return zarada;
    }

    public StanjeObjekta getStatus() {
        return status;
    }

    public static List<Objekat> getSviObjekti() {
        return sviObjekti;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVlasnik(Vlasnik vlasnik) {
        this.vlasnik = vlasnik;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setCijena_rezervacije(double cijena_rezervacije) {
        this.cijena_rezervacije = cijena_rezervacije;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public void setBroj_mjesta(int broj_mjesta) {
        this.broj_mjesta = broj_mjesta;
    }

    public void setBroj_stolova(int broj_stolova) {
        this.broj_stolova = broj_stolova;
    }

    public void setDatumi(String datumi) {
        this.datumi = datumi;
    }

    public void setZarada(double zarada) {
        this.zarada = zarada;
    }

    public void setStatus(StanjeObjekta status) {
        this.status = status;
    }

    public static void setSviObjekti(List<Objekat> sviObjekti) {
        Objekat.sviObjekti = sviObjekti;
    }

    public static Objekat getById(int objekat_id) {
        for (Objekat objekat : sviObjekti) {
            if (objekat.getId() == objekat_id) {
                return objekat;
            }
        }

        return null;
    }
    

    public static void createObjekatList() {
        ObjekatDAO dao = new ObjekatDAO();
        sviObjekti = dao.getAllObjekti();
    }

    public static void addObjekatToList(Objekat objekat) {
        sviObjekti.add(objekat);
    }

    public static void updateObjekatList(Objekat objekat, String naziv) {
        for (int i = 0; i < sviObjekti.size(); i++) {
            if (sviObjekti.get(i).getNaziv().equals(naziv)) {
                sviObjekti.set(i, objekat);
                break;
            }
        }
    }

    public static void removeObjekatFromList(String naziv) {
        for (Objekat objekat : sviObjekti) {
            if (objekat.getNaziv().equals(naziv)) {
                Objekat.sviObjekti.remove(objekat);
            }
        }
    }

    @Override
    public String toString() {
        return this.getNaziv() + ", " + this.getGrad() + ", " + this.getAdresa();
    }
}
