package controller;
import app.App;
import exceptions.BusinessException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.User;
import model.enums.Role;
import services.UserService;
import java.io.IOException;

public class SignupController {
    private final UserService userService = new UserService();
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField farmIdField;
    @FXML private Label errorLabel;
    @FXML private Button signupButton;

    @FXML
    private void initialize() {
        errorLabel.setText("");
    }

    @FXML
    public void handleSignup() {
        try {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            String farmIdStr = farmIdField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || farmIdStr.isEmpty()) {
                showError("Tous les champs sont obligatoires.");
                return;
            }

            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setRole(Role.ADMIN);
            newUser.setFarmId(Integer.parseInt(farmIdStr));

            userService.createUser(newUser);
            App.showLogin();
        } catch (NumberFormatException e) {
            showError("L'ID de la ferme doit \u00eatre un nombre.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void goToLogin() {
        try { App.showLogin(); } catch (IOException e) { showError("Retour impossible."); }
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }
}
