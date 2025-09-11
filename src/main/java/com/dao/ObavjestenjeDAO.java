package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConnection;
import com.model.Obavjestenje;

public class ObavjestenjeDAO {

    public List<Obavjestenje> getAllObavjestenja() {
        List<Obavjestenje> obavjestenja = new ArrayList<>();
        String sql = "SELECT * FROM Obavjestenje";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int objekat_id = rs.getInt("Objekat_id");
                String tekst = rs.getString("tekst");

                obavjestenja.add(new Obavjestenje(
                    id,
                    ObjekatDAO.getById(objekat_id),
                    tekst
                ));
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return obavjestenja;
    }

    public boolean createObavjestenje(Obavjestenje obavjestenje) {
        String sql = "INSERT INTO " 
            + "Obavjestenje (Objekat_id, tekst VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, obavjestenje.getObjekat().getId());
            ps.setString(2, obavjestenje.getTekst());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating Obavjestenje was unsuccessful!!!");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    obavjestenje.setId(rs.getInt(1));
                }
            }
            Obavjestenje.addObavjestenjeToList(obavjestenje);
            System.out.println("Creating Obavjestenje...");

            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Creating Obavjestenje was unsuccessful!!!");
            return false;
        }
    }

    public boolean removeObavjestenje(int id) {
        String sql = "DELETE FROM Obavjestenje WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            Obavjestenje.removeObavjestenjeFromList(id);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Removing Obavjestenje was unsuccessful!!!");
            return false;
        }
    }

    public boolean updateObavjestenje(Obavjestenje obavjestenje, int id) {
        String sql = "UPDATE Obavjestenje SET Objekat_id = ?, tekst" 
            + " WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, obavjestenje.getObjekat().getId());
            ps.setString(2, obavjestenje.getTekst());
            ps.setInt(3, id);

            Obavjestenje.updateObavjestenjeList(obavjestenje, id);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Updating Obavjestenje was unsuccessful!!!");
            return false;
        }
    }
    
    public static Obavjestenje getById(int id) throws SQLException {
        String sql = "SELECT * FROM Obavjestenje WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Obavjestenje obavjestenje = new Obavjestenje();
                    obavjestenje.setObjekat(ObjekatDAO.getById(rs.getInt("Objekat_id")));
                    obavjestenje.setTekst(rs.getString("tekst"));
                    
                    return obavjestenje;
                }
            }        
        } 

        return null;
    }
    
}
