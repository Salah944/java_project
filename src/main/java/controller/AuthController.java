package controller;

import dto.LoginRequestDTO;
import dto.UserResponseDTO;
import services.AuthService;

public class AuthController {
    private final AuthService authService = new AuthService();

    public UserResponseDTO login(String email, String password) {
        LoginRequestDTO request = new LoginRequestDTO(email, password);
        return authService.login(request);
    }
}
