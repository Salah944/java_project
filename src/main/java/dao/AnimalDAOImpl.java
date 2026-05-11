package dao;

import database.ConnectionDb;
import model.Animal;
import model.Vache;
import model.Poulet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnimalDAOImpl implements AnimalDAO {

    @Override
    public Vache addVache(Vache vache) {
        String animalSql = "INSERT INTO Animal (farmId, age, healthStatus, type) VALUES (?, ?, ?, 'VACHE')";
        String vacheSql = "INSERT INTO Vache (animal_id, milkProduction) VALUES (?, ?)";

        try (Connection cnx = ConnectionDb.getConnection()) {
            cnx.setAutoCommit(false);
            try (PreparedStatement animalStmt = cnx.prepareStatement(animalSql, Statement.RETURN_GENERATED_KEYS)) {
                animalStmt.setInt(1, vache.getFarmId());
                animalStmt.setInt(2, vache.getAge());
                animalStmt.setString(3, vache.getHealthStatus());
                animalStmt.executeUpdate();

                try (ResultSet keys = animalStmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        int animalId = keys.getInt(1);
                        vache.setId(animalId);
                        try (PreparedStatement vacheStmt = cnx.prepareStatement(vacheSql)) {
                            vacheStmt.setInt(1, animalId);
                            vacheStmt.setDouble(2, vache.getMilkProduction());
                            vacheStmt.executeUpdate();
                            cnx.commit();
                            return vache;
                        }
                    }
                }
            } catch (SQLException e) {
                cnx.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding vache: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Poulet addPoulet(Poulet poulet) {
        String animalSql = "INSERT INTO Animal (farmId, age, healthStatus, type) VALUES (?, ?, ?, 'POULET')";
        String pouletSql = "INSERT INTO Poulet (animal_id, eggProduction) VALUES (?, ?)";

        try (Connection cnx = ConnectionDb.getConnection()) {
            cnx.setAutoCommit(false);
            try (PreparedStatement animalStmt = cnx.prepareStatement(animalSql, Statement.RETURN_GENERATED_KEYS)) {
                animalStmt.setInt(1, poulet.getFarmId());
                animalStmt.setInt(2, poulet.getAge());
                animalStmt.setString(3, poulet.getHealthStatus());
                animalStmt.executeUpdate();

                try (ResultSet keys = animalStmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        int animalId = keys.getInt(1);
                        poulet.setId(animalId);
                        try (PreparedStatement pouletStmt = cnx.prepareStatement(pouletSql)) {
                            pouletStmt.setInt(1, animalId);
                            pouletStmt.setInt(2, poulet.getEggProduction());
                            pouletStmt.executeUpdate();
                            cnx.commit();
                            return poulet;
                        }
                    }
                }
            } catch (SQLException e) {
                cnx.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding poulet: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Animal> getAll() {
        return fetchAnimals("SELECT a.*, v.milkProduction, p.eggProduction FROM Animal a LEFT JOIN Vache v ON a.id = v.animal_id LEFT JOIN Poulet p ON a.id = p.animal_id", (Integer) null);
    }

    @Override
    public List<Animal> getByFarm(int farmId) {
        return fetchAnimals("SELECT a.*, v.milkProduction, p.eggProduction FROM Animal a LEFT JOIN Vache v ON a.id = v.animal_id LEFT JOIN Poulet p ON a.id = p.animal_id WHERE a.farmId = ?", farmId);
    }

    @Override
    public List<Animal> getByType(String type) {
        return fetchAnimals("SELECT a.*, v.milkProduction, p.eggProduction FROM Animal a LEFT JOIN Vache v ON a.id = v.animal_id LEFT JOIN Poulet p ON a.id = p.animal_id WHERE UPPER(a.type) = UPPER(?)", type);
    }

    @Override
    public Optional<Animal> getById(int id) {
        List<Animal> animals = fetchAnimals("SELECT a.*, v.milkProduction, p.eggProduction FROM Animal a LEFT JOIN Vache v ON a.id = v.animal_id LEFT JOIN Poulet p ON a.id = p.animal_id WHERE a.id = ?", id);
        return animals.isEmpty() ? Optional.empty() : Optional.of(animals.get(0));
    }

    @Override
    public Animal update(Animal animal) {
        String animalSql = "UPDATE Animal SET age = ?, healthStatus = ? WHERE id = ?";
        try (Connection cnx = ConnectionDb.getConnection()) {
            cnx.setAutoCommit(false);
            try (PreparedStatement stmt = cnx.prepareStatement(animalSql)) {
                stmt.setInt(1, animal.getAge());
                stmt.setString(2, animal.getHealthStatus());
                stmt.setInt(3, animal.getId());
                stmt.executeUpdate();

                if (animal instanceof Vache) {
                    try (PreparedStatement vacheStmt = cnx.prepareStatement("UPDATE Vache SET milkProduction = ? WHERE animal_id = ?")) {
                        vacheStmt.setDouble(1, ((Vache) animal).getMilkProduction());
                        vacheStmt.setInt(2, animal.getId());
                        vacheStmt.executeUpdate();
                    }
                } else if (animal instanceof Poulet) {
                    try (PreparedStatement pouletStmt = cnx.prepareStatement("UPDATE Poulet SET eggProduction = ? WHERE animal_id = ?")) {
                        pouletStmt.setInt(1, ((Poulet) animal).getEggProduction());
                        pouletStmt.setInt(2, animal.getId());
                        pouletStmt.executeUpdate();
                    }
                }
                cnx.commit();
                return animal;
            } catch (SQLException e) {
                cnx.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating animal: " + e.getMessage(), e);
        }
    }

    @Override
    public Animal update(Animal animal, int id) {
        animal.setId(id);
        return update(animal);
    }

    @Override
    public boolean delete(int id) {
        try (Connection cnx = ConnectionDb.getConnection()) {
            cnx.setAutoCommit(false);
            try (PreparedStatement vacheStmt = cnx.prepareStatement("DELETE FROM Vache WHERE animal_id = ?");
                 PreparedStatement pouletStmt = cnx.prepareStatement("DELETE FROM Poulet WHERE animal_id = ?");
                 PreparedStatement animalStmt = cnx.prepareStatement("DELETE FROM Animal WHERE id = ?")) {
                vacheStmt.setInt(1, id);
                vacheStmt.executeUpdate();
                pouletStmt.setInt(1, id);
                pouletStmt.executeUpdate();
                animalStmt.setInt(1, id);
                boolean deleted = animalStmt.executeUpdate() > 0;
                cnx.commit();
                return deleted;
            } catch (SQLException e) {
                cnx.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting animal: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateHealthStatus(int animalId, String status) {
        String sql = "UPDATE Animal SET healthStatus = ? WHERE id = ?";
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, animalId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating animal health status: " + e.getMessage(), e);
        }
    }

    private List<Animal> fetchAnimals(String sql, Integer param) {
        List<Animal> animals = new ArrayList<>();
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            if (param != null) {
                stmt.setInt(1, param);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String type = rs.getString("type");
                    int id = rs.getInt("id");
                    int farmId = rs.getInt("farmId");
                    int age = rs.getInt("age");
                    String healthStatus = rs.getString("healthStatus");

                    if ("VACHE".equalsIgnoreCase(type)) {
                        animals.add(new Vache(id, farmId, age, healthStatus, rs.getDouble("milkProduction")));
                    } else if ("POULET".equalsIgnoreCase(type)) {
                        animals.add(new Poulet(id, farmId, age, healthStatus, rs.getInt("eggProduction")));
                    } else {
                        animals.add(new Animal(id, farmId, age, healthStatus, type));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching animals: " + e.getMessage(), e);
        }
        return animals;
    }

    private List<Animal> fetchAnimals(String sql, String param) {
        List<Animal> animals = new ArrayList<>();
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, param);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String type = rs.getString("type");
                    int id = rs.getInt("id");
                    int farmId = rs.getInt("farmId");
                    int age = rs.getInt("age");
                    String healthStatus = rs.getString("healthStatus");

                    if ("VACHE".equalsIgnoreCase(type)) {
                        animals.add(new Vache(id, farmId, age, healthStatus, rs.getDouble("milkProduction")));
                    } else if ("POULET".equalsIgnoreCase(type)) {
                        animals.add(new Poulet(id, farmId, age, healthStatus, rs.getInt("eggProduction")));
                    } else {
                        animals.add(new Animal(id, farmId, age, healthStatus, type));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching animals: " + e.getMessage(), e);
        }
        return animals;
    }
}
