package controller;

import app.App;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Farm;

import java.io.IOException;

public class FarmViewController {

    private final FarmController farmController = new FarmController();
    private ObservableList<Farm> farmList;

    @FXML private TableView<Farm> farmTable;
    @FXML private TableColumn<Farm, Integer> colId;
    @FXML private TableColumn<Farm, String> colName;
    @FXML private TableColumn<Farm, String> colLocation;

    @FXML private TextField nameField;
    @FXML private TextField locationField;
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colLocation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));

        farmTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameField.setText(newSelection.getName());
                locationField.setText(newSelection.getLocation());
            }
        });

        loadFarms();
    }

    private void loadFarms() {
        try {
            farmList = FXCollections.observableArrayList(farmController.getAllFarms());
            farmTable.setItems(farmList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les fermes : " + e.getMessage());
        }
    }

    @FXML
    private void handleAdd() {
        String name = nameField.getText() == null ? "" : nameField.getText().trim();
        String location = locationField.getText() == null ? "" : locationField.getText().trim();

        if (name.isEmpty() || location.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Le nom et la localisation sont obligatoires.");
            return;
        }

        try {
            Farm farm = new Farm(0, name, location);
            farmController.createFarm(farm);
            showAlert(Alert.AlertType.INFORMATION, "Succes", "Ferme creee avec succes.");
            clearFields();
            loadFarms();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de creer la ferme : " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        Farm selected = farmTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Selection", "Veuillez selectionner une ferme a modifier.");
            return;
        }
        String name = nameField.getText() == null ? "" : nameField.getText().trim();
        String location = locationField.getText() == null ? "" : locationField.getText().trim();
        if (name.isEmpty() || location.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Le nom et la localisation sont obligatoires.");
            return;
        }
        try {
            Farm updated = new Farm(selected.getId(), name, location);
            farmController.updateFarm(updated, selected.getId());
            showAlert(Alert.AlertType.INFORMATION, "Succes", "Ferme mise a jour avec succes.");
            clearFields();
            loadFarms();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de modifier la ferme : " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Farm selected = farmTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Selection", "Veuillez selectionner une ferme a supprimer.");
            return;
        }
        try {
            farmController.deleteFarm(selected.getId());
            showAlert(Alert.AlertType.INFORMATION, "Succes", "Ferme supprimee avec succes.");
            clearFields();
            loadFarms();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer : " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() throws IOException {
        App.showDashboard();
    }

    private void clearFields() {
        nameField.clear();
        locationField.clear();
        farmTable.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
