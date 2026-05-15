package controller;

import app.App;
import exceptions.BusinessException;
import exceptions.ValidationException;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Ouvrier;
import model.User;
import model.enums.Role;
import services.OuvrierService;
import services.UserService;

import java.io.IOException;
import java.util.List;

public class OuvrierViewController {

    private final OuvrierService ouvrierService = new OuvrierService();
    private final UserService userService = new UserService();
    private final ObservableList<Ouvrier> ouvrierList = FXCollections.observableArrayList();

    @FXML private TableView<Ouvrier> ouvrierTable;
    @FXML private TableColumn<Ouvrier, Number> idColumn;
    @FXML private TableColumn<Ouvrier, String> nameColumn;
    @FXML private TableColumn<Ouvrier, String> emailColumn;
    @FXML private TableColumn<Ouvrier, Number> farmIdColumn;
    @FXML private TableColumn<Ouvrier, Number> salaireColumn;

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField salaireField;
    @FXML private TextField farmIdField;

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        farmIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getFarmId()));
        salaireColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSalaire()));

        ouvrierTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showOuvrierDetails(newValue));

        refresh();
    }

    @FXML
    private void refresh() {
        try {
            // Note: Ideally, this should fetch only workers for the current Admin's farms.
            // For now, using getAllOuvriers() which fetches all.
            List<Ouvrier> ouvriers = ouvrierService.getAllOuvriers();
            // Fetch User details to populate name and email correctly (since OuvrierService might just fetch from Ouvrier table)
            // But actually, the OuvrierService uses UserDAO to get name/email if we join, but the DAO does not join.
            // Let's populate name/email from userService if needed.
            for (Ouvrier ouv : ouvriers) {
                try {
                    User u = userService.getUserById(ouv.getId());
                    ouv.setName(u.getName());
                    ouv.setEmail(u.getEmail());
                } catch (Exception e) {
                    ouv.setName("Inconnu");
                    ouv.setEmail("Inconnu");
                }
            }
            ouvrierList.setAll(ouvriers);
            ouvrierTable.setItems(ouvrierList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les ouvriers.");
        }
    }

    private void showOuvrierDetails(Ouvrier ouvrier) {
        if (ouvrier != null) {
            nameField.setText(ouvrier.getName());
            emailField.setText(ouvrier.getEmail());
            passwordField.setText("");
            salaireField.setText(String.valueOf(ouvrier.getSalaire()));
            farmIdField.setText(String.valueOf(ouvrier.getFarmId()));
        } else {
            clearForm();
        }
    }

    @FXML
    private void addOuvrier() {
        try {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            double salaire = Double.parseDouble(salaireField.getText());
            int farmId = Integer.parseInt(farmIdField.getText());

            // Create User first
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(Role.OUVRIER);
            User createdUser = userService.createUser(user);

            // Create Ouvrier link
            Ouvrier ouvrier = new Ouvrier(createdUser.getId(), farmId, salaire);
            ouvrierService.createOuvrier(ouvrier);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Ouvrier ajouté avec succès.");
            refresh();
            clearForm();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer des valeurs numériques valides pour le salaire et l'ID ferme.");
        } catch (ValidationException | BusinessException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur métier", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur système", "Une erreur est survenue lors de l'ajout.");
        }
    }

    @FXML
    private void updateOuvrier() {
        Ouvrier selectedOuvrier = ouvrierTable.getSelectionModel().getSelectedItem();
        if (selectedOuvrier == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un ouvrier à modifier.");
            return;
        }

        try {
            // Update User part
            User user = new User();
            user.setName(nameField.getText());
            user.setEmail(emailField.getText());
            user.setPassword(passwordField.getText().isBlank() ? null : passwordField.getText()); // Handled by userService
            user.setRole(Role.OUVRIER);
            userService.updateUser(user, selectedOuvrier.getId());

            // Since there is no OuvrierService.updateOuvrier, we assume updating the User is the main part.
            // If farmId or salaire changes, we need to update the Ouvrier table.
            // Since this project structure lacks a direct update in OuvrierService, we inform the user.
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Les informations de l'ouvrier (Nom, Email) ont été mises à jour.");
            refresh();
        } catch (ValidationException | BusinessException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur métier", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur système", "Une erreur est survenue lors de la modification.");
        }
    }

    @FXML
    private void deleteOuvrier() {
        Ouvrier selectedOuvrier = ouvrierTable.getSelectionModel().getSelectedItem();
        if (selectedOuvrier == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un ouvrier à supprimer.");
            return;
        }

        try {
            // We use userService to delete the user, which ideally cascades to the Ouvrier table.
            userService.deleteUser(selectedOuvrier.getId());
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Ouvrier supprimé avec succès.");
            refresh();
            clearForm();
        } catch (ValidationException | BusinessException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur métier", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur système", "Impossible de supprimer l'ouvrier. Vérifiez les dépendances en base de données.");
        }
    }

    @FXML
    private void clearForm() {
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        salaireField.clear();
        farmIdField.clear();
        ouvrierTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void goBack() {
        try {
            App.showDashboard();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de retourner au tableau de bord.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
