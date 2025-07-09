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

}
