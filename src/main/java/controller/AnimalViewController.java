package controller;

import app.App;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Animal;
import model.Poulet;
import model.Vache;

import java.io.IOException;

public class AnimalViewController {
    private final AnimalController animalController = new AnimalController();
    private ObservableList<Animal> animalList;

    @FXML private TableView<Animal> animalTable;
    @FXML private TableColumn<Animal, Integer> colId;
    @FXML private TableColumn<Animal, Integer> colFarmId;
    @FXML private TableColumn<Animal, Integer> colAge;
    @FXML private TableColumn<Animal, String> colHealth;

    @FXML private ComboBox<String> typeComboBox;
    @FXML private TextField farmIdField;
    @FXML private TextField ageField;
    @FXML private TextField healthField;
    @FXML private TextField productionField; 

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colFarmId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getFarmId()).asObject());
        colAge.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAge()).asObject());
        colHealth.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHealthStatus()));

        typeComboBox.setItems(FXCollections.observableArrayList("Vache", "Poulet"));
        typeComboBox.getSelectionModel().selectFirst();

        loadAnimals();
    }

    private void loadAnimals() {
        try {
            animalList = FXCollections.observableArrayList(animalController.getAllAnimals());
            animalTable.setItems(animalList);
        } catch(Exception e) {
            showAlert("Erreur", "Impossible de charger les animaux : " + e.getMessage());
        }
    }

    @FXML
    private void handleAdd() {
        try {
            int farmId = Integer.parseInt(farmIdField.getText());
            int age = Integer.parseInt(ageField.getText());
            String health = healthField.getText();
            double production = Double.parseDouble(productionField.getText());

            if (typeComboBox.getValue().equals("Vache")) {
                Vache v = new Vache(0, farmId, age, health, production);
                animalController.addVache(v);
            } else {
                Poulet p = new Poulet(0, farmId, age, health, (int)production);
                animalController.addPoulet(p);
            }
            loadAnimals();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des donnees valides.");
        }
    }

    @FXML
    private void handleDelete() {
        Animal selected = animalTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            animalController.deleteAnimal(selected.getId());
            loadAnimals();
        } else {
            showAlert("Erreur", "Veuillez selectionner un animal.");
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
