package dao;

import model.Task;
import java.util.List;

public interface TaskDAO {
    void create(Task task);
    List<Task> getAll();
    Task getById(int id);
    void update(Task task, int id);
    void delete(int id);
}