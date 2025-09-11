package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConnection;
import com.model.Klijent;
import com.model.Meni;
import com.model.Objekat;
import com.model.Proslava;

public class ProslavaDAO {

    public List<Proslava> getAllProslave() {
        List<Proslava> proslave = new ArrayList<>();
        String sql = "SELECT * FROM Proslava";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int objekat_id = rs.getInt("Objekat_id");
                int klijent_id = rs.getInt("Klijent_id");
                int meni_id = rs.getInt("Meni_id");
                String datum = rs.getString("datum");
                int broj_gostiju = rs.getInt("broj_gostiju");
                double ukupna_cijena = rs.getDouble("ukupna_cijena");
                double uplacen_iznos = rs.getDouble("uplacen_iznos");

                proslave.add(new Proslava(
                    id,
                    Objekat.getById(objekat_id),
                    Klijent.getById(klijent_id),
                    Meni.getById(meni_id),
                    datum,
                    broj_gostiju,
                    ukupna_cijena,
                    uplacen_iznos
                ));
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return proslave;
    }

    public boolean createProslava(Proslava proslava) {

        String sql = "INSERT INTO " 
            + "Proslava (Objekat_id, Klijent_id, Meni_id, datum, broj_gostiju," 
            + " ukupna_cijena, uplacen_iznos VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, proslava.getObjekat().getId());
            ps.setInt(2, proslava.getKlijent().getId());
            ps.setInt(3, proslava.getMeni().getId());
            ps.setString(4, proslava.getDatum());
            ps.setInt(5, proslava.getBroj_gostiju());
            ps.setDouble(6, proslava.getUkupna_cijena());
            ps.setDouble(7, proslava.getUplacen_iznos());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating Proslava was unsuccessful!!!");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    proslava.setId(rs.getInt(1));
                }
            }
            Proslava.addProslavaToList(proslava);
            System.out.println("Creating Proslava...");

            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Creating Proslava was unsuccessful!!!");
            return false;
        }
    }

    public boolean removeProslava(int id) {
        String sql = "DELETE FROM Proslava WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            Proslava.removeProslavaFromList(id);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Removing Proslava was unsuccessful!!!");
            return false;
        }
    }

    public boolean updateProslava(Proslava proslava, int id) {
        String sql = "UPDATE Proslava SET Objekat_id = ?, Klijent_id = ?," 
            + " Meni_id = ?, datum = ?, broj_gostiju = ?, ukupna_cijena = ?,"
            + " uplacen_iznos = ? WHERE naziv = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, proslava.getObjekat().getId());
            ps.setInt(2, proslava.getKlijent().getId());
            ps.setInt(3, proslava.getMeni().getId());
            ps.setString(4, proslava.getDatum());
            ps.setInt(5, proslava.getBroj_gostiju());
            ps.setDouble(6, proslava.getUkupna_cijena());
            ps.setDouble(7, proslava.getUplacen_iznos());
            ps.setInt(8, id);

            Proslava.updateProslavaList(proslava, id);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Updating Proslava was unsuccessful!!!");
            return false;
        }
    }
    
    public static Proslava getById(int id) throws SQLException {
        String sql = "SELECT * FROM Proslava WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Proslava proslava = new Proslava();
                    proslava.setObjekat(ObjekatDAO.getById(rs.getInt("Objekat_id")));
                    proslava.setKlijent(KlijentDAO.getById(rs.getInt("Klijent_id")));
                    proslava.setMeni(MeniDAO.getById(rs.getInt("Meni_id")));
                    proslava.setDatum(rs.getString("datum"));
                    proslava.setBroj_gostiju(rs.getInt("broj_gostiju"));
                    proslava.setUkupna_cijena(rs.getDouble("ukupna_cijena"));
                    proslava.setUplacen_iznos(rs.getDouble("uplacen_iznos"));
                    
                    return proslava;
                }
            }        
        } 

        return null;
    }
}
