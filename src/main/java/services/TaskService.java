package services;

import dao.TaskDAO;
import dao.TaskDAOImpl;
import dao.OuvrierDAO;
import dao.OuvrierDAOImpl;
import dao.FarmDAO;
import dao.FarmDAOImpl;
import exceptions.NotFoundException;
import exceptions.ValidationException;
import model.Ouvrier;
import model.Task;
import model.enums.TaskStatus;
import java.util.List;

public class TaskService {

    private final TaskDAO taskDAO = new TaskDAOImpl();
    private final OuvrierDAO ouvrierDAO = new OuvrierDAOImpl();
    private final FarmDAO farmDAO = new FarmDAOImpl();

    public Task createTask(Task task) {
        if (task.getStatus() == null) task.setStatus(TaskStatus.PENDING);
        validateTask(task);
        return taskDAO.create(task);
    }

    public List<Task> getAllTasks() {
        return taskDAO.getAll();
    }

    public Task getTaskById(int id) {
        return taskDAO.getById(id)
                .orElseThrow(() -> new NotFoundException("Tâche non trouvée avec l'id : " + id));
    }

    public List<Task> getTasksByWorker(int workerId) {
        if (!ouvrierDAO.getById(workerId).isPresent()) {
            throw new NotFoundException("Ouvrier non trouvé avec l'id : " + workerId);
        }
        return taskDAO.getByWorker(workerId);
    }

    public List<Task> getTasksByFarm(int farmId) {
        if (!farmDAO.getById(farmId).isPresent()) {
            throw new NotFoundException("Ferme non trouvée avec l'id : " + farmId);
        }
        return taskDAO.getByFarm(farmId);
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        validateSupportedStatus(status);
        return taskDAO.getByStatus(status);
    }

    public List<Task> searchTaskByStatus(TaskStatus status) {
        return getTasksByStatus(status);
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
        Task task = taskDAO.getById(taskId).orElse(null);
        if (task == null) {
            throw new NotFoundException("Tâche non trouvée avec l'id : " + taskId);
        }
        validateAssignedWorker(workerId, task.getFarmId());
        return taskDAO.assignWorker(taskId, workerId);
    }

    public boolean updateTaskStatus(int taskId, TaskStatus status) {
        if (!taskDAO.getById(taskId).isPresent()) {
            throw new NotFoundException("Tâche non trouvée avec l'id : " + taskId);
        }
        validateSupportedStatus(status);
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
        validateSupportedStatus(task.getStatus());
        if (task.getWorkerId() != null) {
            validateAssignedWorker(task.getWorkerId(), task.getFarmId());
        }
    }

    private void validateAssignedWorker(int workerId, int farmId) {
        Ouvrier ouvrier = ouvrierDAO.getById(workerId)
                .orElseThrow(() -> new NotFoundException("Ouvrier non trouvé avec l'id : " + workerId));
        if (ouvrier.getFarmId() != farmId) {
            throw new ValidationException("L'ouvrier n'appartient pas a cette ferme.");
        }
    }

    private void validateSupportedStatus(TaskStatus status) {
        if (status == null) {
            throw new ValidationException("Le statut de tâche est obligatoire.");
        }
        if (status == TaskStatus.CANCELLED) {
            throw new ValidationException("Le statut CANCELLED n'est pas autorisé par la base de données.");
        }
    }
}
