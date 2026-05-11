package services;

import dao.AnimalDAO;
import dao.AnimalDAOImpl;
import dao.FarmDAO;
import dao.FarmDAOImpl;
import exceptions.NotFoundException;
import exceptions.ValidationException;
import model.Animal;
import model.Poulet;
import model.Vache;
import java.util.List;

public class AnimalService {

    private final AnimalDAO animalDAO = new AnimalDAOImpl();
    private final FarmDAO farmDAO = new FarmDAOImpl();

    public Vache addVache(Vache vache) {
        validateAnimal(vache);
        if (vache.getMilkProduction() < 0) {
            throw new ValidationException("La production de lait ne peut pas être négative.");
        }
        return animalDAO.addVache(vache);
    }

    public Poulet addPoulet(Poulet poulet) {
        validateAnimal(poulet);
        if (poulet.getEggProduction() < 0) {
            throw new ValidationException("La production d'oeufs ne peut pas être négative.");
        }
        return animalDAO.addPoulet(poulet);
    }

    public List<Animal> getAllAnimals() {
        return animalDAO.getAll();
    }

    public Animal getAnimalById(int id) {
        return animalDAO.getById(id)
                .orElseThrow(() -> new NotFoundException("Animal non trouvé avec l'id : " + id));
    }

    public List<Animal> getAnimalsByFarmId(int farmId) {
        if (!farmDAO.getById(farmId).isPresent()) {
            throw new NotFoundException("Ferme non trouvée avec l'id : " + farmId);
        }
        return animalDAO.getByFarm(farmId);
    }

    public Animal updateAnimal(Animal animal) {
        if (!animalDAO.getById(animal.getId()).isPresent()) {
            throw new NotFoundException("Animal non trouvé avec l'id : " + animal.getId());
        }
        validateAnimal(animal);
        return animalDAO.update(animal);
    }

    public boolean deleteAnimal(int id) {
        if (!animalDAO.getById(id).isPresent()) {
            throw new NotFoundException("Animal non trouvé avec l'id : " + id);
        }
        return animalDAO.delete(id);
    }

    private void validateAnimal(Animal animal) {
        if (animal.getFarmId() <= 0) {
            throw new ValidationException("L'id de la ferme est invalide.");
        }
        if (!farmDAO.getById(animal.getFarmId()).isPresent()) {
            throw new NotFoundException("Ferme non trouvée avec l'id : " + animal.getFarmId());
        }
        if (animal.getAge() < 0) {
            throw new ValidationException("L'âge ne peut pas être négatif.");
        }
        if (animal.getHealthStatus() == null || animal.getHealthStatus().isBlank()) {
            throw new ValidationException("Le statut de santé est obligatoire.");
        }
    }
}
