package services;

import dao.UserDAO;
import dao.UserDAOImpl;
import dto.LoginRequestDTO;
import dto.UserResponseDTO;
import exceptions.AuthenticationException;
import model.User;
import util.PasswordHasher;
import util.SessionManager;
import util.ValidationUtils;

public class AuthService {

    private final UserDAO userDAO = new UserDAOImpl();

    public UserResponseDTO login(LoginRequestDTO loginRequest) {
        ValidationUtils.validateEmail(loginRequest.getEmail());
        ValidationUtils.validateNotEmpty(loginRequest.getPassword(), "mot de passe");

        User user = userDAO.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AuthenticationException("Email ou mot de passe incorrect."));

        if (!PasswordHasher.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Email ou mot de passe incorrect.");
        }

        SessionManager.login(user);

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
