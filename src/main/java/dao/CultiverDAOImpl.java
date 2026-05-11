package dao;

import database.ConnectionDb;
import model.Cultiver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CultiverDAOImpl implements CultiverDAO {

    @Override
    public void create(Cultiver cultiver) {
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement(
                    "INSERT INTO Cultiver (name, planning_date, herves_date, quantity, status) VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setString(1, cultiver.getName());
            stmt.setDate(2, new java.sql.Date(cultiver.getPlanningDate().getTime()));
            stmt.setDate(3, new java.sql.Date(cultiver.getHervesDate().getTime()));
            stmt.setInt(4, cultiver.getQuantity());
            stmt.setString(5, cultiver.getStatus());

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Culture créée avec succès.");
            else          System.out.println("Echec de création.");

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Cultiver> getAll() {
        List<Cultiver> list = new ArrayList<>();
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement("SELECT * FROM Cultiver");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Cultiver(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("planning_date"),   // java.sql.Date ✓
                        rs.getDate("herves_date"),      // java.sql.Date ✓
                        rs.getInt("quantity"),
                        rs.getString("status")
                ));
            }

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    @Override
    public Cultiver getById(int id) {
        Cultiver cultiver = null;
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement(
                    "SELECT * FROM Cultiver WHERE id = ?"
            );
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cultiver = new Cultiver(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("planning_date"),
                        rs.getDate("herves_date"),
                        rs.getInt("quantity"),
                        rs.getString("status")
                );
            }

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return cultiver;
    }

    @Override
    public void update(Cultiver cultiver, int id) {
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement(
                    "UPDATE Cultiver SET name = ?, planning_date = ?, herves_date = ?, quantity = ?, status = ? WHERE id = ?"
            );
            stmt.setString(1, cultiver.getName());
            stmt.setDate(2, new java.sql.Date(cultiver.getPlanningDate().getTime()));
            stmt.setDate(3, new java.sql.Date(cultiver.getHervesDate().getTime()));
            stmt.setInt(4, cultiver.getQuantity());
            stmt.setString(5, cultiver.getStatus());
            stmt.setInt(6, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Culture mise à jour.");
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
            PreparedStatement stmt = cnx.prepareStatement(
                    "DELETE FROM Cultiver WHERE id = ?"
            );
            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Culture supprimée.");
            else          System.out.println("Echec de suppression.");

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}