package dao;

import model.Farm;
import java.util.List;

public interface FarmDAO {
    void create(Farm farm);
    List<Farm> getAll();
    Farm getById(int id);
    void update(Farm farm, int id);
    void delete(int id);
}