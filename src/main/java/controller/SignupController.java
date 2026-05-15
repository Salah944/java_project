package controller;

import app.App;
import exceptions.BusinessException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Label errorLabel;
    @FXML private Button signupButton;

    @FXML
    public void initialize() {
        errorLabel.setText("");
    }

    @FXML
    public void handleSignup() {
        String name = nameField.getText() == null ? "" : nameField.getText().trim();
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();
        String farmIdStr = farmIdField.getText() == null ? "" : farmIdField.getText().trim();
        String roleStr = roleComboBox.getValue();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || roleStr == null || farmIdStr.isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        signupButton.setDisable(true);
        try {
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setRole(Role.valueOf(roleStr));
            newUser.setFarmId(Integer.parseInt(farmIdStr));

            userService.createUser(newUser);
            
            App.showLogin();
        } catch (NumberFormatException e) {
            showError("L'ID de la ferme doit \u00eatre un nombre.");
        } catch (BusinessException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Erreur lors de l'inscription.");
            e.printStackTrace();
        } finally {
            signupButton.setDisable(false);
        }
    }

    @FXML
    public void goToLogin(MouseEvent event) {
        try {
            App.showLogin();
        } catch (IOException e) {
            showError("Impossible de charger la page de connexion.");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }
}
