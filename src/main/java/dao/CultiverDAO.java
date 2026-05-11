package dao;

import model.Cultiver;
import java.util.List;
import java.util.Optional;

public interface CultiverDAO {
    Cultiver create(Cultiver cultiver);
    List<Cultiver> getAll();
    Optional<Cultiver> getById(int id);
    List<Cultiver> getByFarm(int farmId);
    Cultiver update(Cultiver cultiver, int id);
    boolean delete(int id);
}
