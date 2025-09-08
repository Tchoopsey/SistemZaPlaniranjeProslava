package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConnection;
import com.model.Klijent;

public class KlijentDAO {

    public List<Klijent> getAllKlijent() {
        List<Klijent> klijenti = new ArrayList<>();
        String sql = "SELECT * FROM `Klijent`";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String ime = rs.getString("ime");
                String prezime = rs.getString("prezime");
                String jmbg = rs.getString("jmbg");
                String broj_racuna = rs.getString("broj_racuna");
                String korisnicko_ime = rs.getString("korisnicko_ime");
                String password = rs.getString("lozinka");
                String broj_telefona = rs.getString("broj_telefona");
                klijenti.add(
                    new Klijent(
                        id,
                        ime,
                        prezime,
                        korisnicko_ime,
                        jmbg,
                        broj_racuna,
                        password,
                        broj_telefona
                    )
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return klijenti;
    }


    public boolean createKlijent(Klijent klijent) {

        String sql = "INSERT INTO " 
            + "Klijent (ime, prezime, jmbg, broj_racuna, korisnicko_ime, lozinka) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, klijent.getIme());
            ps.setString(2, klijent.getPrezime());
            ps.setString(3, klijent.getJmbg());
            ps.setString(4, klijent.getBroj_racuna());
            ps.setString(5, klijent.getKorisnicko_ime());
            ps.setString(6, klijent.getPassword());
            ps.setString(6, klijent.getBroj_telefona());

            Klijent.addKlijentToList(klijent);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Creating Klijent user was unsuccessful!!!");
            return false;
        }
    }

    public boolean removeKlijent(String jmbg) {
        String sql = "DELETE FROM Klijent WHERE jmbg = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, jmbg);

            Klijent.removeKlijentFromList(jmbg);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Removing Klijent user was unsuccessful!!!");
            return false;
        }
    }

    public boolean updateKlijent(Klijent klijent, String jmbg) {
        String sql = "UPDATE Klijent SET ime = ?, prezime = ?, jmbg = ?"
            + ", broj_racuna = ?, korisnicko_ime = ?, lozinka = ? WHERE jmbg = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, klijent.getIme());
            ps.setString(2, klijent.getPrezime());
            ps.setString(3, klijent.getJmbg());
            ps.setString(4, klijent.getBroj_racuna());
            ps.setString(5, klijent.getKorisnicko_ime());
            ps.setString(6, klijent.getPassword());
            ps.setString(7, klijent.getBroj_telefona());
            ps.setString(8, jmbg);

            Klijent.updateKlijentsList(klijent, jmbg);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Updating Klijent user was unsuccessful!!!");
            return false;
        }
    }

    public static Klijent getById(int id) throws SQLException {
        String sql = "SELECT * FROM Klijent WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Klijent klijent = new Klijent();
                    klijent.setIme(rs.getString("ime"));
                    klijent.setPrezime(rs.getString("prezime"));
                    klijent.setJmbg("jmbg");
                    klijent.setBroj_racuna("broj_racuna");
                    klijent.setKorisnicko_ime("korisnicko_ime");
                    klijent.setPassword("lozinka");

                    return klijent;
                }
            }        
        } 

        return null;
    }

}
