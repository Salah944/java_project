package service;

import dao.UserDAO;
import model.User;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public boolean addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            return false;
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return false;
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            return false;
        }

        if (user.getRole() == null || user.getRole().isBlank()) {
            return false;
        }

        if (userDAO.existsByEmail(user.getEmail())) {
            return false;
        }

        return userDAO.addUser(user);
    }
}
