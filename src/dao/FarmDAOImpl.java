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
        String sql = "INSERT INTO Farm (name, location) VALUES (?, ?)";

        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, farm.getName());
            stmt.setString(2, farm.getLocation());

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Ferme creee avec succes.");
            else          System.out.println("Echec de creation.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Farm> getAll() {
        List<Farm> farms = new ArrayList<>();
        String sql = "SELECT * FROM Farm";

        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                farms.add(new Farm(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return farms;
    }

    @Override
    public Farm getById(int id) {
        String sql = "SELECT * FROM Farm WHERE id = ?";

        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Farm(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("location")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public void update(Farm farm, int id) {
        String sql = "UPDATE Farm SET name = ?, location = ? WHERE id = ?";

        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, farm.getName());
            stmt.setString(2, farm.getLocation());
            stmt.setInt(3, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Ferme mise a jour.");
            else          System.out.println("Echec de mise a jour.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Farm WHERE id = ?";

        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Ferme supprimee.");
            else          System.out.println("Echec de suppression.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
