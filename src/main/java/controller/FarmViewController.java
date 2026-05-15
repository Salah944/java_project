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
            showAlert("Erreur", "Impossible de charger les fermes : " + e.getMessage());
        }
    }

    @FXML
    private void handleAdd() {
        if (validateInput()) {
            Farm farm = new Farm(0, nameField.getText(), locationField.getText());
            farmController.createFarm(farm);
            clearFields();
            loadFarms();
        }
    }

    @FXML
    private void handleUpdate() {
        Farm selected = farmTable.getSelectionModel().getSelectedItem();
        if (selected != null && validateInput()) {
            Farm updated = new Farm(selected.getId(), nameField.getText(), locationField.getText());
            farmController.updateFarm(updated, selected.getId());
            clearFields();
            loadFarms();
        } else {
            showAlert("Selection", "Veuillez selectionner une ferme a modifier.");
        }
    }

    @FXML
    private void handleDelete() {
        Farm selected = farmTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            farmController.deleteFarm(selected.getId());
            clearFields();
            loadFarms();
        } else {
            showAlert("Selection", "Veuillez selectionner une ferme a supprimer.");
        }
    }

    @FXML
    private void handleBack() throws IOException {
        App.showDashboard();
    }

    private boolean validateInput() {
        if (nameField.getText() == null || nameField.getText().trim().isEmpty() || 
            locationField.getText() == null || locationField.getText().trim().isEmpty()) {
            showAlert("Erreur", "Tous les champs sont obligatoires.");
            return false;
        }
        return true;
    }

    private void clearFields() {
        nameField.clear();
        locationField.clear();
        farmTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
