package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dbutil.DBConnection;
import com.model.BankovniRacun;
import com.model.Vlasnik;

public class VlasnikDAO {

    public List<Vlasnik> getAllVlasnik() {
        List<Vlasnik> vlasnici = new ArrayList<>();
        String sql = "SELECT * FROM `Bankovni racun`";

        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
            // String ime = rs.getString("ime");
            // String prezime = rs.getString("prezime");
            String jmbg = rs.getString("jmbg");
            String broj_racuna = rs.getString("broj_racuna");
            String korisnicko_ime = rs.getString("korisnicko_ime");
            String password = rs.getString("lozinka");
            vlasnici.add(new Vlasnik(korisnicko_ime, jmbg, new BankovniRacun(jmbg, broj_racuna), password));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vlasnici;
    }

}
