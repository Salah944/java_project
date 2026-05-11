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

    public User createWorker(User worker) {
        return userService.createWorker(worker);
    }

    public User getUserById(int id) {
        return userService.getUserById(id);
    }

    public User getWorkerById(int id) {
        return userService.getWorkerById(id);
    }

    public User searchUserByEmail(String email) {
        return userService.searchUserByEmail(email);
    }

    public List<User> searchUsersByEmail(String email) {
        return userService.searchUsersByEmail(email);
    }

    public User updateUser(User user, int id) {
        return userService.updateUser(user, id);
    }

    public User updateWorker(User worker, int id) {
        return userService.updateWorker(worker, id);
    }

    public boolean deleteUser(int id) {
        return userService.deleteUser(id);
    }

    public boolean deleteWorker(int id) {
        return userService.deleteWorker(id);
    }
}
