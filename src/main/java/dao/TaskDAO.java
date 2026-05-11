package dao;

import model.Task;
import model.enums.TaskStatus;
import java.util.List;
import java.util.Optional;

public interface TaskDAO {
    Task create(Task task);
    List<Task> getAll();
    Optional<Task> getById(int id);
    List<Task> getByWorker(int workerId);
    List<Task> getByFarm(int farmId);
    List<Task> getByStatus(TaskStatus status);
    Task update(Task task, int id);
    boolean delete(int id);
    boolean assignWorker(int taskId, int workerId);
    boolean updateStatus(int taskId, TaskStatus status);
}
