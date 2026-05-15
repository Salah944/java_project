package controller;

import app.App;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Stock;

import java.io.IOException;
import java.util.Date;

public class StockViewController {
    private final StockController stockController = new StockController();
    private ObservableList<Stock> stockList;

    @FXML private TableView<Stock> stockTable;
    @FXML private TableColumn<Stock, Integer> colId;
    @FXML private TableColumn<Stock, Integer> colFarmId;
    @FXML private TableColumn<Stock, String> colType;
    @FXML private TableColumn<Stock, Double> colQuantity;

    @FXML private TextField farmIdField;
    @FXML private TextField typeField;
    @FXML private TextField quantityField;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colFarmId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getFarmId()).asObject());
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        colQuantity.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getQuantity()).asObject());

        loadStocks();
    }

    private void loadStocks() {
        try {
            stockList = FXCollections.observableArrayList(stockController.getAllStocks());
            stockTable.setItems(stockList);
        } catch(Exception e) {
            showAlert("Erreur", "Impossible de charger les stocks : " + e.getMessage());
        }
    }

    @FXML
    private void handleAdd() {
        try {
            int farmId = Integer.parseInt(farmIdField.getText());
            String type = typeField.getText();
            double quantity = Double.parseDouble(quantityField.getText());

            Stock stock = new Stock(0, farmId, type, quantity, new Date());
            stockController.createStock(stock);
            loadStocks();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des donnees valides.");
        }
    }

    @FXML
    private void handleDelete() {
        Stock selected = stockTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            stockController.deleteStock(selected.getId());
            loadStocks();
        } else {
            showAlert("Erreur", "Veuillez selectionner un stock.");
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
