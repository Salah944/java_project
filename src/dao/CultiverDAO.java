package dao;

import model.Cultiver;
import java.util.List;

public interface CultiverDAO {
    void create(Cultiver cultiver);
    List<Cultiver> getAll();
    Cultiver getById(int id);
    void update(Cultiver cultiver, int id);
    void delete(int id);
}