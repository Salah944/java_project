package controller;

import app.App;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Task;
import model.enums.TaskStatus;

import java.io.IOException;

public class TaskViewController {
    private final TaskController taskController = new TaskController();
    private ObservableList<Task> taskList;

    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, Integer> colId;
    @FXML private TableColumn<Task, Integer> colFarmId;
    @FXML private TableColumn<Task, String> colDesc;
    @FXML private TableColumn<Task, String> colStatus;

    @FXML private TextField farmIdField;
    @FXML private TextField descField;
    @FXML private ComboBox<TaskStatus> statusComboBox;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colFarmId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getFarmId()).asObject());
        colDesc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().name()));

        statusComboBox.setItems(FXCollections.observableArrayList(TaskStatus.values()));
        statusComboBox.getSelectionModel().selectFirst();

        loadTasks();
    }

    private void loadTasks() {
        try {
            taskList = FXCollections.observableArrayList(taskController.getAllTasks());
            taskTable.setItems(taskList);
        } catch(Exception e) {
            showAlert("Erreur", "Impossible de charger les taches : " + e.getMessage());
        }
    }

    @FXML
    private void handleAdd() {
        try {
            int farmId = Integer.parseInt(farmIdField.getText());
            String desc = descField.getText();
            TaskStatus status = statusComboBox.getValue();

            Task task = new Task(0, farmId, null, desc, status, new java.sql.Date(System.currentTimeMillis()));
            taskController.createTask(task);
            loadTasks();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des donnees valides.");
        }
    }

    @FXML
    private void handleDelete() {
        Task selected = taskTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            taskController.deleteTask(selected.getId());
            loadTasks();
        } else {
            showAlert("Erreur", "Veuillez selectionner une tache.");
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
