package com.model;

import java.util.ArrayList;
import java.util.List;

import com.dao.AdminDAO;

public class Admin extends Korisnik {

    private int id;

    private static List<Admin> admins = new ArrayList<>();

    public Admin(int id, String ime, String prezime,
        String korisnicko_ime, String password) {
        super(ime, prezime, korisnicko_ime, password);
        this.id = id;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public static List<Admin> getAdmins() {
        return admins;
    }


    public static void setAdmins(ArrayList<Admin> admins) {
        Admin.admins = admins;
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
        admins.removeIf(a -> a.getKorisnicko_ime().equals(korisnicko_ime));
    }

    public static Admin getAdmin(String user) {
        for (Admin admin : admins) {
            if (admin.getKorisnicko_ime().equals(user)) {
                return admin;
            }
        }
        return null;
    }

}
