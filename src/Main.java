import dto.LoginRequestDTO;
import dto.UserResponseDTO;
import model.Farm;
import model.Poulet;
import model.User;
import model.Vache;
import services.AnimalService;
import services.AuthService;
import services.FarmService;
import services.UserService;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final AuthService AUTH_SERVICE = new AuthService();
    private static final UserService USER_SERVICE = new UserService();
    private static final FarmService FARM_SERVICE = new FarmService();
    private static final AnimalService ANIMAL_SERVICE = new AnimalService();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            showMenu();
            int choice = readInt("Choice: ");

            switch (choice) {
                case 1 -> login();
                case 2 -> addUser();
                case 3 -> addFarm();
                case 4 -> showFarms();
                case 5 -> addPoulet();
                case 6 -> addVache();
                case 7 -> findFarmById();
                case 8 -> updateFarm();
                case 9 -> deleteFarm();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice.");
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

        LoginRequestDTO request = new LoginRequestDTO(email, password);
        UserResponseDTO response = AUTH_SERVICE.login(request);

        if (response == null) {
            System.out.println("Invalid email or password.");
            return;
        }

        System.out.println("Login successful.");
        System.out.println("Welcome " + response.getName());
        System.out.println("Role: " + response.getRole());
    }

    private static void addUser() {
        String name = readRequiredString("Name: ");
        String email = readRequiredString("Email: ");
        String password = readRequiredString("Password: ");
        String role = readRole();

        User user = new User(0, name, email, password, role);
        USER_SERVICE.createUser(user);
    }

    private static void addFarm() {
        String name = readRequiredString("Farm name: ");
        String location = readRequiredString("Location: ");

        FARM_SERVICE.createFarm(new Farm(0, name, location));
    }

    private static void showFarms() {
        List<Farm> farms = FARM_SERVICE.getAllFarms();

        if (farms.isEmpty()) {
            System.out.println("No farms found.");
            return;
        }

        for (Farm farm : farms) {
            System.out.println(farm);
        }
    }

    private static void findFarmById() {
        int farmId = readInt("Farm id: ");
        Farm farm = FARM_SERVICE.getFarmById(farmId);

        if (farm == null) {
            System.out.println("Farm not found.");
            return;
        }

        System.out.println(farm);
    }

    private static void updateFarm() {
        int farmId = readExistingFarmId();

        if (farmId == 0) {
            return;
        }

        Farm existingFarm = FARM_SERVICE.getFarmById(farmId);
        if (existingFarm == null) {
            System.out.println("Farm not found.");
            return;
        }

        System.out.println("Current farm: " + existingFarm);
        String name = readRequiredString("New farm name: ");
        String location = readRequiredString("New location: ");

        FARM_SERVICE.updateFarm(new Farm(farmId, name, location), farmId);
    }

    private static void deleteFarm() {
        int farmId = readExistingFarmId();

        if (farmId == 0) {
            return;
        }

        Farm existingFarm = FARM_SERVICE.getFarmById(farmId);
        if (existingFarm == null) {
            System.out.println("Farm not found.");
            return;
        }

        System.out.println("Farm to delete: " + existingFarm);
        if (!readConfirmation("Confirm delete? (yes/no): ")) {
            System.out.println("Delete canceled.");
            return;
        }

        FARM_SERVICE.deleteFarm(farmId);
    }

    private static void addPoulet() {
        int farmId = readExistingFarmId();

        if (farmId == 0) {
            return;
        }

        int age = readNonNegativeInt("Age: ");
        String healthStatus = readRequiredString("Health status: ");
        int eggProduction = readNonNegativeInt("Egg production: ");

        Poulet poulet = new Poulet(0, farmId, age, healthStatus, eggProduction);
        ANIMAL_SERVICE.addPoulet(poulet);
    }

    private static void addVache() {
        int farmId = readExistingFarmId();

        if (farmId == 0) {
            return;
        }

        int age = readNonNegativeInt("Age: ");
        String healthStatus = readRequiredString("Health status: ");
        double milkProduction = readNonNegativeDouble("Milk production: ");

        Vache vache = new Vache(0, farmId, age, healthStatus, milkProduction);
        ANIMAL_SERVICE.addVache(vache);
    }

    private static int readExistingFarmId() {
        List<Farm> farms = FARM_SERVICE.getAllFarms();

        if (farms.isEmpty()) {
            System.out.println("No farms found. Add a farm first.");
            return 0;
        }

        while (true) {
            System.out.println("Available farms:");
            for (Farm farm : farms) {
                System.out.println(farm);
            }
            System.out.println("Enter 0 to cancel.");

            int farmId = readInt("Farm id: ");

            if (farmId == 0 || farmExists(farms, farmId)) {
                return farmId;
            }

            System.out.println("Farm id not found.");
        }
    }

    private static boolean farmExists(List<Farm> farms, int farmId) {
        for (Farm farm : farms) {
            if (farm.getId() == farmId) {
                return true;
            }
        }

        return false;
    }

    private static String readRole() {
        while (true) {
            System.out.println("Available roles:");
            System.out.println("1. ADMIN");
            System.out.println("2. OUVRIER");

            int choice = readInt("Role: ");

            switch (choice) {
                case 1 -> {
                    return "ADMIN";
                }
                case 2 -> {
                    return "OUVRIER";
                }
                default -> System.out.println("Invalid role.");
            }
        }
    }

    private static String readRequiredString(String prompt) {
        String value;

        do {
            System.out.print(prompt);
            value = readLine();

            if (value.isEmpty()) {
                System.out.println("This field is required.");
            }
        } while (value.isEmpty());

        return value;
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = readLine();

            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid integer.");
            }
        }
    }

    private static int readNonNegativeInt(String prompt) {
        while (true) {
            int value = readInt(prompt);

            if (value >= 0) {
                return value;
            }

            System.out.println("Enter a value greater than or equal to 0.");
        }
    }

    private static double readNonNegativeDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = readLine();

            try {
                double parsed = Double.parseDouble(value);

                if (parsed >= 0) {
                    return parsed;
                }

                System.out.println("Enter a value greater than or equal to 0.");
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    private static boolean readConfirmation(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = readLine();

            if (value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("y")) {
                return true;
            }

            if (value.equalsIgnoreCase("no") || value.equalsIgnoreCase("n")) {
                return false;
            }

            System.out.println("Enter yes or no.");
        }
    }

    private static String readLine() {
        if (!SCANNER.hasNextLine()) {
            return "";
        }

        return SCANNER.nextLine()
                .replace("\uFEFF", "")
                .replace("\u0000", "")
                .trim();
    }
}
