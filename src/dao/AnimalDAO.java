package dao;

import database.ConnectionDb;
import model.Vache;
import model.Poulet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class AnimalDAO {

    public boolean addVache(Vache vache) {
        String animalSql = "INSERT INTO Animals (farm_id, age, healthStatus, type) VALUES (?, ?, ?, ?)";
        String vacheSql = "INSERT INTO Vaches (animal_id, milkProduction) VALUES (?, ?)";

        try (Connection conn = ConnectionDb.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement animalStmt = conn.prepareStatement(animalSql, Statement.RETURN_GENERATED_KEYS)) {
                animalStmt.setInt(1, vache.getFarmId());
                animalStmt.setInt(2, vache.getAge());
                animalStmt.setString(3, vache.getHealthStatus());
                animalStmt.setString(4, vache.getType());

                animalStmt.executeUpdate();

                try (ResultSet generatedKeys = animalStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int animalId = generatedKeys.getInt(1);

                        try (PreparedStatement vacheStmt = conn.prepareStatement(vacheSql)) {
                            vacheStmt.setInt(1, animalId);
                            vacheStmt.setDouble(2, vache.getMilkProduction());
                            vacheStmt.executeUpdate();
                        }

                        conn.commit();
                        return true;
                    }
                }
            }

            conn.rollback();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean addPoulet(Poulet poulet) {
        String animalSql = "INSERT INTO Animals (farm_id, age, healthStatus, type) VALUES (?, ?, ?, ?)";
        String pouletSql = "INSERT INTO Poulets (animal_id, eggProduction) VALUES (?, ?)";

        try (Connection conn = ConnectionDb.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement animalStmt = conn.prepareStatement(animalSql, Statement.RETURN_GENERATED_KEYS)) {
                animalStmt.setInt(1, poulet.getFarmId());
                animalStmt.setInt(2, poulet.getAge());
                animalStmt.setString(3, poulet.getHealthStatus());
                animalStmt.setString(4, poulet.getType());

                animalStmt.executeUpdate();

                try (ResultSet generatedKeys = animalStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int animalId = generatedKeys.getInt(1);

                        try (PreparedStatement pouletStmt = conn.prepareStatement(pouletSql)) {
                            pouletStmt.setInt(1, animalId);
                            pouletStmt.setInt(2, poulet.getEggProduction());
                            pouletStmt.executeUpdate();
                        }

                        conn.commit();
                        return true;
                    }
                }
            }

            conn.rollback();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
