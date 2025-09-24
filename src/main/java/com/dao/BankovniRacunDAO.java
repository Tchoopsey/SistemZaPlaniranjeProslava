package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConnection;
import com.model.BankovniRacun;

public class BankovniRacunDAO {

    public List<BankovniRacun> getAllBankovniRacun() {
        List<BankovniRacun> racuni = new ArrayList<>();
        String sql = "SELECT * FROM `Bankovni racun`";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String broj_racuna = rs.getString("broj_racuna");
                String jmbg = rs.getString("jmbg");
                double stanje = rs.getDouble("stanje");
                racuni.add(new BankovniRacun(id, jmbg, broj_racuna, stanje));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return racuni;
    }

    public boolean createBankovniRacun(BankovniRacun bankovni_racun) {

        String sql = "INSERT INTO " 
            + "`Bankovni racun` (jmbg, broj_racuna, stanje) "
            + "VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bankovni_racun.getJmbg());
            ps.setString(2, bankovni_racun.getBroj_racuna());
            ps.setDouble(3, bankovni_racun.getStanje());

            BankovniRacun.createBankovniRacunsList();

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Creating BankovniRacun was unsuccessful!!!");
            return false;
        }
    }

    public boolean removeBankovniRacun(String jmbg) {
        String sql = "DELETE FROM `Bankovni racun` WHERE jmbg = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, jmbg);

            BankovniRacun.removeBankovniRacunFromList(jmbg);
            
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Removing BankovniRacun was unsuccessful!!!");
            return false;
        }
    }

    public boolean updateBankovniRacun(BankovniRacun bankovni_racun) {
        String sql = "UPDATE `Bankovni racun` SET jmbg = ?, broj_racuna = ?,"
            + " stanje = ? WHERE jmbg = ?";

        String jmbg = bankovni_racun.getJmbg();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bankovni_racun.getJmbg());
            ps.setString(2, bankovni_racun.getBroj_racuna());
            ps.setDouble(3, bankovni_racun.getStanje());
            ps.setString(4, jmbg);

            BankovniRacun.updateBankovniRacunsList(bankovni_racun, jmbg);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Updating BankovniRacun was unsuccessful!!!");
            return false;
        }
    }
}
