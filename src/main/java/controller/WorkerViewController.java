package controller;
import app.App;
import exceptions.BusinessException;
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
import java.util.stream.Collectors;

public class WorkerViewController {
    private final OuvrierService ouvrierService = new OuvrierService();
    private final UserService userService = new UserService();
    private final AuthController authController = new AuthController();

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField salaireField;
    @FXML private Button saveButton;
    @FXML private Button deleteButton;

    @FXML private TableView<Ouvrier> workerTable;
    @FXML private TableColumn<Ouvrier, Integer> idCol;
    @FXML private TableColumn<Ouvrier, String> nameCol;
    @FXML private TableColumn<Ouvrier, String> emailCol;
    @FXML private TableColumn<Ouvrier, Double> salaireCol;

    private Ouvrier selectedWorker;

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        emailCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        salaireCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSalaire()).asObject());

        workerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> selectWorker(newVal));
        loadWorkers();
    }

    private void loadWorkers() {
        authController.getCurrentUser().ifPresent(user -> {
            Integer farmId = user.getFarmId();
            if (farmId != null) {
                ObservableList<Ouvrier> workers = FXCollections.observableArrayList(
                        ouvrierService.getAllOuvriers().stream()
                                .filter(w -> w.getFarmId() == farmId)
                                .map(w -> {
                                    userService.userDAO.getById(w.getId()).ifPresent(u -> {
                                        w.setName(u.getName());
                                        w.setEmail(u.getEmail());
                                    });
                                    return w;
                                })
                                .collect(Collectors.toList())
                );
                workerTable.setItems(workers);
            }
        });
    }

    private void selectWorker(Ouvrier worker) {
        selectedWorker = worker;
        if (worker != null) {
            nameField.setText(worker.getName());
            emailField.setText(worker.getEmail());
            passwordField.setText("");
            salaireField.setText(String.valueOf(worker.getSalaire()));
            deleteButton.setVisible(true);
            saveButton.setText("Modifier");
        } else {
            clearForm();
        }
    }

    @FXML
    public void handleSave() {
        try {
            User currentUser = authController.getCurrentUser().orElseThrow(() -> new BusinessException("Session expir\u00e9e"));
            if (currentUser.getFarmId() == null) throw new BusinessException("Aucune ferme associ\u00e9e \u00e0 votre compte.");

            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            double salaire = Double.parseDouble(salaireField.getText().trim());

            if (selectedWorker == null) {
                // CREATE
                User newUser = new User();
                newUser.setName(name);
                newUser.setEmail(email);
                newUser.setPassword(password.isEmpty() ? "1234" : password);
                newUser.setRole(Role.OUVRIER);
                newUser.setFarmId(currentUser.getFarmId());
                
                User createdUser = userService.createUser(newUser);
                
                Ouvrier newOuvrier = new Ouvrier();
                newOuvrier.setId(createdUser.getId());
                newOuvrier.setFarmId(currentUser.getFarmId());
                newOuvrier.setSalaire(salaire);
                
                ouvrierService.createOuvrier(newOuvrier);
            } else {
                // UPDATE
                User userToUpdate = userService.getUserById(selectedWorker.getId());
                userToUpdate.setName(name);
                userToUpdate.setEmail(email);
                if (!password.isEmpty()) userToUpdate.setPassword(password);
                userService.updateUser(userToUpdate, selectedWorker.getId());

                selectedWorker.setSalaire(salaire);
                ouvrierService.updateOuvrier(selectedWorker, selectedWorker.getId());
            }
            loadWorkers();
            clearForm();
        } catch (Exception e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    @FXML
    public void handleDelete() {
        if (selectedWorker != null) {
            try {
                ouvrierService.deleteOuvrier(selectedWorker.getId());
                userService.deleteUser(selectedWorker.getId());
                loadWorkers();
                clearForm();
            } catch (Exception e) {
                showAlert("Erreur", e.getMessage());
            }
        }
    }

    @FXML
    public void clearForm() {
        selectedWorker = null;
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        salaireField.clear();
        deleteButton.setVisible(false);
        saveButton.setText("Enregistrer");
        workerTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void goBack() throws IOException {
        App.showDashboard();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
