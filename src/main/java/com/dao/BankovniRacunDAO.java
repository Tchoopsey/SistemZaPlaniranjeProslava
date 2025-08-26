package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.model.BankovniRacun;

public class BankovniRacunDAO {

    public List<BankovniRacun> getAllBankovniRacun(Connection conn) {
        List<BankovniRacun> racuni = new ArrayList<>();
        String sql = "SELECT * FROM `Bankovni racun`";

        try (PreparedStatement ps = conn.prepareStatement(sql);
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

    public boolean createBankovniRacun(BankovniRacun bankovni_racun, Connection conn) {

        String sql = "INSERT INTO " 
            + "`Bankovni racun` (jmbg, broj_racuna, stanje) "
            + "VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bankovni_racun.getJmbg());
            ps.setString(2, bankovni_racun.getBroj_racuna());
            ps.setDouble(3, bankovni_racun.getStanje());

            BankovniRacun.createBankovniRacunsList(conn);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Creating BankovniRacun was unsuccessful!!!");
            return false;
        }
    }

    public boolean removeBankovniRacun(String jmbg, Connection conn) {
        String sql = "DELETE FROM `Bankovni racun` WHERE jmbg = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, jmbg);

            BankovniRacun.removeBankovniRacunFromList(jmbg);
            
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Removing BankovniRacun was unsuccessful!!!");
            return false;
        }
    }

    public boolean updateBankovniRacun(BankovniRacun bankovni_racun, Connection conn) {
        String sql = "UPDATE `Bankovni racun` SET jmbg = ?, broj_racuna = ?,"
            + " stanje = ? WHERE jmbg = ?";

        String jmbg = bankovni_racun.getJmbg();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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

    public BankovniRacun getById(int id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM `Bankovni Racun` WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BankovniRacun bankovniRacun = new BankovniRacun();

                    bankovniRacun.setBroj_racuna(rs.getString("broj_racuna"));

                    return bankovniRacun;
                }
            }        
        } 

        return null;
    }
}
