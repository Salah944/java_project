package dao;

import model.Ouvrier;
import java.util.List;
import java.util.Optional;

public interface OuvrierDAO {
    Ouvrier create(Ouvrier ouvrier);
    List<Ouvrier> getAll();
    Optional<Ouvrier> getById(int id);
    Ouvrier update(Ouvrier ouvrier, int id);
    boolean delete(int id);
}
