package service;

import dao.TaskDAO;
import dao.TaskDAOImpl;
import model.Task;

import java.util.List;

public class TaskService {

    private final TaskDAO taskDAO = new TaskDAOImpl();

    public void createTask(Task task) {
        if (task.getDescription() == null || task.getDescription().isEmpty()) {
            System.out.println("Erreur : la description est obligatoire.");
            return;
        }
        if (task.getDate() == null) {
            System.out.println("Erreur : la date est obligatoire.");
            return;
        }
        taskDAO.create(task);
    }

    public List<Task> getAllTasks() {
        return taskDAO.getAll();
    }

    public Task getTaskById(int id) {
        if (id <= 0) {
            System.out.println("Erreur : id invalide.");
            return null;
        }
        return taskDAO.getById(id);
    }

    public void updateTask(Task task, int id) {
        if (id <= 0) {
            System.out.println("Erreur : id invalide.");
            return;
        }
        taskDAO.update(task, id);
    }

    public void deleteTask(int id) {
        if (id <= 0) {
            System.out.println("Erreur : id invalide.");
            return;
        }
        taskDAO.delete(id);
    }
}