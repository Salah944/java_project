package dao;

import model.Stock;
import java.util.List;

public interface StockDAO {
    void create(Stock stock);
    List<Stock> getAll();
    Stock getById(int id);
    void update(Stock stock, int id);
    void delete(int id);
}