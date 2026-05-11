package controller;

import model.Task;
import model.enums.TaskStatus;
import services.TaskService;
import java.util.List;

public class TaskController {
    private final TaskService taskService = new TaskService();

    public Task createTask(Task task) {
        return taskService.createTask(task);
    }

    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    public Task getTaskById(int id) {
        return taskService.getTaskById(id);
    }

    public List<Task> getTasksByWorker(int workerId) {
        return taskService.getTasksByWorker(workerId);
    }

    public List<Task> getTasksByFarm(int farmId) {
        return taskService.getTasksByFarm(farmId);
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskService.getTasksByStatus(status);
    }

    public List<Task> searchTaskByStatus(TaskStatus status) {
        return taskService.searchTaskByStatus(status);
    }

    public Task updateTask(Task task, int id) {
        return taskService.updateTask(task, id);
    }

    public boolean deleteTask(int id) {
        return taskService.deleteTask(id);
    }

    public boolean assignTaskToWorker(int taskId, int workerId) {
        return taskService.assignTaskToWorker(taskId, workerId);
    }

    public boolean updateTaskStatus(int taskId, TaskStatus status) {
        return taskService.updateTaskStatus(taskId, status);
    }
}
