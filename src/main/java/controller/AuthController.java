package controller;

import dto.LoginRequestDTO;
import dto.UserResponseDTO;
import model.User;
import services.AuthService;
import java.util.Optional;

public class AuthController {
    private final AuthService authService = new AuthService();

    public UserResponseDTO login(String email, String password) {
        LoginRequestDTO request = new LoginRequestDTO(email, password);
        return authService.login(request);
    }

    public void logout() {
        authService.logout();
    }

    public Optional<User> getCurrentUser() {
        return authService.getCurrentUser();
    }

    public boolean isAuthenticated() {
        return authService.isAuthenticated();
    }
}
