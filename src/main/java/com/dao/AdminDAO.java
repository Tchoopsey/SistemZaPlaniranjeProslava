package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.model.Admin;

public class AdminDAO {

    public List<Admin> getAllAdmin(Connection conn) {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT * FROM `Admin`";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String ime = rs.getString("ime");
                String prezime = rs.getString("prezime");
                String jmbg = rs.getString("jmbg");
                String korisnicko_ime = rs.getString("korisnicko_ime");
                String password = rs.getString("lozinka");
                admins.add(
                    new Admin(id, ime, prezime, jmbg, korisnicko_ime, password)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admins;
    }

    public boolean createAdmin(Admin admin, Connection conn) {

        String sql = "INSERT INTO " 
            + "Admin (ime, prezime, korisnicko_ime, lozinka) "
            + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, admin.getIme());
            ps.setString(2, admin.getPrezime());
            ps.setString(3, admin.getKorisnicko_ime());
            ps.setString(4, admin.getPassword());

            Admin.addAdminToList(admin);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Creating Admin user was unsuccessful!!!");
            return false;
        }
    }

    public boolean removeAdmin(String korisnicko_ime, Connection conn) {
        String sql = "DELETE FROM Admin WHERE korisnicko_ime = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, korisnicko_ime);

            Admin.removeAdminFromList(korisnicko_ime);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Removing Admin user was unsuccessful!!!");
            return false;
        }

    }

    public boolean updateAdmin(Admin admin, String korisnicko_ime, Connection conn) {
        String sql = "UPDATE Admin SET ime = ?, prezime = ?,"
            + " korisnicko_ime = ?, lozinka = ? WHERE korisnicko_ime = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, admin.getIme());
            ps.setString(2, admin.getPrezime());
            ps.setString(3, admin.getKorisnicko_ime());
            ps.setString(4, admin.getPassword());
            ps.setString(5, korisnicko_ime);

            Admin.updateAdminsList(admin, korisnicko_ime);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Updating Admin user was unsuccessful!!!");
            return false;
        }
    }
}
