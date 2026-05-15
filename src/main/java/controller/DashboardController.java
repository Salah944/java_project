package controller;

import app.App;
import exceptions.BusinessException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.Farm;
import java.io.IOException;
import java.util.List;

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
                "Worker Management",
                "Animal Management",
                "Stock Management",
                "Task Management",
                "Cultiver Management"
        );
        navigationList.getSelectionModel().selectFirst();

        authController.getCurrentUser().ifPresent(user ->
                userLabel.setText(user.getName() + "  [" + user.getRole() + "]")
        );

        refreshDashboard();
    }

    @FXML
    private void refreshDashboard() {
        statusLabel.setText("");
        try {
            List<Farm> farms = farmController.getAllFarms();

            long totalAnimals = 0;
            long totalWorkers = 0;
            long totalTasks   = 0;
            long totalStocks  = 0;

            for (Farm farm : farms) {
                totalAnimals += farmController.countAnimals(farm.getId());
                totalWorkers += farmController.countWorkers(farm.getId());
                totalTasks   += farmController.countTasks(farm.getId());
                totalStocks  += farmController.countStocks(farm.getId());
            }

            farmsCountLabel.setText(String.valueOf(farms.size()));
            animalsCountLabel.setText(String.valueOf(totalAnimals));
            workersCountLabel.setText(String.valueOf(totalWorkers));
            tasksCountLabel.setText(String.valueOf(totalTasks));
            stocksCountLabel.setText(String.valueOf(totalStocks));

            if (farms.isEmpty()) {
                statusLabel.setText("Aucune ferme. Commencez par creer une ferme dans 'Farm Management'.");
            } else {
                statusLabel.setText("Donnees actualisees. " + farms.size() + " ferme(s) trouvee(s).");
            }

        } catch (BusinessException e) {
            showError("Erreur metier", e.getMessage());
        } catch (RuntimeException e) {
            showError("Erreur de chargement", "Impossible de charger les statistiques. Verifiez la base de donnees.");
        }
    }

    @FXML
    private void openSelectedModule() {
        String module = navigationList.getSelectionModel().getSelectedItem();
        if (module == null) return;
        try {
            switch (module) {
                case "Farm Management":    App.showFarmManagement();    break;
                case "Worker Management":  App.showWorkerManagement();  break;
                case "Animal Management":  App.showAnimalManagement();  break;
                case "Stock Management":   App.showStockManagement();   break;
                case "Task Management":    App.showTaskManagement();    break;
                case "Cultiver Management":App.showCultiverManagement();break;
                default: showInfo("Module inconnu", module);
            }
        } catch (IOException e) {
            showError("Erreur de navigation", "Impossible d'ouvrir le module : " + module);
        }
    }

    @FXML
    private void logout() {
        authController.logout();
        try {
            App.showLogin();
        } catch (IOException e) {
            showError("Erreur", "Retour login impossible.");
        }
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
