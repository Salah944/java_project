package dao;

import database.ConnectionDb;
import model.Ouvrier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OuvrierDAOImpl implements OuvrierDAO {

    @Override
    public void create(Ouvrier ouvrier) {
        try {
            Connection cnx = ConnectionDb.getConnection();
            // Ouvrier hérite de User : on insère d'abord dans Users, puis dans Ouvriers
            PreparedStatement stmtUser = cnx.prepareStatement(
                    "INSERT INTO Users (name, email, password, role) VALUES (?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            stmtUser.setString(1, ouvrier.getName());
            stmtUser.setString(2, ouvrier.getEmail());
            stmtUser.setString(3, ouvrier.getPassword());
            stmtUser.setString(4, "OUVRIER");
            stmtUser.executeUpdate();

            ResultSet keys = stmtUser.getGeneratedKeys();
            if (keys.next()) {
                int userId = keys.getInt(1);
                PreparedStatement stmtOuvrier = cnx.prepareStatement(
                        "INSERT INTO Ouvriers (user_id, salaire, role) VALUES (?, ?, ?)"
                );
                stmtOuvrier.setInt(1, userId);
                stmtOuvrier.setDouble(2, ouvrier.getSalaire());
                stmtOuvrier.setString(3, ouvrier.getRole());

                int rows = stmtOuvrier.executeUpdate();
                if (rows > 0) System.out.println("Ouvrier créé avec succès.");
                else          System.out.println("Echec de création de l'ouvrier.");
            }

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Ouvrier> getAll() {
        List<Ouvrier> ouvriers = new ArrayList<>();
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement(
                    "SELECT u.id, u.name, u.email, u.password, o.salaire, o.role " +
                            "FROM Users u JOIN Ouvriers o ON u.id = o.user_id"
            );
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ouvriers.add(new Ouvrier(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getDouble("salaire"),
                        rs.getString("role")
                ));
            }

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ouvriers;
    }

    @Override
    public Ouvrier getById(int id) {
        Ouvrier ouvrier = null;
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement(
                    "SELECT u.id, u.name, u.email, u.password, o.salaire, o.role " +
                            "FROM Users u JOIN Ouvriers o ON u.id = o.user_id WHERE u.id = ?"
            );
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ouvrier = new Ouvrier(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getDouble("salaire"),
                        rs.getString("role")
                );
            }

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ouvrier;
    }

    @Override
    public void update(Ouvrier ouvrier, int id) {
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement(
                    "UPDATE Ouvriers SET salaire = ?, role = ? WHERE user_id = ?"
            );
            stmt.setDouble(1, ouvrier.getSalaire());
            stmt.setString(2, ouvrier.getRole());
            stmt.setInt(3, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Ouvrier mis à jour.");
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
            // Suppression en cascade via Users (Ouvriers lié par FK)
            PreparedStatement stmt = cnx.prepareStatement("DELETE FROM Users WHERE id = ?");
            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Ouvrier supprimé.");
            else          System.out.println("Echec de suppression.");

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}