package services;

import dao.UserDAO;
import dao.UserDAOImpl;
import exceptions.NotFoundException;
import exceptions.ValidationException;
import model.User;
import model.enums.Role;
import util.PasswordHasher;
import util.ValidationUtils;
import java.util.List;

public class UserService {
    private final UserDAO userDAO = new UserDAOImpl();

    public User createUser(User user) {
        validateUser(user);
        user.setPassword(PasswordHasher.hashPassword(user.getPassword()));
        return userDAO.create(user);
    }

    public List<User> getAllUsers() { return userDAO.getAll(); }
    public List<User> getAllWorkers() { return userDAO.getAllWorkers(); }

    public User getUserById(int id) {
        return userDAO.getById(id).orElseThrow(() -> new NotFoundException("Utilisateur non trouvé."));
    }

    public User createWorker(User worker) {
        worker.setRole(Role.OUVRIER);
        return createUser(worker);
    }

    public User getWorkerById(int id) {
        return userDAO.getWorkerById(id).orElseThrow(() -> new NotFoundException("Ouvrier non trouvé."));
    }

    public User updateWorker(User worker, int id) {
        worker.setRole(Role.OUVRIER);
        return updateUser(worker, id);
    }

    public boolean deleteWorker(int id) {
        getWorkerById(id);
        return userDAO.delete(id);
    }

    public User searchUserByEmail(String email) {
        ValidationUtils.validateEmail(email);
        return userDAO.findByEmail(email).orElseThrow(() -> new NotFoundException("Utilisateur non trouvé."));
    }

    public User updateUser(User user, int id) {
        if (!userDAO.getById(id).isPresent()) throw new NotFoundException("Utilisateur non trouvé.");
        validateUser(user);
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(PasswordHasher.hashPassword(user.getPassword()));
        }
        return userDAO.update(user, id);
    }

    public boolean deleteUser(int id) {
        if (!userDAO.getById(id).isPresent()) throw new NotFoundException("Utilisateur non trouvé.");
        return userDAO.delete(id);
    }

    private void validateUser(User user) {
        ValidationUtils.validateNotEmpty(user.getName(), "nom");
        ValidationUtils.validateEmail(user.getEmail());
        ValidationUtils.validateMinLength(user.getPassword(), 4, "mot de passe");
        if (user.getRole() == null) throw new ValidationException("Le rôle est obligatoire.");
    }
}
