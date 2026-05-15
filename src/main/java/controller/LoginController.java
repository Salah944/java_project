package controller;

import app.App;
import dto.UserResponseDTO;
import exceptions.AuthenticationException;
import exceptions.BusinessException;
import exceptions.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.io.IOException;

public class LoginController {

    private final AuthController authController = new AuthController();

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    @FXML
    public void initialize() {
        errorLabel.setText("");
        passwordField.setOnAction(event -> handleLogin());
        emailField.textProperty().addListener((observable, oldValue, newValue) -> clearError());
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> clearError());
    }

    @FXML
    public void handleLogin() {
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();

        if (email.isBlank()) {
            showError("Veuillez saisir votre email.");
            emailField.requestFocus();
            return;
        }
        if (password.isBlank()) {
            showError("Veuillez saisir votre mot de passe.");
            passwordField.requestFocus();
            return;
        }

        loginButton.setDisable(true);
        try {
            UserResponseDTO user = authController.login(email, password);
            if (user == null) {
                showError("Authentification impossible.");
                return;
            }
            App.showDashboard();
        } catch (AuthenticationException e) {
            showError(e.getMessage());
        } catch (ValidationException e) {
            showError(e.getMessage());
        } catch (BusinessException e) {
            showError(e.getMessage());
        } catch (IOException e) {
            showError("Impossible d'ouvrir le dashboard.");
        } catch (RuntimeException e) {
            showError("Connexion impossible. Verifiez la base de donn\u00e9es.");
            e.printStackTrace();
        } finally {
            loginButton.setDisable(false);
        }
    }

    private void clearError() {
        errorLabel.setText("");
    }

    private void showError(String message) {
        errorLabel.setText(message == null || message.isBlank() ? "Une erreur est survenue." : message);
    }

    @FXML
    public void goToSignup(MouseEvent event) {
        try {
            App.showSignup();
        } catch (IOException e) {
            showError("Impossible d'ouvrir la page d'inscription.");
        }
    }
}
