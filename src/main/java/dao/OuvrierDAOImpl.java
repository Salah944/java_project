package dao;

import database.ConnectionDb;
import model.Ouvrier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OuvrierDAOImpl implements OuvrierDAO {

    @Override
    public Ouvrier create(Ouvrier ouvrier) {
        String nextIdSql = "SELECT ISNULL(MAX(id), 0) + 1 FROM Ouvrier WITH (UPDLOCK, HOLDLOCK)";
        String insertSql = "INSERT INTO Ouvrier (id, salaire, role, farm_id) VALUES (?, ?, ?, ?)";
        try (Connection cnx = ConnectionDb.getConnection()) {
            cnx.setAutoCommit(false);
            try (PreparedStatement idStmt = cnx.prepareStatement(nextIdSql);
                 PreparedStatement insertStmt = cnx.prepareStatement(insertSql)) {
                if (ouvrier.getId() <= 0) {
                    try (ResultSet rs = idStmt.executeQuery()) {
                        if (rs.next()) {
                            ouvrier.setId(rs.getInt(1));
                        }
                    }
                }
                insertStmt.setInt(1, ouvrier.getId());
                insertStmt.setDouble(2, ouvrier.getSalaire());
                insertStmt.setString(3, ouvrier.getRole().name());
                insertStmt.setInt(4, ouvrier.getFarmId());
                insertStmt.executeUpdate();
                cnx.commit();
                return ouvrier;
            } catch (SQLException e) {
                cnx.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating worker: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Ouvrier> getAll() {
        return fetchOuvriers("SELECT id, salaire, role, farm_id FROM Ouvrier", null);
    }

    @Override
    public Optional<Ouvrier> getById(int id) {
        List<Ouvrier> ouvriers = fetchOuvriers("SELECT id, salaire, role, farm_id FROM Ouvrier WHERE id = ?", id);
        return ouvriers.isEmpty() ? Optional.empty() : Optional.of(ouvriers.get(0));
    }

    private List<Ouvrier> fetchOuvriers(String sql, Integer id) {
        List<Ouvrier> ouvriers = new ArrayList<>();
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            if (id != null) {
                stmt.setInt(1, id);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Ouvrier ouvrier = new Ouvrier(
                            rs.getInt("id"),
                            rs.getInt("farm_id"),
                            rs.getDouble("salaire")
                    );
                    ouvriers.add(ouvrier);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching workers: " + e.getMessage(), e);
        }
        return ouvriers;
    }
}
