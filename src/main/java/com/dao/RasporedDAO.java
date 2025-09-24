package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                int sto_id = rs.getInt("idSto");
                int proslava_id = rs.getInt("idProslava");
                String gosti = rs.getString("gosti");

                rasporedi.add(new Raspored(
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
            + "Raspored (idSto, idProslava, gosti)  VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, raspored.getSto().getId());
            ps.setInt(2, raspored.getProslava().getId());
            ps.setString(3, Raspored.gostiFromList(raspored.getGosti()));

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating Raspored was unsuccessful!!!");
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

    public boolean removeRaspored(int idProslava) {
        String sql = "DELETE FROM Raspored WHERE idProslava = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProslava);

            Raspored.removeRasporedFromList(idProslava);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Removing Raspored was unsuccessful!!!");
            return false;
        }
    }

    public boolean updateRaspored(Raspored raspored, int idSto) {
        String sql = "UPDATE Raspored SET idSto = ?, idProslava = ?," 
            + " gosti = ? WHERE idSto = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, raspored.getSto().getId());
            ps.setInt(2, raspored.getProslava().getId());
            ps.setString(3, Raspored.gostiFromList(raspored.getGosti()));
            ps.setInt(4, idSto);

            Raspored.updateRasporedList(raspored, idSto);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Updating Raspored was unsuccessful!!!");
            return false;
        }
    }
}
