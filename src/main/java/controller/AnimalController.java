package controller;

import model.Animal;
import model.Poulet;
import model.Vache;
import services.AnimalService;
import java.util.List;

public class AnimalController {
    private final AnimalService animalService = new AnimalService();

    public Vache addVache(Vache vache) {
        return animalService.addVache(vache);
    }

    public Poulet addPoulet(Poulet poulet) {
        return animalService.addPoulet(poulet);
    }

    public List<Animal> getAllAnimals() {
        return animalService.getAllAnimals();
    }

    public List<Animal> getAnimalsByFarm(int farmId) {
        return animalService.getAnimalsByFarmId(farmId);
    }

    public Animal getAnimalById(int id) {
        return animalService.getAnimalById(id);
    }

    public Animal updateAnimal(Animal animal) {
        return animalService.updateAnimal(animal);
    }

    public boolean deleteAnimal(int id) {
        return animalService.deleteAnimal(id);
    }
}
