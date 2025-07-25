package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dbutil.DBConnection;
import com.model.Meni;
// import com.model.Objekat;

public class MeniDAO {

    
    public List<Meni> getAllMeni() {
        List<Meni> menii = new ArrayList<>();
        String sql = "SELECT * FROM Meni";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {

                int id = rs.getInt("id");
                // TODO: pair with ObjekatDAO to create new Meni
                String opis = rs.getString("opis");
                double cijena_po_osobi = rs.getDouble("cijena_po_osobi");
                // menii.add(new Meni(cijena_po_osobi, opis));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menii;
    }

    public boolean createMeni(Meni meni) {

        String sql = "INSERT INTO " 
            + "Meni (cijena_po_osobi, opis) "
            + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, meni.getCijena_po_osobi());
            ps.setString(2, meni.getOpis());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Creating Meni was unsuccessful!!!");
            return false;
        }
    }

    public boolean removeMeni(String cijena_po_osobi) {
        String sql = "DELETE FROM Meni WHERE cijena_po_osobi = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cijena_po_osobi);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Removing Meni was unsuccessful!!!");
            return false;
        }
    }

    public boolean updateMeni(Meni meni) {
        String sql = "UPDATE Meni SET ime = ?, prezime = ?, cijena_po_osobi = ?"
            + " korisnicko_ime = ?, lozinka = ? WHERE cijena_po_osobi = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, meni.getCijena_po_osobi());
            ps.setString(2, meni.getOpis());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Updating Meni was unsuccessful!!!");
            return false;
        }
    }
}
