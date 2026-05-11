package services;

import dao.StockDAO;
import dao.StockDAOImpl;
import model.Stock;

import java.util.List;

public class StockService {

    private final StockDAO stockDAO = new StockDAOImpl();

    public void createStock(Stock stock) {
        if (stock.getType() == null || stock.getType().isEmpty()) {
            System.out.println("Erreur : le type est obligatoire.");
            return;
        }
        if (stock.getQuantity() < 0) {
            System.out.println("Erreur : la quantité ne peut pas être négative.");
            return;
        }
        if (stock.getDateAdded() == null) {
            System.out.println("Erreur : la date est obligatoire.");
            return;
        }
        stockDAO.create(stock);
    }

    public List<Stock> getAllStocks() {
        return stockDAO.getAll();
    }

    public Stock getStockById(int id) {
        if (id <= 0) {
            System.out.println("Erreur : id invalide.");
            return null;
        }
        return stockDAO.getById(id);
    }

    public void updateStock(Stock stock, int id) {
        if (id <= 0) {
            System.out.println("Erreur : id invalide.");
            return;
        }
        stockDAO.update(stock, id);
    }

    public void deleteStock(int id) {
        if (id <= 0) {
            System.out.println("Erreur : id invalide.");
            return;
        }
        stockDAO.delete(id);
    }
}
