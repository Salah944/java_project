package dao;

import model.Animal;
import model.Vache;
import model.Poulet;
import java.util.List;

public interface AnimalDAO {
    void addVache(Vache vache);
    void addPoulet(Poulet poulet);
    List<Animal> getByFarm(int farmId);
    Animal getById(int id);
    void delete(int id);
}