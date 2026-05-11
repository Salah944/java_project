package util;

import model.User;
import java.util.Optional;

public class SessionManager {
    private static User currentUser;

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static boolean isAuthenticated() {
        return currentUser != null;
    }

    public static Optional<User> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }
}
