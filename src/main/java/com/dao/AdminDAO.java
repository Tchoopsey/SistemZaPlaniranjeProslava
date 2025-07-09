/*
 Not sure what exactly to do about this...yet
*/

package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dbutil.DBConnection;
import com.model.Admin;

public class AdminDAO {

    public List<Admin> getAllAdmin() {
        List<Admin> vlasnici = new ArrayList<>();
        String sql = "SELECT * FROM `Bankovni racun`";

        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
            // String ime = rs.getString("ime");
            // String prezime = rs.getString("prezime");
            String korisnicko_ime = rs.getString("korisnicko_ime");
            String password = rs.getString("lozinka");
            vlasnici.add(new Admin(korisnicko_ime, password));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vlasnici;
    }

}
