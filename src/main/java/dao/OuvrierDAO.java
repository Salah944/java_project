package dao;

import model.Ouvrier;
import java.util.List;

public interface OuvrierDAO {
    void create(Ouvrier ouvrier);
    List<Ouvrier> getAll();
    Ouvrier getById(int id);
    void update(Ouvrier ouvrier, int id);
    void delete(int id);
}