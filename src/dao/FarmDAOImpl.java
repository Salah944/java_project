package dao;

import database.ConnectionDb;
import model.Farm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FarmDAOImpl implements FarmDAO {

    @Override
    public void create(Farm farm) {
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement(
                    "INSERT INTO Farms (name, location) VALUES (?, ?)"
            );
            stmt.setString(1, farm.getName());
            stmt.setString(2, farm.getLocation());

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Ferme créée avec succès.");
            else          System.out.println("Echec de création.");

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Farm> getAll() {
        List<Farm> farms = new ArrayList<>();
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement("SELECT * FROM Farms");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                farms.add(new Farm(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location")
                ));
            }

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return farms;
    }

    @Override
    public Farm getById(int id) {
        Farm farm = null;
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement("SELECT * FROM Farms WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                farm = new Farm(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location")
                );
            }

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return farm;
    }

    @Override
    public void update(Farm farm, int id) {
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement(
                    "UPDATE Farms SET name = ?, location = ? WHERE id = ?"
            );
            stmt.setString(1, farm.getName());
            stmt.setString(2, farm.getLocation());
            stmt.setInt(3, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Ferme mise à jour.");
            else          System.out.println("Echec de mise à jour.");

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement("DELETE FROM Farms WHERE id = ?");
            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Ferme supprimée.");
            else          System.out.println("Echec de suppression.");

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}