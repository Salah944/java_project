package dao;

import database.ConnectionDb;
import model.Cultiver;
import model.enums.CropStatus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CultiverDAOImpl implements CultiverDAO {

    @Override
    public Cultiver create(Cultiver cultiver) {
        String sql = "INSERT INTO Cultiver (farm_id, name, plantingDate, harvestDate, quantity, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, cultiver.getFarmId());
            stmt.setString(2, cultiver.getName());
            stmt.setDate(3, new java.sql.Date(cultiver.getPlanningDate().getTime()));
            stmt.setDate(4, cultiver.getHervesDate() != null ? new java.sql.Date(cultiver.getHervesDate().getTime()) : null);
            stmt.setInt(5, cultiver.getQuantity());
            stmt.setString(6, toDatabaseStatus(cultiver.getStatus()));
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) cultiver.setId(keys.getInt(1));
            }
            return cultiver;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating crop: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Cultiver> getAll() {
        return fetchCrops("SELECT * FROM Cultiver", null);
    }

    @Override
    public Optional<Cultiver> getById(int id) {
        List<Cultiver> list = fetchCrops("SELECT * FROM Cultiver WHERE id = ?", id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<Cultiver> getByFarm(int farmId) {
        return fetchCrops("SELECT * FROM Cultiver WHERE farm_id = ?", farmId);
    }

    @Override
    public Cultiver update(Cultiver cultiver, int id) {
        String sql = "UPDATE Cultiver SET farm_id = ?, name = ?, plantingDate = ?, harvestDate = ?, quantity = ?, status = ? WHERE id = ?";
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, cultiver.getFarmId());
            stmt.setString(2, cultiver.getName());
            stmt.setDate(3, new java.sql.Date(cultiver.getPlanningDate().getTime()));
            stmt.setDate(4, cultiver.getHervesDate() != null ? new java.sql.Date(cultiver.getHervesDate().getTime()) : null);
            stmt.setInt(5, cultiver.getQuantity());
            stmt.setString(6, toDatabaseStatus(cultiver.getStatus()));
            stmt.setInt(7, id);
            stmt.executeUpdate();
            cultiver.setId(id);
            return cultiver;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating crop: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Cultiver WHERE id = ?";
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting crop: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateStatus(int id, CropStatus status) {
        String sql = "UPDATE Cultiver SET status = ? WHERE id = ?";
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, toDatabaseStatus(status));
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating crop status: " + e.getMessage(), e);
        }
    }

    private List<Cultiver> fetchCrops(String sql, Integer param) {
        List<Cultiver> list = new ArrayList<>();
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            if (param != null) stmt.setInt(1, param);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Cultiver(
                        rs.getInt("id"),
                        rs.getInt("farm_id"),
                        rs.getString("name"),
                        rs.getDate("plantingDate"),
                        rs.getDate("harvestDate"),
                        rs.getInt("quantity"),
                        fromDatabaseStatus(rs.getString("status"))
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching crops: " + e.getMessage(), e);
        }
        return list;
    }

    private String toDatabaseStatus(CropStatus status) {
        switch (status) {
            case PLANTED:
            case GROWING:
                return "En cours";
            case HARVESTED:
                return "R\u00E9colt\u00E9";
            case FAILED:
                return "Abandonn\u00E9";
            default:
                throw new IllegalArgumentException("Unsupported crop status for database: " + status);
        }
    }

    private CropStatus fromDatabaseStatus(String status) {
        if (status == null) {
            return null;
        }
        String normalized = status.trim();
        if (normalized.equalsIgnoreCase("En cours") ||
                normalized.equalsIgnoreCase("PLANTED") ||
                normalized.equalsIgnoreCase("GROWING")) {
            return CropStatus.GROWING;
        }
        if (normalized.equalsIgnoreCase("R\u00E9colt\u00E9") ||
                normalized.equalsIgnoreCase("HARVESTED")) {
            return CropStatus.HARVESTED;
        }
        if (normalized.equalsIgnoreCase("Abandonn\u00E9") ||
                normalized.equalsIgnoreCase("FAILED")) {
            return CropStatus.FAILED;
        }
        throw new IllegalArgumentException("Unknown crop status from database: " + status);
    }
}

