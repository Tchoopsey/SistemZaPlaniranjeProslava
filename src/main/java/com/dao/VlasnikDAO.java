package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.model.Vlasnik;

public class VlasnikDAO {

    public List<Vlasnik> getAllVlasnik(Connection conn) {
        List<Vlasnik> vlasnici = new ArrayList<>();
        String sql = "SELECT * FROM `Vlasnik`";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String ime = rs.getString("ime");
                String prezime = rs.getString("prezime");
                String jmbg = rs.getString("jmbg");
                String broj_racuna = rs.getString("broj_racuna");
                String korisnicko_ime = rs.getString("korisnicko_ime");
                String password = rs.getString("lozinka");
                vlasnici.add(
                    new Vlasnik(
                        id,
                        ime,
                        prezime,
                        korisnicko_ime,
                        jmbg,
                        broj_racuna,
                        password
                    )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vlasnici;
    }

    public boolean createVlasnik(Vlasnik vlasnik, Connection conn) {

        String sql = "INSERT INTO " 
            + "Vlasnik (ime, prezime, korisnicko_ime, jmbg, lozinka) "
            + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vlasnik.getIme());
            ps.setString(2, vlasnik.getPrezime());
            ps.setString(3, vlasnik.getKorisnicko_ime());
            ps.setString(4, vlasnik.getJmbg());
            ps.setString(5, vlasnik.getPassword());

            Vlasnik.addVlasnikToList(vlasnik);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Creating Vlasnik user was unsuccessful!!!");
            return false;
        }
    }

    public boolean removeVlasnik(String jmbg, Connection conn) {
        String sql = "DELETE FROM Vlasnik WHERE jmbg = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, jmbg);

            Vlasnik.removeVlasnikFromList(jmbg);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Removing Vlasnik user was unsuccessful!!!");
            return false;
        }
    }

    public boolean updateVlasnik(Vlasnik vlasnik, String jmbg, Connection conn) {
        String sql = "UPDATE Vlasnik SET ime = ?, prezime = ?, jmbg = ?"
            + " korisnicko_ime = ?, lozinka = ? WHERE jmbg = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vlasnik.getIme());
            ps.setString(2, vlasnik.getPrezime());
            ps.setString(3, vlasnik.getKorisnicko_ime());
            ps.setString(4, vlasnik.getJmbg());
            ps.setString(5, vlasnik.getPassword());
            ps.setString(6, jmbg);

            Vlasnik.updateVlasniksList(vlasnik, jmbg);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Updating Vlasnik user was unsuccessful!!!");
            return false;
        }
    }

    public static Vlasnik getById(int id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM Vlasnik WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Vlasnik vlasnik = new Vlasnik();
                    vlasnik.setIme(rs.getString("ime"));
                    vlasnik.setPrezime(rs.getString("prezime"));
                    vlasnik.setJmbg(rs.getString("jmbg"));
                    vlasnik.setKorisnicko_ime(rs.getString("korisnicko_ime"));
                    vlasnik.setBroj_racuna(rs.getString("broj_racuna"));
                    vlasnik.setPassword(rs.getString("password"));

                    return vlasnik;
                }
            }        
        } 

        return null;
    }

}
