import dto.FarmSummaryDTO;
import dto.UserResponseDTO;
import exceptions.BusinessException;
import model.Cultiver;
import model.Farm;
import model.Ouvrier;
import model.Poulet;
import model.Stock;
import model.Task;
import model.User;
import model.Vache;
import model.enums.CropStatus;
import model.enums.Role;
import model.enums.TaskStatus;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final controller.AuthController AUTH_CONTROLLER = new controller.AuthController();
    private static final controller.UserController USER_CONTROLLER = new controller.UserController();
    private static final controller.FarmController FARM_CONTROLLER = new controller.FarmController();
    private static final controller.OuvrierController OUVRIER_CONTROLLER = new controller.OuvrierController();
    private static final controller.AnimalController ANIMAL_CONTROLLER = new controller.AnimalController();
    private static final controller.StockController STOCK_CONTROLLER = new controller.StockController();
    private static final controller.TaskController TASK_CONTROLLER = new controller.TaskController();
    private static final controller.CultiverController CULTIVER_CONTROLLER = new controller.CultiverController();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            try {
                showMenu();
                int choice = readInt("Choice: ");

                switch (choice) {
                    case 1:
                        login();
                        break;
                    case 2:
                        addUser();
                        break;
                    case 3:
                        addFarm();
                        break;
                    case 4:
                        showFarms();
                        break;
                    case 5:
                        addPoulet();
                        break;
                    case 6:
                        addVache();
                        break;
                    case 7:
                        findFarmById();
                        break;
                    case 8:
                        updateFarm();
                        break;
                    case 9:
                        deleteFarm();
                        break;
                    case 10:
                        addStock();
                        break;
                    case 11:
                        showStocksByFarm();
                        break;
                    case 12:
                        addTask();
                        break;
                    case 13:
                        showTasksByFarm();
                        break;
                    case 14:
                        assignTaskToWorker();
                        break;
                    case 15:
                        updateTaskStatus();
                        break;
                    case 16:
                        addCultiver();
                        break;
                    case 17:
                        showCultiversByFarm();
                        break;
                    case 18:
                        updateCultiverStatus();
                        break;
                    case 19:
                        showFarmDashboard();
                        break;
                    case 20:
                        quickSearch();
                        break;
                    case 21:
                        addOuvrier();
                        break;
                    case 22:
                        showOuvriers();
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (BusinessException e) {
                System.out.println("Erreur metier : " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erreur inattendue : " + e.getMessage());
            }
        }

        System.out.println("Goodbye.");
    }

    private static void showMenu() {
        System.out.println();
        System.out.println("==== Farm Management ====");
        System.out.println("1. Login");
        System.out.println("2. Add user");
        System.out.println("3. Add farm");
        System.out.println("4. Show farms");
        System.out.println("5. Add poulet");
        System.out.println("6. Add vache");
        System.out.println("7. Find farm by id");
        System.out.println("8. Update farm");
        System.out.println("9. Delete farm");
        System.out.println("10. Add stock");
        System.out.println("11. Show stocks by farm");
        System.out.println("12. Add task");
        System.out.println("13. Show tasks by farm");
        System.out.println("14. Assign task to worker");
        System.out.println("15. Update task status");
        System.out.println("16. Add culture");
        System.out.println("17. Show cultures by farm");
        System.out.println("18. Update culture status");
        System.out.println("19. Farm dashboard");
        System.out.println("20. Quick search/filter");
        System.out.println("21. Add worker profile");
        System.out.println("22. Show workers");
        System.out.println("0. Exit");
    }

    private static void login() {
        String email = readRequiredString("Email: ");
        String password = readRequiredString("Password: ");

        UserResponseDTO response = AUTH_CONTROLLER.login(email, password);
        System.out.println("Login successful. Welcome " + response.getName());
    }

    private static void addUser() {
        String name = readRequiredString("Name: ");
        String email = readRequiredString("Email: ");
        String password = readRequiredString("Password: ");
        Role role = readRole();
        Integer farmId = null;
        Double salaire = null;

        if (role == Role.OUVRIER) {
            farmId = readInt("Farm id: ");
            salaire = readNonNegativeDouble("Salary: ");
        }

        User user = new User(0, name, email, password, role.name());
        User createdUser = USER_CONTROLLER.createUser(user);
        if (role == Role.OUVRIER) {
            OUVRIER_CONTROLLER.createOuvrier(new Ouvrier(createdUser.getId(), farmId, salaire));
            System.out.println("User and worker profile created successfully. Worker id: " + createdUser.getId());
        } else {
            System.out.println("User created successfully.");
        }
    }

    private static void addFarm() {
        String name = readRequiredString("Farm name: ");
        String location = readRequiredString("Location: ");

        FARM_CONTROLLER.createFarm(new Farm(0, name, location));
        System.out.println("Farm created successfully.");
    }

    private static void showFarms() {
        List<Farm> farms = FARM_CONTROLLER.getAllFarms();
        if (farms.isEmpty()) {
            System.out.println("No farms found.");
            return;
        }
        farms.forEach(System.out::println);
    }

    private static void addOuvrier() {
        int workerId = readPositiveInt("Existing OUVRIER user id: ");
        int farmId = readInt("Farm id: ");
        double salaire = readNonNegativeDouble("Salary: ");

        Ouvrier ouvrier = OUVRIER_CONTROLLER.createOuvrier(new Ouvrier(workerId, farmId, salaire));
        System.out.println("Worker created successfully. Worker id: " + ouvrier.getId());
    }

    private static void showOuvriers() {
        List<Ouvrier> ouvriers = OUVRIER_CONTROLLER.getAllOuvriers();
        if (ouvriers.isEmpty()) {
            System.out.println("No workers found.");
            return;
        }
        ouvriers.forEach(System.out::println);
    }

    private static void findFarmById() {
        int farmId = readInt("Farm id: ");
        Farm farm = FARM_CONTROLLER.getFarmById(farmId);
        System.out.println(farm);
    }

    private static void updateFarm() {
        int farmId = readInt("Farm id to update: ");
        String name = readRequiredString("New farm name: ");
        String location = readRequiredString("New location: ");

        FARM_CONTROLLER.updateFarm(new Farm(farmId, name, location), farmId);
        System.out.println("Farm updated successfully.");
    }

    private static void deleteFarm() {
        int farmId = readInt("Farm id to delete: ");
        if (readConfirmation("Confirm delete? (yes/no): ")) {
            FARM_CONTROLLER.deleteFarm(farmId);
            System.out.println("Farm deleted successfully.");
        }
    }

    private static void addPoulet() {
        int farmId = readInt("Farm id: ");
        int age = readNonNegativeInt("Age: ");
        String healthStatus = readRequiredString("Health status: ");
        int eggProduction = readNonNegativeInt("Egg production: ");

        ANIMAL_CONTROLLER.addPoulet(new Poulet(0, farmId, age, healthStatus, eggProduction));
        System.out.println("Poulet added successfully.");
    }

    private static void addVache() {
        int farmId = readInt("Farm id: ");
        int age = readNonNegativeInt("Age: ");
        String healthStatus = readRequiredString("Health status: ");
        double milkProduction = readNonNegativeDouble("Milk production: ");

        ANIMAL_CONTROLLER.addVache(new Vache(0, farmId, age, healthStatus, milkProduction));
        System.out.println("Vache added successfully.");
    }

    private static void addStock() {
        int farmId = readInt("Farm id: ");
        String type = readRequiredString("Stock type: ");
        double quantity = readNonNegativeDouble("Quantity: ");

        STOCK_CONTROLLER.createStock(new Stock(0, farmId, type, quantity, new java.util.Date()));
        System.out.println("Stock created successfully.");
    }

    private static void showStocksByFarm() {
        int farmId = readInt("Farm id: ");
        List<Stock> stocks = STOCK_CONTROLLER.getStocksByFarm(farmId);
        if (stocks.isEmpty()) {
            System.out.println("No stocks found.");
            return;
        }
        stocks.forEach(System.out::println);
    }

    private static void addTask() {
        int farmId = readInt("Farm id: ");
        int workerId = readInt("Worker id (0 for none): ");
        String description = readRequiredString("Description: ");
        TaskStatus status = readTaskStatus();
        java.util.Date dueDate = readDate("Due date (yyyy-mm-dd): ");

        Integer assignedWorker = workerId <= 0 ? null : workerId;
        TASK_CONTROLLER.createTask(new Task(0, farmId, assignedWorker, description, status, dueDate));
        System.out.println("Task created successfully.");
    }

    private static void showTasksByFarm() {
        int farmId = readInt("Farm id: ");
        List<Task> tasks = TASK_CONTROLLER.getTasksByFarm(farmId);
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
        tasks.forEach(System.out::println);
    }

    private static void assignTaskToWorker() {
        int taskId = readInt("Task id: ");
        int workerId = readInt("Worker id: ");
        TASK_CONTROLLER.assignTaskToWorker(taskId, workerId);
        System.out.println("Task assigned successfully.");
    }

    private static void updateTaskStatus() {
        int taskId = readInt("Task id: ");
        TaskStatus status = readTaskStatus();
        TASK_CONTROLLER.updateTaskStatus(taskId, status);
        System.out.println("Task status updated successfully.");
    }

    private static void addCultiver() {
        int farmId = readInt("Farm id: ");
        String name = readRequiredString("Culture name: ");
        java.util.Date planningDate = readDate("Planning date (yyyy-mm-dd): ");
        int quantity = readPositiveInt("Quantity: ");
        CropStatus status = readCropStatus();

        CULTIVER_CONTROLLER.createCultiver(new Cultiver(0, farmId, name, planningDate, null, quantity, status));
        System.out.println("Culture created successfully.");
    }

    private static void showCultiversByFarm() {
        int farmId = readInt("Farm id: ");
        List<Cultiver> cultivers = CULTIVER_CONTROLLER.getCultiversByFarm(farmId);
        if (cultivers.isEmpty()) {
            System.out.println("No cultures found.");
            return;
        }
        cultivers.forEach(System.out::println);
    }

    private static void updateCultiverStatus() {
        int cultiverId = readInt("Culture id: ");
        CropStatus status = readCropStatus();
        CULTIVER_CONTROLLER.updateCultiverStatus(cultiverId, status.name());
        System.out.println("Culture status updated successfully.");
    }

    private static void showFarmDashboard() {
        int farmId = readInt("Farm id: ");
        FarmSummaryDTO summary = FARM_CONTROLLER.getFarmSummary(farmId);
        System.out.println("Animals: " + summary.getAnimalsCount());
        System.out.println("Workers: " + summary.getWorkersCount());
        System.out.println("Tasks: " + summary.getTasksCount());
        System.out.println("Stocks: " + summary.getStocksCount());
    }

    private static void quickSearch() {
        System.out.println("1. Farm by name");
        System.out.println("2. User by email");
        System.out.println("3. Animal by type");
        System.out.println("4. Task by status");
        System.out.println("5. Stock by type");
        int choice = readInt("Choice: ");

        switch (choice) {
            case 1:
                FARM_CONTROLLER.searchFarmByName(readRequiredString("Farm name: ")).forEach(System.out::println);
                break;
            case 2:
                System.out.println(USER_CONTROLLER.searchUserByEmail(readRequiredString("Email: ")));
                break;
            case 3:
                ANIMAL_CONTROLLER.searchAnimalByType(readRequiredString("Animal type: ")).forEach(System.out::println);
                break;
            case 4:
                TASK_CONTROLLER.searchTaskByStatus(readTaskStatus()).forEach(System.out::println);
                break;
            case 5:
                STOCK_CONTROLLER.searchStockByType(readRequiredString("Stock type: ")).forEach(System.out::println);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static Role readRole() {
        while (true) {
            System.out.println("Available roles: 1. ADMIN, 2. OUVRIER");
            int choice = readInt("Choice: ");
            if (choice == 1) return Role.ADMIN;
            if (choice == 2) return Role.OUVRIER;
            System.out.println("Invalid choice.");
        }
    }

    private static TaskStatus readTaskStatus() {
        while (true) {
            System.out.println("Task statuses: 1. PENDING, 2. IN_PROGRESS, 3. DONE");
            int choice = readInt("Choice: ");
            if (choice == 1) return TaskStatus.PENDING;
            if (choice == 2) return TaskStatus.IN_PROGRESS;
            if (choice == 3) return TaskStatus.DONE;
            System.out.println("Invalid choice.");
        }
    }

    private static CropStatus readCropStatus() {
        while (true) {
            System.out.println("Crop statuses: 1. PLANTED, 2. GROWING, 3. HARVESTED, 4. FAILED");
            int choice = readInt("Choice: ");
            if (choice == 1) return CropStatus.PLANTED;
            if (choice == 2) return CropStatus.GROWING;
            if (choice == 3) return CropStatus.HARVESTED;
            if (choice == 4) return CropStatus.FAILED;
            System.out.println("Invalid choice.");
        }
    }

    private static String readRequiredString(String prompt) {
        String value;
        do {
            System.out.print(prompt);
            value = readLine();
            if (value.isEmpty()) System.out.println("This field is required.");
        } while (value.isEmpty());
        return value;
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(readLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid integer.");
            }
        }
    }

    private static int readNonNegativeInt(String prompt) {
        while (true) {
            int val = readInt(prompt);
            if (val >= 0) return val;
            System.out.println("Must be non-negative.");
        }
    }

    private static int readPositiveInt(String prompt) {
        while (true) {
            int val = readInt(prompt);
            if (val > 0) return val;
            System.out.println("Must be greater than 0.");
        }
    }

    private static double readNonNegativeDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(readLine());
                if (val >= 0) return val;
            } catch (NumberFormatException e) {
                // retry
            }
            System.out.println("Must be a non-negative number.");
        }
    }

    private static boolean readConfirmation(String prompt) {
        while (true) {
            System.out.print(prompt);
            String val = readLine();
            if (val.equalsIgnoreCase("yes") || val.equalsIgnoreCase("y")) return true;
            if (val.equalsIgnoreCase("no") || val.equalsIgnoreCase("n")) return false;
        }
    }

    private static java.util.Date readDate(String prompt) {
        while (true) {
            try {
                return Date.valueOf(readRequiredString(prompt));
            } catch (IllegalArgumentException e) {
                System.out.println("Enter a valid date like 2026-05-11.");
            }
        }
    }

    private static String readLine() {
        return SCANNER.hasNextLine() ? SCANNER.nextLine().trim() : "";
    }
}
