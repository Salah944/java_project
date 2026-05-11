package services;

import dao.UserDAO;
import dao.UserDAOImpl;
import model.User;

import java.util.List;

public class UserService {

    private final UserDAO userDAO = new UserDAOImpl();

    public void createUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            System.out.println("Erreur : le nom est obligatoire.");
            return;
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            System.out.println("Erreur : l'email est obligatoire.");
            return;
        }
        if (user.getPassword() == null || user.getPassword().length() < 4) {
            System.out.println("Erreur : le mot de passe doit contenir au moins 4 caractères.");
            return;
        }
        userDAO.create(user);
    }

    public List<User> getAllUsers() {
        return userDAO.getAll();
    }

    public User getUserById(int id) {
        if (id <= 0) {
            System.out.println("Erreur : id invalide.");
            return null;
        }
        return userDAO.getById(id);
    }

    public void updateUser(User user, int id) {
        if (id <= 0) {
            System.out.println("Erreur : id invalide.");
            return;
        }
        userDAO.update(user, id);
    }

    public void deleteUser(int id) {
        if (id <= 0) {
            System.out.println("Erreur : id invalide.");
            return;
        }
        userDAO.delete(id);
    }
}
