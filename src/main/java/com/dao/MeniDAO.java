package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConnection;
import com.model.Meni;
import com.model.Objekat;

public class MeniDAO {
    
    public List<Meni> getAllMeni() {
        List<Meni> menii = new ArrayList<>();
        String sql = "SELECT * FROM Meni";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {

                int id = rs.getInt("id");
                int objekat_id = rs.getInt("Objekat_id");
                String opis = rs.getString("opis");
                double cijena_po_osobi = rs.getDouble("cijena_po_osobi");
                menii.add(new Meni(
                    id, 
                    Objekat.getById(objekat_id), 
                    opis, 
                    cijena_po_osobi)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menii;
    }

    public boolean createMeni(Meni meni) {

        String sql = "INSERT INTO " 
            + "Meni (Objekat_id, opis, cijena_po_osobi) "
            + "VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, meni.getObjekat().getId());
            ps.setString(2, meni.getOpis());
            ps.setDouble(3, meni.getCijena_po_osobi());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating Meni was unsuccessful!!!");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    meni.setId(rs.getInt(1));
                }
            }
            Meni.addMeniToList(meni);
            System.out.println("Creating Meni...");

            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Creating Meni was unsuccessful!!!");
            return false;
        }
    }

    public boolean removeMeni(int id) {
        String sql = "DELETE FROM Meni WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            Meni.removeMeniFromList(id);
            System.out.println("Removing Meni...");

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Removing Meni was unsuccessful!!!");
            return false;
        }
    }

    public boolean updateMeni(Meni meni) {
        String sql = "UPDATE Meni SET Objekat_id = ?, opis = ?, cijena_po_osobi = ?"
            + " WHERE id = ?";

        int id = meni.getId();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, meni.getObjekat().getId());
            ps.setString(2, meni.getOpis());
            ps.setDouble(3, meni.getCijena_po_osobi());
            ps.setInt(4, id);

            Meni.updateMeniList(meni, id);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Updating Meni was unsuccessful!!!");
            return false;
        }
    }
    
    public static Meni getById(int id) throws SQLException {
        String sql = "SELECT * FROM Meni WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Meni meni = new Meni();
                    meni.setObjekat(ObjekatDAO.getById(rs.getInt("Objekat_id")));
                    meni.setOpis(rs.getString("opis"));
                    meni.setCijena_po_osobi(rs.getDouble("cijena_po_osobi"));
                    return meni;
                }
            }        
        } 

        return null;
    }
}
