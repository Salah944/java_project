package dao;

import model.Farm;
import java.util.List;
import java.util.Optional;

public interface FarmDAO {
    Farm create(Farm farm);
    List<Farm> getAll();
    List<Farm> getByAdminId(int adminId);
    Optional<Farm> getById(int id);
    List<Farm> searchByName(String name);
    List<Farm> searchByNameAndAdmin(String name, int adminId);
    Farm update(Farm farm, int id);
    boolean delete(int id);
    long countAnimals(int farmId);
    long countWorkers(int farmId);
    long countTasks(int farmId);
    long countStocks(int farmId);
}
