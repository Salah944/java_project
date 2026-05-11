package dao;

import database.ConnectionDb;
import model.Task;
import model.enums.TaskStatus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskDAOImpl implements TaskDAO {

    @Override
    public Task create(Task task) {
        String sql = "INSERT INTO Tasks (farm_id, worker_id, description, status, due_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, task.getFarmId());
            if (task.getWorkerId() != null) stmt.setInt(2, task.getWorkerId());
            else                           stmt.setNull(2, Types.INTEGER);
            stmt.setString(3, task.getDescription());
            stmt.setString(4, task.getStatus().name());
            stmt.setDate(5, new java.sql.Date(task.getDueDate().getTime()));
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    task.setId(keys.getInt(1));
                }
            }
            return task;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating task: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Task> getAll() {
        return fetchTasks("SELECT * FROM Tasks", null);
    }

    @Override
    public Optional<Task> getById(int id) {
        List<Task> tasks = fetchTasks("SELECT * FROM Tasks WHERE id = ?", id);
        return tasks.isEmpty() ? Optional.empty() : Optional.of(tasks.get(0));
    }

    @Override
    public Task update(Task task, int id) {
        String sql = "UPDATE Tasks SET farm_id = ?, worker_id = ?, description = ?, status = ?, due_date = ? WHERE id = ?";
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, task.getFarmId());
            if (task.getWorkerId() != null) stmt.setInt(2, task.getWorkerId());
            else                           stmt.setNull(2, Types.INTEGER);
            stmt.setString(3, task.getDescription());
            stmt.setString(4, task.getStatus().name());
            stmt.setDate(5, new java.sql.Date(task.getDueDate().getTime()));
            stmt.setInt(6, id);
            stmt.executeUpdate();
            task.setId(id);
            return task;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating task: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Tasks WHERE id = ?";
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting task: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean assignWorker(int taskId, int workerId) {
        String sql = "UPDATE Tasks SET worker_id = ? WHERE id = ?";
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, workerId);
            stmt.setInt(2, taskId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error assigning worker to task: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateStatus(int taskId, TaskStatus status) {
        String sql = "UPDATE Tasks SET status = ? WHERE id = ?";
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, taskId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating task status: " + e.getMessage(), e);
        }
    }

    private List<Task> fetchTasks(String sql, Integer param) {
        List<Task> tasks = new ArrayList<>();
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            if (param != null) {
                stmt.setInt(1, param);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Task task = new Task(
                            rs.getInt("id"),
                            rs.getInt("farm_id"),
                            rs.getObject("worker_id") != null ? rs.getInt("worker_id") : null,
                            rs.getString("description"),
                            TaskStatus.valueOf(rs.getString("status")),
                            rs.getDate("due_date")
                    );
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching tasks: " + e.getMessage(), e);
        }
        return tasks;
    }
}
