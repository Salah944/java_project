package dao;

import model.Stock;
import java.util.List;
import java.util.Optional;

public interface StockDAO {
    Stock create(Stock stock);
    List<Stock> getAll();
    Optional<Stock> getById(int id);
    List<Stock> getByFarm(int farmId);
    List<Stock> getByType(String type);
    Stock update(Stock stock, int id);
    boolean delete(int id);
    boolean updateQuantity(int stockId, double quantity);
}
