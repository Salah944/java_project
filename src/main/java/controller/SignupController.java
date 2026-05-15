package controller;

import app.App;
import exceptions.BusinessException;
import exceptions.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import model.enums.Role;
import services.UserService;

import java.io.IOException;

public class SignupController {

    private final UserService userService = new UserService();

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button signupButton;

    @FXML
    private void initialize() {
        errorLabel.setText("");
        passwordField.setOnAction(event -> handleSignup());
        nameField.textProperty().addListener((observable, oldValue, newValue) -> clearError());
        emailField.textProperty().addListener((observable, oldValue, newValue) -> clearError());
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> clearError());
    }

    @FXML
    private void handleSignup() {
        String name = nameField.getText() == null ? "" : nameField.getText().trim();
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();

        if (name.isBlank()) {
            showError("Veuillez saisir votre nom.");
            nameField.requestFocus();
            return;
        }
        if (email.isBlank()) {
            showError("Veuillez saisir votre email.");
            emailField.requestFocus();
            return;
        }
        if (password.isBlank()) {
            showError("Veuillez saisir un mot de passe.");
            passwordField.requestFocus();
            return;
        }

        signupButton.setDisable(true);
        try {
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setRole(Role.MANAGER); // Default role for self-signup

            userService.createUser(newUser);

            // Navigate back to login with a success message (or just navigate)
            goToLogin();
        } catch (ValidationException | BusinessException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Erreur lors de l'inscription. Verifiez la base de donnees.");
        } finally {
            signupButton.setDisable(false);
        }
    }

    @FXML
    private void goToLogin() {
        try {
            App.showLogin();
        } catch (IOException e) {
            showError("Impossible d'ouvrir la page de connexion.");
        }
    }

    private void clearError() {
        errorLabel.setText("");
    }

    private void showError(String message) {
        errorLabel.setText(message == null || message.isBlank() ? "Une erreur est survenue." : message);
    }
}
