package services;

import dao.TaskDAO;
import dao.TaskDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import dao.FarmDAO;
import dao.FarmDAOImpl;
import exceptions.NotFoundException;
import exceptions.ValidationException;
import model.Task;
import model.enums.TaskStatus;
import java.util.List;

public class TaskService {

    private final TaskDAO taskDAO = new TaskDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();
    private final FarmDAO farmDAO = new FarmDAOImpl();

    public Task createTask(Task task) {
        validateTask(task);
        if (task.getStatus() == null) task.setStatus(TaskStatus.PENDING);
        return taskDAO.create(task);
    }

    public List<Task> getAllTasks() {
        return taskDAO.getAll();
    }

    public Task getTaskById(int id) {
        return taskDAO.getById(id)
                .orElseThrow(() -> new NotFoundException("Tâche non trouvée avec l'id : " + id));
    }

    public Task updateTask(Task task, int id) {
        if (!taskDAO.getById(id).isPresent()) {
            throw new NotFoundException("Tâche non trouvée avec l'id : " + id);
        }
        validateTask(task);
        return taskDAO.update(task, id);
    }

    public boolean deleteTask(int id) {
        if (!taskDAO.getById(id).isPresent()) {
            throw new NotFoundException("Tâche non trouvée avec l'id : " + id);
        }
        return taskDAO.delete(id);
    }

    public boolean assignTaskToWorker(int taskId, int workerId) {
        if (!taskDAO.getById(taskId).isPresent()) {
            throw new NotFoundException("Tâche non trouvée avec l'id : " + taskId);
        }
        if (!userDAO.getById(workerId).isPresent()) {
            throw new NotFoundException("Ouvrier non trouvé avec l'id : " + workerId);
        }
        return taskDAO.assignWorker(taskId, workerId);
    }

    public boolean updateTaskStatus(int taskId, TaskStatus status) {
        if (!taskDAO.getById(taskId).isPresent()) {
            throw new NotFoundException("Tâche non trouvée avec l'id : " + taskId);
        }
        return taskDAO.updateStatus(taskId, status);
    }

    private void validateTask(Task task) {
        if (task.getFarmId() <= 0) {
            throw new ValidationException("L'id de la ferme est invalide.");
        }
        if (!farmDAO.getById(task.getFarmId()).isPresent()) {
            throw new NotFoundException("Ferme non trouvée avec l'id : " + task.getFarmId());
        }
        if (task.getDescription() == null || task.getDescription().isBlank()) {
            throw new ValidationException("La description de la tâche est obligatoire.");
        }
        if (task.getDueDate() == null) {
            throw new ValidationException("La date d'échéance est obligatoire.");
        }
    }
}
