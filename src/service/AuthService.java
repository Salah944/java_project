package service;

import dao.UserDAO;
import dto.LoginRequestDTO;
import dto.UserResponseDTO;
import model.User;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    public UserResponseDTO login(LoginRequestDTO request) {

        User user = userDAO.findByEmailAndPassword(
                request.getEmail(),
                request.getPassword()
        );

        if (user == null) {
            return null;
        }

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}