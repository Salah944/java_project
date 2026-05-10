package services;

import dao.UserDAO;
import dao.UserDAOImpl;
import dto.LoginRequestDTO;
import dto.UserResponseDTO;
import model.User;
import java.util.List;

public class AuthService {

    private final UserDAO userDAO = new UserDAOImpl();

    public UserResponseDTO login(LoginRequestDTO loginRequest) {

        // Validation des champs
        if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) {
            System.out.println("Erreur : l'email est obligatoire.");
            return null;
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            System.out.println("Erreur : le mot de passe est obligatoire.");
            return null;
        }

        // Cherche tous les users et vérifie email + password
        List<User> users = userDAO.getAll();
        for (User user : users) {
            if (user.getEmail().equals(loginRequest.getEmail()) &&
                    user.getPassword().equals(loginRequest.getPassword())) {

                // Connexion réussie → retourne UserResponseDTO (sans password)
                System.out.println("Connexion réussie. Bienvenue " + user.getName() + " !");
                return new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole()
                );
            }
        }

        System.out.println("Erreur : email ou mot de passe incorrect.");
        return null;
    }
}