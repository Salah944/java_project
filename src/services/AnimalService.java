package service;

import dao.AnimalDAO;
import dao.AnimalDAOImpl;
import model.Animal;
import model.Vache;
import model.Poulet;

import java.util.List;

public class AnimalService {

    private final AnimalDAO animalDAO = new AnimalDAOImpl();

    public void addVache(Vache vache) {
        if (vache.getAge() <= 0) {
            System.out.println("Erreur : l'âge doit être positif.");
            return;
        }
        if (vache.getMilkProduction() < 0) {
            System.out.println("Erreur : la production de lait ne peut pas être négative.");
            return;
        }
        animalDAO.addVache(vache);
    }

    public void addPoulet(Poulet poulet) {
        if (poulet.getAge() <= 0) {
            System.out.println("Erreur : l'âge doit être positif.");
            return;
        }
        if (poulet.getEggProduction() < 0) {
            System.out.println("Erreur : la production d'oeufs ne peut pas être négative.");
            return;
        }
        animalDAO.addPoulet(poulet);
    }

    public List<Animal> getAnimalsByFarm(int farmId) {
        if (farmId <= 0) {
            System.out.println("Erreur : farmId invalide.");
            return null;
        }
        return animalDAO.getByFarm(farmId);
    }

    public Animal getAnimalById(int id) {
        if (id <= 0) {
            System.out.println("Erreur : id invalide.");
            return null;
        }
        return animalDAO.getById(id);
    }

    public void deleteAnimal(int id) {
        if (id <= 0) {
            System.out.println("Erreur : id invalide.");
            return;
        }
        animalDAO.delete(id);
    }
}