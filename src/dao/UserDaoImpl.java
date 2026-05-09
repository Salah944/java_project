package dao;

import database.ConnectionDb;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public void create(User user) {
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement(
                    "INSERT INTO Users (name, email, password, role) VALUES (?, ?, ?, ?)"
            );
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("User créé avec succès.");
            else          System.out.println("Echec de création.");

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement("SELECT * FROM Users");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    @Override
    public User getById(int id) {
        User user = null;
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement("SELECT * FROM Users WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    @Override
    public void update(User user, int id) {
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement(
                    "UPDATE Users SET name = ?, email = ?, password = ?, role = ? WHERE id = ?"
            );
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.setInt(5, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("User mis à jour.");
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
            PreparedStatement stmt = cnx.prepareStatement("DELETE FROM Users WHERE id = ?");
            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("User supprimé.");
            else          System.out.println("Echec de suppression.");

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}