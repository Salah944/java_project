package dao;

import database.ConnectionDb;
import model.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskDAOImpl implements TaskDAO {

    @Override
    public void create(Task task) {
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement(
                    "INSERT INTO Tasks (description, status, date) VALUES (?, ?, ?)"
            );
            stmt.setString(1, task.getDescription());
            stmt.setString(2, task.getStatus());
            stmt.setDate(3, new java.sql.Date(task.getDate().getTime()));

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Tâche créée avec succès.");
            else          System.out.println("Echec de création.");

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasks = new ArrayList<>();
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement("SELECT * FROM Tasks");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tasks.add(new Task(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getDate("date")        // java.sql.Date est sous-classe de java.util.Date ✓
                ));
            }

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tasks;
    }

    @Override
    public Task getById(int id) {
        Task task = null;
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement(
                    "SELECT * FROM Tasks WHERE id = ?"
            );
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                task = new Task(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getDate("date")
                );
            }

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return task;
    }

    @Override
    public void update(Task task, int id) {
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement(
                    "UPDATE Tasks SET description = ?, status = ?, date = ? WHERE id = ?"
            );
            stmt.setString(1, task.getDescription());
            stmt.setString(2, task.getStatus());
            stmt.setDate(3, new java.sql.Date(task.getDate().getTime()));
            stmt.setInt(4, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Tâche mise à jour.");
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
                    "DELETE FROM Tasks WHERE id = ?"
            );
            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Tâche supprimée.");
            else          System.out.println("Echec de suppression.");

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}