package dao;

import database.ConnectionDb;
import model.Farm;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FarmDAOImpl implements FarmDAO {

    @Override
    public Farm create(Farm farm) {
        String sql = "INSERT INTO Farm (name, location) VALUES (?, ?)";
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, farm.getName());
            stmt.setString(2, farm.getLocation());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    farm.setId(generatedKeys.getInt(1));
                }
            }
            return farm;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating farm: " + e.getMessage(), e);
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
            throw new RuntimeException("Error fetching farms: " + e.getMessage(), e);
        }
        return farms;
    }

    @Override
    public Optional<Farm> getById(int id) {
        String sql = "SELECT * FROM Farm WHERE id = ?";
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Farm(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("location")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching farm by id: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Farm> searchByName(String name) {
        List<Farm> farms = new ArrayList<>();
        String sql = "SELECT * FROM Farm WHERE LOWER(name) LIKE LOWER(?)";
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    farms.add(new Farm(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("location")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching farms by name: " + e.getMessage(), e);
        }
        return farms;
    }

    @Override
    public Farm update(Farm farm, int id) {
        String sql = "UPDATE Farm SET name = ?, location = ? WHERE id = ?";
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, farm.getName());
            stmt.setString(2, farm.getLocation());
            stmt.setInt(3, id);
            stmt.executeUpdate();
            farm.setId(id);
            return farm;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating farm: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Farm WHERE id = ?";
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting farm: " + e.getMessage(), e);
        }
    }

    @Override
    public long countAnimals(int farmId) {
        return countByFarm("SELECT COUNT(*) FROM Animal WHERE farmId = ?", farmId);
    }

    @Override
    public long countWorkers(int farmId) {
        return countByFarm("SELECT COUNT(*) FROM Ouvrier WHERE farm_id = ?", farmId);
    }

    @Override
    public long countTasks(int farmId) {
        return countByFarm("SELECT COUNT(*) FROM Task WHERE farm_id = ?", farmId);
    }

    @Override
    public long countStocks(int farmId) {
        return countByFarm("SELECT COUNT(*) FROM Stock WHERE farm_id = ?", farmId);
    }

    private long countByFarm(String sql, int farmId) {
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, farmId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getLong(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting farm data: " + e.getMessage(), e);
        }
    }
}
