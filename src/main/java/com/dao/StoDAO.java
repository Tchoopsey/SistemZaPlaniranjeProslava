package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConnection;
import com.model.Sto;

public class StoDAO {

    public List<Sto> getAllStolovi() {
        List<Sto> stolovi = new ArrayList<>();
        String sql = "SELECT * FROM Meni";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int objekat_id = rs.getInt("Objekat_id");
                int broj_mjesta = rs.getInt("broj_mjesta");

                stolovi.add(new Sto(
                    id,
                    ObjekatDAO.getById(objekat_id),
                    broj_mjesta
                ));
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stolovi;
    }

    public boolean createSto(Sto sto) {

        String sql = "INSERT INTO " 
            + "Sto (Objekat_id, broj_mjesta VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sto.getObjekat().getId());
            ps.setInt(2, sto.getBroj_mjesta());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Creating Sto was unsuccessful!!!");
            return false;
        }
    }

    public boolean removeSto(int id) {
        String sql = "DELETE FROM Sto WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Removing Sto was unsuccessful!!!");
            return false;
        }
    }

    public boolean updateSto(Sto sto, int id) {
        String sql = "UPDATE Sto SET Objekat_id = ?, broj_mjesta = ?," 
            + " WHERE naziv = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sto.getObjekat().getId());
            ps.setInt(2, sto.getBroj_mjesta());
            ps.setInt(3, id);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Updating Sto was unsuccessful!!!");
            return false;
        }
    }
    
    public static Sto getById(int id) throws SQLException {
        String sql = "SELECT * FROM Sto WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Sto sto = new Sto();
                    sto.setObjekat(ObjekatDAO.getById(rs.getInt("Objekat_id")));
                    sto.setBroj_mjesta(rs.getInt("broj_mjesta"));
                    
                    return sto;
                }
            }        
        } 

        return null;
    }
}
