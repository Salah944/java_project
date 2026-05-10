package controller;

import model.User;
import service.UserService;

import java.util.List;
import java.util.Scanner;

public class UserController {

    private final UserService userService = new UserService();
    private final Scanner scanner = new Scanner(System.in);

    public void menu() {
        int choix = -1;
        while (choix != 0) {
            System.out.println("\n===== MENU USER =====");
            System.out.println("1. Ajouter un user");
            System.out.println("2. Afficher tous les users");
            System.out.println("3. Afficher un user par id");
            System.out.println("4. Modifier un user");
            System.out.println("5. Supprimer un user");
            System.out.println("0. Retour");
            System.out.print("Votre choix : ");
            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1 -> addUser();
                case 2 -> getAllUsers();
                case 3 -> getUserById();
                case 4 -> updateUser();
                case 5 -> deleteUser();
                case 0 -> System.out.println("Retour au menu principal.");
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void addUser() {
        System.out.print("Nom : ");
        String name = scanner.nextLine();
        System.out.print("Email : ");
        String email = scanner.nextLine();
        System.out.print("Password : ");
        String password = scanner.nextLine();
        System.out.print("Role : ");
        String role = scanner.nextLine();

        User user = new User(0, name, email, password, role);
        userService.createUser(user);
    }

    private void getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("Aucun user trouvé.");
        } else {
            users.forEach(System.out::println);
        }
    }

    private void getUserById() {
        System.out.print("ID du user : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        User user = userService.getUserById(id);
        if (user != null) System.out.println(user);
        else System.out.println("User non trouvé.");
    }

    private void updateUser() {
        System.out.print("ID du user à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nouveau nom : ");
        String name = scanner.nextLine();
        System.out.print("Nouvel email : ");
        String email = scanner.nextLine();
        System.out.print("Nouveau password : ");
        String password = scanner.nextLine();
        System.out.print("Nouveau role : ");
        String role = scanner.nextLine();

        User user = new User(id, name, email, password, role);
        userService.updateUser(user, id);
    }

    private void deleteUser() {
        System.out.print("ID du user à supprimer : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        userService.deleteUser(id);
    }
}