package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConnection;
import com.model.Objekat;
import com.model.StanjeObjekta;
import com.model.Vlasnik;

public class ObjekatDAO {

    public List<Objekat> getAllObjekti() {
        List<Objekat> objekti = new ArrayList<>();
        String sql = "SELECT * FROM Objekat";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int vlasnik_id = rs.getInt("Vlasnik_id");
                String naziv = rs.getString("naziv");
                double cijena_rezervacije = rs.getDouble("cijena_rezervacije");
                String grad = rs.getString("grad");
                String adresa = rs.getString("adresa");
                int broj_mjesta = rs.getInt("broj_mjesta");
                int broj_stolova = rs.getInt("broj_stolova");
                String datumi = rs.getString("datumi");
                double zarada = rs.getDouble("zarada");
                StanjeObjekta status = StanjeObjekta.fromString(
                    rs.getString("status"));

                objekti.add(new Objekat(
                    id, 
                    Vlasnik.getById(vlasnik_id), 
                    naziv, 
                    cijena_rezervacije, 
                    grad, 
                    adresa, 
                    broj_mjesta, 
                    broj_stolova, 
                    datumi,
                    zarada, 
                    status
                ));
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return objekti;
    }

    public boolean createObjekat(Objekat objekat) {

        String sql = "INSERT INTO " 
            + "Objekat (Vlasnik_id, naziv, cijena_rezervacije, grad, "
            + "adresa, broj_mjesta, broj_stolova, datumi, zarada, status) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, objekat.getVlasnik().getId());
            ps.setString(2, objekat.getNaziv());
            ps.setDouble(3, objekat.getCijena_rezervacije());
            ps.setString(4, objekat.getGrad());
            ps.setString(5, objekat.getAdresa());
            ps.setInt(6, objekat.getBroj_mjesta());
            ps.setInt(7, objekat.getBroj_stolova());
            ps.setString(8, objekat.getDatumi());
            ps.setDouble(9, objekat.getZarada());
            ps.setString(10, StanjeObjekta.toString(objekat.getStatus()));

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating Objekat was unsuccessful!!!");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    objekat.setId(rs.getInt(1));
                }
            }
            Objekat.addObjekatToList(objekat);
            System.out.println("Creating Objekat...");

            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Creating Objekat was unsuccessful!!!");
            return false;
        }
    }

    public boolean removeObjekat(String naziv) {
        String sql = "DELETE FROM Objekat WHERE naziv = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, naziv);

            Objekat.removeObjekatFromList(naziv);
            System.out.println("Removing Objekat...");

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Removing Objekat was unsuccessful!!!");
            return false;
        }
    }

    public boolean updateObjekat(Objekat objekat, String naziv) {
        String sql = "UPDATE Objekat SET Vlasnik_id = ?, naziv = ?, cijena_rezervacije = ?,"
            + " grad = ?, adresa = ?, broj_mjesta = ?, broj_stolova = ?, datumi = ?, zarada = ?,"
            + " status = ? WHERE naziv = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, objekat.getVlasnik().getId());
            ps.setString(2, objekat.getNaziv());
            ps.setDouble(3, objekat.getCijena_rezervacije());
            ps.setString(4, objekat.getGrad());
            ps.setString(5, objekat.getAdresa());
            ps.setInt(6, objekat.getBroj_mjesta());
            ps.setInt(7, objekat.getBroj_stolova());
            ps.setString(8, objekat.getDatumi());
            ps.setDouble(9, objekat.getZarada());
            ps.setString(10, StanjeObjekta.toString(objekat.getStatus()));
            ps.setString(11, naziv);

            Objekat.updateObjekatList(objekat, naziv);

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Updating Objekat was unsuccessful!!!");
            return false;
        }
    }
    
    public static Objekat getById(int id) throws SQLException {
        String sql = "SELECT * FROM Objekat WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Objekat objekat = new Objekat();
                    objekat.setVlasnik(VlasnikDAO.getById(rs.getInt("Vlasnik_id")));
                    objekat.setNaziv(rs.getString("naziv"));
                    objekat.setCijena_rezervacije(rs.getDouble("cijena_rezervacije"));
                    objekat.setGrad(rs.getString("grad"));
                    objekat.setAdresa(rs.getString("adresa"));
                    objekat.setBroj_mjesta(rs.getInt("broj_mjesta"));
                    objekat.setBroj_stolova(rs.getInt("broj_stolova"));
                    objekat.setDatumi(rs.getString("datumi"));
                    objekat.setZarada(rs.getDouble("zarada"));
                    objekat.setStatus(StanjeObjekta.fromString(rs.getString("status")));
                    
                    return objekat;
                }
            }        
        } 

        return null;
    }

}
