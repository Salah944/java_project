package controller;

import model.User;
import services.UserService;
import java.util.List;

public class UserController {
    private final UserService userService = new UserService();

    public User createUser(User user) {
        return userService.createUser(user);
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public List<User> getAllWorkers() {
        return userService.getAllWorkers();
    }

    public User getUserById(int id) {
        return userService.getUserById(id);
    }

    public User updateUser(User user, int id) {
        return userService.updateUser(user, id);
    }

    public boolean deleteUser(int id) {
        return userService.deleteUser(id);
    }
}
