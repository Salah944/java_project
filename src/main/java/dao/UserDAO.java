package dao;

import model.User;
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    User create(User user);
    List<User> getAll();
    List<User> getAllWorkers();
    Optional<User> getById(int id);
    Optional<User> getWorkerById(int id);
    Optional<User> findByEmail(String email);
    List<User> searchByEmail(String email);
    User update(User user, int id);
    boolean delete(int id);
}
