package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConnection;
import com.model.Proslava;
import com.model.Raspored;
import com.model.Sto;

public class RasporedDAO {

    public List<Raspored> getAllRasporedi() {
        List<Raspored> rasporedi = new ArrayList<>();
        String sql = "SELECT * FROM Raspored";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int sto_id = rs.getInt("Sto_id");
                int proslava_id = rs.getInt("Proslava_id");
                String gosti = rs.getString("gosti");

                rasporedi.add(new Raspored(
                    id,
                    Sto.getById(sto_id),
                    Proslava.getById(proslava_id),
                    Raspored.gostiFromString(gosti)
                ));
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rasporedi;
    }

    public boolean createRaspored(Raspored raspored) {

        String sql = "INSERT INTO " 
            + "Raspored (Sto_id, Proslava_id, gosti  VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, raspored.getSto().getId());
            ps.setInt(2, raspored.getProslava().getId());
            ps.setString(3, Raspored.gostiFromList(raspored.getGosti()));

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating Raspored was unsuccessful!!!");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    raspored.setId(rs.getInt(1));
                }
            }
            Raspored.addRasporedToList(raspored);
            System.out.println("Creating Raspored...");

            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Creating Raspored was unsuccessful!!!");
            return false;
        }
    }

    public boolean removeRaspored(int id) {
        String sql = "DELETE FROM Raspored WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            Raspored.removeRasporedFromList(id);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Removing Raspored was unsuccessful!!!");
            return false;
        }
    }

    public boolean updateRaspored(Raspored raspored, int id) {
        String sql = "UPDATE Raspored SET Sto_id = ?, Proslava_id = ?," 
            + " gosti WHERE naziv = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, raspored.getSto().getId());
            ps.setInt(2, raspored.getProslava().getId());
            ps.setString(3, Raspored.gostiFromList(raspored.getGosti()));
            ps.setInt(4, id);

            Raspored.updateRasporedList(raspored, id);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Updating Raspored was unsuccessful!!!");
            return false;
        }
    }
    
    public static Raspored getById(int id) throws SQLException {
        String sql = "SELECT * FROM Raspored WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Raspored raspored = new Raspored();
                    raspored.setSto(StoDAO.getById(rs.getInt("Sto_id")));
                    raspored.setProslava(ProslavaDAO.getById(rs.getInt("Proslava_id")));
                    raspored.setGosti(Raspored.gostiFromString(rs.getString("gosti")));
                    
                    return raspored;
                }
            }        
        } 

        return null;
    }
    
}
