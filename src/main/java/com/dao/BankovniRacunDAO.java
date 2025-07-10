package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dbutil.DBConnection;
import com.model.BankovniRacun;

public class BankovniRacunDAO {

    public List<BankovniRacun> getAllBankovniRacun() {
        List<BankovniRacun> racuni = new ArrayList<>();
        String sql = "SELECT * FROM `Bankovni racun`";

        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
            String broj_racuna = rs.getString("broj_racuna");
            String jmbg = rs.getString("jmbg");
            racuni.add(new BankovniRacun(jmbg, broj_racuna));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return racuni;
    }

    public boolean createbankovni_racun(BankovniRacun bankovni_racun) {

        String sql = "INSERT INTO " 
            + "bankovni_racun (jmbg, broj_racuna) "
            + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bankovni_racun.getJmbg());
            ps.setString(2, bankovni_racun.getBroj_racuna());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Creating BankovniRacun was unsuccessful!!!");
            return false;
        }
    }

    public boolean removebankovni_racun(String jmbg) {
        String sql = "DELETE FROM bankovni_racun WHERE jmbg = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, jmbg);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Removing BankovniRacun was unsuccessful!!!");
            return false;
        }
    }

    public boolean updatebankovni_racun(BankovniRacun bankovni_racun) {
        String sql = "UPDATE bankovni_racun SET ime = ?, prezime = ?, jmbg = ?"
            + " korisnicko_ime = ?, lozinka = ? WHERE jmbg = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bankovni_racun.getJmbg());
            ps.setString(2, bankovni_racun.getBroj_racuna());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Updating BankovniRacun was unsuccessful!!!");
            return false;
        }
    }
}
