package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        primaryStage.setTitle("Farm Management");
        showLogin();
    }

    public static void showLogin() throws IOException {
        loadView("/views/LoginView.fxml", "Farm Management - Login", 900, 600);
    }

    public static void showDashboard() throws IOException {
        loadView("/views/DashboardView.fxml", "Farm Management - Dashboard", 1180, 760);
    }

    public static void showFarmManagement() throws IOException {
        loadView("/views/FarmView.fxml", "Farm Management", 1180, 760);
    }

    public static void showWorkerManagement() throws IOException {
        loadView("/views/WorkerView.fxml", "Worker Management", 1180, 760);
    }

    public static void showSignup() throws IOException {
        loadView("/views/SignupView.fxml", "Farm Management - Inscription", 900, 600);
    }

    public static void showAnimalManagement() throws IOException {
        loadView("/views/AnimalView.fxml", "Animal Management", 1180, 760);
    }

    public static void showStockManagement() throws IOException {
        loadView("/views/StockView.fxml", "Stock Management", 1180, 760);
    }

    public static void showTaskManagement() throws IOException {
        loadView("/views/TaskView.fxml", "Task Management", 1180, 760);
    }

    public static void showCultiverManagement() throws IOException {
        loadView("/views/CultiverView.fxml", "Cultiver Management", 1180, 760);
    }

    private static void loadView(String resource, String title, double width, double height) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(resource));
        Parent root = loader.load();

        Scene scene = new Scene(root, width, height);

        primaryStage.setTitle(title);
        primaryStage.setMinWidth(820);
        primaryStage.setMinHeight(560);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}