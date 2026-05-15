package controller;

import app.App;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Cultiver;
import model.enums.CropStatus;

import java.io.IOException;

public class CultiverViewController {
    private final CultiverController cultiverController = new CultiverController();
    private ObservableList<Cultiver> cultiverList;

    @FXML private TableView<Cultiver> cultiverTable;
    @FXML private TableColumn<Cultiver, Integer> colId;
    @FXML private TableColumn<Cultiver, Integer> colFarmId;
    @FXML private TableColumn<Cultiver, String> colName;
    @FXML private TableColumn<Cultiver, String> colStatus;

    @FXML private TextField farmIdField;
    @FXML private TextField nameField;
    @FXML private TextField quantityField;
    @FXML private ComboBox<CropStatus> statusComboBox;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colFarmId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getFarmId()).asObject());
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().name()));

        statusComboBox.setItems(FXCollections.observableArrayList(CropStatus.values()));
        statusComboBox.getSelectionModel().selectFirst();

        loadCultivers();
    }

    private void loadCultivers() {
        try {
            cultiverList = FXCollections.observableArrayList(cultiverController.getAllCultivers());
            cultiverTable.setItems(cultiverList);
        } catch(Exception e) {
            showAlert("Erreur", "Impossible de charger les cultures : " + e.getMessage());
        }
    }

    @FXML
    private void handleAdd() {
        try {
            int farmId = Integer.parseInt(farmIdField.getText());
            String name = nameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            CropStatus status = statusComboBox.getValue();

            Cultiver cultiver = new Cultiver(0, farmId, name, new java.sql.Date(System.currentTimeMillis()), null, quantity, status);
            cultiverController.createCultiver(cultiver);
            loadCultivers();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des donnees valides.");
        }
    }

    @FXML
    private void handleDelete() {
        Cultiver selected = cultiverTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            cultiverController.deleteCultiver(selected.getId());
            loadCultivers();
        } else {
            showAlert("Erreur", "Veuillez selectionner une culture.");
        }
    }

    @FXML
    private void handleBack() throws IOException {
        App.showDashboard();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
