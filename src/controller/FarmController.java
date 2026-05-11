package controller;

import model.Farm;
import services.FarmService;

import java.util.List;
import java.util.Scanner;

public class FarmController {

    private final FarmService farmService = new FarmService();
    private final Scanner scanner = new Scanner(System.in);

    public void menu() {
        int choice = -1;

        while (choice != 0) {
            System.out.println();
            System.out.println("===== MENU FARM =====");
            System.out.println("1. Ajouter une ferme");
            System.out.println("2. Afficher toutes les fermes");
            System.out.println("3. Trouver une ferme par id");
            System.out.println("4. Modifier une ferme");
            System.out.println("5. Supprimer une ferme");
            System.out.println("0. Retour");

            choice = readInt("Votre choix : ");

            switch (choice) {
                case 1 -> addFarm();
                case 2 -> displayAllFarms();
                case 3 -> displayFarm();
                case 4 -> updateFarm();
                case 5 -> removeFarm();
                case 0 -> System.out.println("Retour au menu principal.");
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    public void addFarm() {
        String name = readRequiredString("Nom : ");
        String location = readRequiredString("Localisation : ");

        farmService.createFarm(new Farm(0, name, location));
    }

    public void removeFarm() {
        int id = readInt("ID de la ferme a supprimer : ");
        Farm farm = farmService.getFarmById(id);

        if (farm == null) {
            System.out.println("Ferme non trouvee.");
            return;
        }

        farmService.deleteFarm(id);
    }

    public void updateFarm() {
        int id = readInt("ID de la ferme a modifier : ");
        Farm farm = farmService.getFarmById(id);

        if (farm == null) {
            System.out.println("Ferme non trouvee.");
            return;
        }

        String name = readRequiredString("Nouveau nom : ");
        String location = readRequiredString("Nouvelle localisation : ");

        farmService.updateFarm(new Farm(id, name, location), id);
    }

    public void displayFarm() {
        int id = readInt("ID de la ferme : ");
        Farm farm = farmService.getFarmById(id);

        if (farm == null) {
            System.out.println("Ferme non trouvee.");
            return;
        }

        System.out.println(farm);
    }

    public void displayAllFarms() {
        List<Farm> farms = farmService.getAllFarms();

        if (farms.isEmpty()) {
            System.out.println("Aucune ferme trouvee.");
            return;
        }

        farms.forEach(System.out::println);
    }

    public void displayFarmsByType() {
        System.out.println("Fonctionnalite non disponible pour les fermes.");
    }

    public void displayFarmsByHealthStatus() {
        System.out.println("Fonctionnalite non disponible pour les fermes.");
    }

    private String readRequiredString(String prompt) {
        String value;

        do {
            System.out.print(prompt);
            value = scanner.nextLine().trim();

            if (value.isEmpty()) {
                System.out.println("Champ obligatoire.");
            }
        } while (value.isEmpty());

        return value;
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Entrez un nombre entier valide.");
            }
        }
    }
}
