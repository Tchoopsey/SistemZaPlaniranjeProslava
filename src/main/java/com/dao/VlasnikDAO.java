package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConnection;
import com.model.Vlasnik;

public class VlasnikDAO {

    public List<Vlasnik> getAllVlasnik() {
        List<Vlasnik> vlasnici = new ArrayList<>();
        String sql = "SELECT * FROM `Vlasnik`";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String ime = rs.getString("ime");
                String prezime = rs.getString("prezime");
                String jmbg = rs.getString("jmbg");
                String korisnicko_ime = rs.getString("korisnicko_ime");
                String broj_racuna = rs.getString("broj_racuna");
                String password = rs.getString("lozinka");
                String broj_telefona = rs.getString("broj_telefona");
                vlasnici.add(
                    new Vlasnik(
                        id,
                        ime,
                        prezime,
                        jmbg,
                        korisnicko_ime,
                        broj_racuna,
                        password,
                        broj_telefona
                    )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vlasnici;
    }

    public boolean createVlasnik(Vlasnik vlasnik) {

        String sql = "INSERT INTO " 
            + "Vlasnik (ime, prezime, korisnicko_ime, jmbg, lozinka, broj_telefona, broj_racuna) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, vlasnik.getIme());
            ps.setString(2, vlasnik.getPrezime());
            ps.setString(3, vlasnik.getKorisnicko_ime());
            ps.setString(4, vlasnik.getJmbg());
            ps.setString(5, vlasnik.getPassword());
            ps.setString(6, vlasnik.getBroj_telefona());
            ps.setString(7, vlasnik.getBroj_racuna());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating Vlasnik was unsuccessful!!!");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    vlasnik.setId(rs.getInt(1));
                }
            }
            Vlasnik.addVlasnikToList(vlasnik);
            System.out.println("Creating Vlasnik...");

            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Creating Vlasnik user was unsuccessful!!!");
            return false;
        }
    }

    public boolean removeVlasnik(String jmbg) {
        String sql = "DELETE FROM Vlasnik WHERE jmbg = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, jmbg);

            Vlasnik.removeVlasnikFromList(jmbg);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Removing Vlasnik user was unsuccessful!!!");
            return false;
        }
    }

    public boolean updateVlasnik(Vlasnik vlasnik, String jmbg) {
        String sql = "UPDATE Vlasnik SET ime = ?, prezime = ?, jmbg = ?"
            + " korisnicko_ime = ?, lozinka = ?, broj_telefona = ? WHERE jmbg = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vlasnik.getIme());
            ps.setString(2, vlasnik.getPrezime());
            ps.setString(3, vlasnik.getKorisnicko_ime());
            ps.setString(4, vlasnik.getJmbg());
            ps.setString(5, vlasnik.getPassword());
            ps.setString(6, vlasnik.getBroj_telefona());
            ps.setString(7, jmbg);

            Vlasnik.updateVlasniksList(vlasnik, jmbg);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Updating Vlasnik user was unsuccessful!!!");
            return false;
        }
    }

    public static Vlasnik getById(int id) throws SQLException {
        String sql = "SELECT * FROM Vlasnik WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Vlasnik vlasnik = new Vlasnik();
                    vlasnik.setIme(rs.getString("ime"));
                    vlasnik.setPrezime(rs.getString("prezime"));
                    vlasnik.setJmbg(rs.getString("jmbg"));
                    vlasnik.setKorisnicko_ime(rs.getString("korisnicko_ime"));
                    vlasnik.setBroj_racuna(rs.getString("broj_racuna"));
                    vlasnik.setPassword(rs.getString("lozinka"));
                    vlasnik.setBroj_telefona(rs.getString("broj_telefona"));

                    return vlasnik;
                }
            }        
        } 

        return null;
    }

}
