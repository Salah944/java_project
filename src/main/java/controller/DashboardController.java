package controller;

import app.App;
import exceptions.BusinessException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.User;
import model.enums.Role;
import java.io.IOException;

public class DashboardController {

    private final AuthController authController = new AuthController();
    private final FarmController farmController = new FarmController();

    @FXML private Label userLabel;
    @FXML private Label farmsCountLabel;
    @FXML private Label animalsCountLabel;
    @FXML private Label workersCountLabel;
    @FXML private Label tasksCountLabel;
    @FXML private Label stocksCountLabel;
    @FXML private Label statusLabel;
    @FXML private ListView<String> navigationList;

    @FXML
    private void initialize() {
        navigationList.getItems().setAll(
                "Farm Management",
                "Animal Management",
                "Stock Management",
                "Task Management",
                "Cultiver Management"
        );
        
        authController.getCurrentUser().ifPresent(user -> {
            userLabel.setText(user.getName() + " [" + user.getRole() + "]");
            if (user.getRole() == Role.ADMIN) {
                if (!navigationList.getItems().contains("Worker Management")) {
                    navigationList.getItems().add("Worker Management");
                }
            }
        });
        
        navigationList.getSelectionModel().selectFirst();
        refreshDashboard();
    }

    @FXML
    private void refreshDashboard() {
        statusLabel.setText("");
        try {
            User currentUser = authController.getCurrentUser().orElse(null);
            if (currentUser == null || currentUser.getFarmId() == null) {
                resetStats();
                statusLabel.setText("Aucune ferme associ\u00e9e.");
                return;
            }

            int farmId = currentUser.getFarmId();
            farmsCountLabel.setText("1");
            animalsCountLabel.setText(String.valueOf(farmController.countAnimals(farmId)));
            workersCountLabel.setText(String.valueOf(farmController.countWorkers(farmId)));
            tasksCountLabel.setText(String.valueOf(farmController.countTasks(farmId)));
            stocksCountLabel.setText(String.valueOf(farmController.countStocks(farmId)));
            statusLabel.setText("Donn\u00e9es de votre ferme actualis\u00e9es.");
        } catch (BusinessException e) {
            showError("Erreur m\u00e9tier", e.getMessage());
        } catch (RuntimeException e) {
            showError("Erreur de chargement", "Impossible de charger les statistiques.");
            e.printStackTrace();
        }
    }

    private void resetStats() {
        farmsCountLabel.setText("0");
        animalsCountLabel.setText("0");
        workersCountLabel.setText("0");
        tasksCountLabel.setText("0");
        stocksCountLabel.setText("0");
    }

    @FXML
    private void openSelectedModule() {
        String module = navigationList.getSelectionModel().getSelectedItem();
        if (module == null) return;
        try {
            switch (module) {
                case "Farm Management": App.showFarmManagement(); break;
                case "Animal Management": App.showAnimalManagement(); break;
                case "Stock Management": App.showStockManagement(); break;
                case "Task Management": App.showTaskManagement(); break;
                case "Cultiver Management": App.showCultiverManagement(); break;
                case "Worker Management": App.showWorkerManagement(); break;
                default: showInfo("Module inconnu", module);
            }
        } catch (IOException e) {
            showError("Erreur de navigation", "Impossible d'ouvrir le module " + module);
        }
    }

    @FXML
    private void logout() {
        authController.logout();
        try { App.showLogin(); } catch (IOException e) { showError("Erreur", "Retour login impossible."); }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
