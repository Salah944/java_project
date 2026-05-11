

import dto.UserResponseDTO;
import exceptions.BusinessException;
import model.Farm;
import model.Poulet;
import model.User;
import model.Vache;
import model.enums.Role;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final controller.AuthController AUTH_CONTROLLER = new controller.AuthController();
    private static final controller.UserController USER_CONTROLLER = new controller.UserController();
    private static final controller.FarmController FARM_CONTROLLER = new controller.FarmController();
    private static final controller.AnimalController ANIMAL_CONTROLLER = new controller.AnimalController();

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
                    case 0:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (BusinessException e) {
                System.out.println("Erreur métier : " + e.getMessage());
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

        User user = new User(0, name, email, password, role.name());
        USER_CONTROLLER.createUser(user);
        System.out.println("User created successfully.");
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

    private static Role readRole() {
        while (true) {
            System.out.println("Available roles: 1. ADMIN, 2. OUVRIER");
            int choice = readInt("Choice: ");
            if (choice == 1) return Role.ADMIN;
            if (choice == 2) return Role.OUVRIER;
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
            try { return Integer.parseInt(readLine()); }
            catch (NumberFormatException e) { System.out.println("Enter a valid integer."); }
        }
    }

    private static int readNonNegativeInt(String prompt) {
        while (true) {
            int val = readInt(prompt);
            if (val >= 0) return val;
            System.out.println("Must be non-negative.");
        }
    }

    private static double readNonNegativeDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(readLine());
                if (val >= 0) return val;
            } catch (NumberFormatException e) {}
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

    private static String readLine() {
        return SCANNER.hasNextLine() ? SCANNER.nextLine().trim() : "";
    }
}

