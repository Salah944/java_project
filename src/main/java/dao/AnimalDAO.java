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
    Optional<Animal> getById(int id);
    Animal update(Animal animal);
    boolean delete(int id);
}
