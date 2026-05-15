package controller;
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
import app.App;
import exceptions.BusinessException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.User;
import model.enums.Role;
import services.UserService;
import java.io.IOException;

public class SignupController {
<<<<<<< Updated upstream
    private final UserService userService = new UserService();
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField farmIdField;
=======

    private final UserService userService = new UserService();

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
>>>>>>> Stashed changes
    @FXML private Label errorLabel;
    @FXML private Button signupButton;

    @FXML
    private void initialize() {
        errorLabel.setText("");
    }

    @FXML
    public void handleSignup() {
<<<<<<< Updated upstream
        try {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            String farmIdStr = farmIdField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || farmIdStr.isEmpty()) {
                showError("Tous les champs sont obligatoires.");
                return;
            }

=======
        String name = nameField.getText() == null ? "" : nameField.getText().trim();
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();
        String roleStr = roleComboBox.getValue();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || roleStr == null) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        signupButton.setDisable(true);
        try {
>>>>>>> Stashed changes
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPassword(password);
<<<<<<< Updated upstream
            newUser.setRole(Role.ADMIN);
            newUser.setFarmId(Integer.parseInt(farmIdStr));

            userService.createUser(newUser);
            App.showLogin();
        } catch (NumberFormatException e) {
            showError("L'ID de la ferme doit \u00eatre un nombre.");
        } catch (Exception e) {
            showError(e.getMessage());
=======
            newUser.setRole(Role.valueOf(roleStr));

            userService.createUser(newUser);
            
            // Navigate to login after success
            App.showLogin();
        } catch (BusinessException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Erreur lors de l'inscription. Verifiez la base de donn\u00e9es.");
            e.printStackTrace();
        } finally {
            signupButton.setDisable(false);
>>>>>>> Stashed changes
        }
    }

    @FXML
    public void goToLogin() {
<<<<<<< Updated upstream
        try { App.showLogin(); } catch (IOException e) { showError("Retour impossible."); }
=======
        try {
            App.showLogin();
        } catch (IOException e) {
            showError("Impossible de charger la page de connexion.");
        }
>>>>>>> Stashed changes
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }
}
