package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.model.Obavjestenje;

public class ObavjestenjeDAO {

    public List<Obavjestenje> getAllObavjestenja(Connection conn) {
        List<Obavjestenje> obavjestenja = new ArrayList<>();
        String sql = "SELECT * FROM Obavjestenje";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int objekat_id = rs.getInt("Objekat_id");
                String tekst = rs.getString("tekst");

                obavjestenja.add(new Obavjestenje(
                    id,
                    ObjekatDAO.getById(objekat_id, conn),
                    tekst
                ));
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return obavjestenja;
    }

    public boolean createObavjestenje(Obavjestenje obavjestenje, Connection conn) {
        String sql = "INSERT INTO " 
            + "Obavjestenje (Objekat_id, tekst VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, obavjestenje.getObjekat().getId());
            ps.setString(2, obavjestenje.getTekst());

            Obavjestenje.addObavjestenjeToList(obavjestenje);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Creating Obavjestenje was unsuccessful!!!");
            return false;
        }
    }

    public boolean removeObavjestenje(int id, Connection conn) {
        String sql = "DELETE FROM Obavjestenje WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            Obavjestenje.removeObavjestenjeFromList(id);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Removing Obavjestenje was unsuccessful!!!");
            return false;
        }
    }

    public boolean updateObavjestenje(Obavjestenje obavjestenje, int id, Connection conn) {
        String sql = "UPDATE Obavjestenje SET Objekat_id = ?, tekst" 
            + " WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
    
    public static Obavjestenje getById(int id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM Obavjestenje WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Obavjestenje obavjestenje = new Obavjestenje();
                    obavjestenje.setObjekat(ObjekatDAO.getById(rs.getInt("Objekat_id"), conn));
                    obavjestenje.setTekst(rs.getString("tekst"));
                    
                    return obavjestenje;
                }
            }        
        } 

        return null;
    }
    
}
