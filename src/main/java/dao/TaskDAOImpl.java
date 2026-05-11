package dao;

import database.ConnectionDb;
import model.Task;
import model.enums.TaskStatus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskDAOImpl implements TaskDAO {

    private static final String TASK_SELECT =
            "SELECT t.*, ot.ouvrier_id AS worker_id FROM Task t " +
            "LEFT JOIN Ouvrier_Task ot ON t.id = ot.task_id";

    @Override
    public Task create(Task task) {
        String sql = "INSERT INTO Task (farm_id, description, status, date) VALUES (?, ?, ?, ?)";
        try (Connection cnx = ConnectionDb.getConnection()) {
            cnx.setAutoCommit(false);
            try (PreparedStatement stmt = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, task.getFarmId());
                stmt.setString(2, task.getDescription());
                stmt.setString(3, toDatabaseStatus(task.getStatus()));
                stmt.setDate(4, new java.sql.Date(task.getDueDate().getTime()));
                stmt.executeUpdate();
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        task.setId(keys.getInt(1));
                    }
                }
                if (task.getWorkerId() != null) {
                    assignWorkerInternal(cnx, task.getId(), task.getWorkerId());
                }
                cnx.commit();
                return task;
            } catch (SQLException e) {
                cnx.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating task: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Task> getAll() {
        return fetchTasks(TASK_SELECT, (Integer) null);
    }

    @Override
    public Optional<Task> getById(int id) {
        List<Task> tasks = fetchTasks(TASK_SELECT + " WHERE t.id = ?", id);
        return tasks.isEmpty() ? Optional.empty() : Optional.of(tasks.get(0));
    }

    @Override
    public List<Task> getByWorker(int workerId) {
        return fetchTasks(TASK_SELECT + " WHERE ot.ouvrier_id = ?", workerId);
    }

    @Override
    public List<Task> getByFarm(int farmId) {
        return fetchTasks(TASK_SELECT + " WHERE t.farm_id = ?", farmId);
    }

    @Override
    public List<Task> getByStatus(TaskStatus status) {
        return fetchTasks(TASK_SELECT + " WHERE t.status = ?", toDatabaseStatus(status));
    }

    @Override
    public Task update(Task task, int id) {
        String sql = "UPDATE Task SET farm_id = ?, description = ?, status = ?, date = ? WHERE id = ?";
        try (Connection cnx = ConnectionDb.getConnection()) {
            cnx.setAutoCommit(false);
            try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
                stmt.setInt(1, task.getFarmId());
                stmt.setString(2, task.getDescription());
                stmt.setString(3, toDatabaseStatus(task.getStatus()));
                stmt.setDate(4, new java.sql.Date(task.getDueDate().getTime()));
                stmt.setInt(5, id);
                stmt.executeUpdate();
                if (task.getWorkerId() != null) {
                    assignWorkerInternal(cnx, id, task.getWorkerId());
                }
                cnx.commit();
                task.setId(id);
                return task;
            } catch (SQLException e) {
                cnx.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating task: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) {
        try (Connection cnx = ConnectionDb.getConnection()) {
            cnx.setAutoCommit(false);
            try (PreparedStatement linkStmt = cnx.prepareStatement("DELETE FROM Ouvrier_Task WHERE task_id = ?");
                 PreparedStatement taskStmt = cnx.prepareStatement("DELETE FROM Task WHERE id = ?")) {
                linkStmt.setInt(1, id);
                linkStmt.executeUpdate();
                taskStmt.setInt(1, id);
                boolean deleted = taskStmt.executeUpdate() > 0;
                cnx.commit();
                return deleted;
            } catch (SQLException e) {
                cnx.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting task: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean assignWorker(int taskId, int workerId) {
        try (Connection cnx = ConnectionDb.getConnection()) {
            cnx.setAutoCommit(false);
            try {
                boolean assigned = assignWorkerInternal(cnx, taskId, workerId);
                cnx.commit();
                return assigned;
            } catch (SQLException e) {
                cnx.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error assigning worker to task: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateStatus(int taskId, TaskStatus status) {
        String sql = "UPDATE Task SET status = ? WHERE id = ?";
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, toDatabaseStatus(status));
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
                            fromDatabaseStatus(rs.getString("status")),
                            rs.getDate("date")
                    );
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching tasks: " + e.getMessage(), e);
        }
        return tasks;
    }

    private List<Task> fetchTasks(String sql, String param) {
        List<Task> tasks = new ArrayList<>();
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, param);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Task task = new Task(
                            rs.getInt("id"),
                            rs.getInt("farm_id"),
                            rs.getObject("worker_id") != null ? rs.getInt("worker_id") : null,
                            rs.getString("description"),
                            fromDatabaseStatus(rs.getString("status")),
                            rs.getDate("date")
                    );
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching tasks: " + e.getMessage(), e);
        }
        return tasks;
    }

    private boolean assignWorkerInternal(Connection cnx, int taskId, int workerId) throws SQLException {
        try (PreparedStatement deleteStmt = cnx.prepareStatement("DELETE FROM Ouvrier_Task WHERE task_id = ?");
             PreparedStatement insertStmt = cnx.prepareStatement("INSERT INTO Ouvrier_Task (ouvrier_id, task_id) VALUES (?, ?)")) {
            deleteStmt.setInt(1, taskId);
            deleteStmt.executeUpdate();
            insertStmt.setInt(1, workerId);
            insertStmt.setInt(2, taskId);
            return insertStmt.executeUpdate() > 0;
        }
    }

    private String toDatabaseStatus(TaskStatus status) {
        switch (status) {
            case PENDING:
                return "\u00C0 faire";
            case IN_PROGRESS:
                return "En cours";
            case DONE:
                return "Termin\u00E9";
            default:
                throw new IllegalArgumentException("Unsupported task status for database: " + status);
        }
    }

    private TaskStatus fromDatabaseStatus(String status) {
        if (status == null) {
            return null;
        }
        String normalized = status.trim();
        if (normalized.equalsIgnoreCase("\u00C0 faire") || normalized.equalsIgnoreCase("PENDING")) {
            return TaskStatus.PENDING;
        }
        if (normalized.equalsIgnoreCase("En cours") || normalized.equalsIgnoreCase("IN_PROGRESS")) {
            return TaskStatus.IN_PROGRESS;
        }
        if (normalized.equalsIgnoreCase("Termin\u00E9") || normalized.equalsIgnoreCase("DONE")) {
            return TaskStatus.DONE;
        }
        if (normalized.equalsIgnoreCase("CANCELLED")) {
            return TaskStatus.CANCELLED;
        }
        throw new IllegalArgumentException("Unknown task status from database: " + status);
    }
}
