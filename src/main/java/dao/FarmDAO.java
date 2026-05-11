package dao;

import model.Farm;
import java.util.List;
import java.util.Optional;

public interface FarmDAO {
    Farm create(Farm farm);
    List<Farm> getAll();
    Optional<Farm> getById(int id);
    Farm update(Farm farm, int id);
    boolean delete(int id);
}
