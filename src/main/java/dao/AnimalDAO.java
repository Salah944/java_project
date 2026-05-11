package dao;

import model.Animal;
import model.Vache;
import model.Poulet;
import java.util.List;
import java.util.Optional;

public interface AnimalDAO {
    Vache addVache(Vache vache);
    Poulet addPoulet(Poulet poulet);
    List<Animal> getAll();
    List<Animal> getByFarm(int farmId);
    List<Animal> getByType(String type);
    Optional<Animal> getById(int id);
    Animal update(Animal animal);
    Animal update(Animal animal, int id);
    boolean delete(int id);
    boolean updateHealthStatus(int animalId, String status);
}
