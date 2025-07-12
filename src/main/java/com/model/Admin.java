package com.model;

import java.util.ArrayList;
import java.util.List;

import com.dao.AdminDAO;

public class Admin extends Korisnik {

    private String password;

    private static List<Admin> admins = new ArrayList<>();

    public Admin(String ime, String prezime, String korisnicko_ime, String password) {
        super(ime, prezime, korisnicko_ime);
        this.password = password;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void createAdminsList() {
        AdminDAO dao = new AdminDAO();
        admins = dao.getAllAdmin();
    }

    public static void addAdminToList(Admin admin) {
        admins.add(admin);
    }

    public static void updateAdminsList(Admin admin, String korisnicko_ime) {
        for (int i = 0; i < admins.size(); i++) {
            if (admins.get(i).getKorisnicko_ime().equals(korisnicko_ime)) {
                admins.set(i, admin);
                break;
            }
        }
    }

    public static void removeAdminFromList(String korisnicko_ime) {
        for (Admin a : admins) {
            if (a.getKorisnicko_ime().equals(korisnicko_ime)) {
                Admin.admins.remove(a);
            }
        }
    }

}
