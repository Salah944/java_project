package services;

import dao.StockDAO;
import dao.StockDAOImpl;
import dao.FarmDAO;
import dao.FarmDAOImpl;
import exceptions.NotFoundException;
import exceptions.ValidationException;
import model.Stock;
import java.util.List;

public class StockService {

    private final StockDAO stockDAO = new StockDAOImpl();
    private final FarmDAO farmDAO = new FarmDAOImpl();

    public Stock createStock(Stock stock) {
        validateStock(stock);
        return stockDAO.create(stock);
    }

    public List<Stock> getAllStocks() {
        return stockDAO.getAll();
    }

    public Stock getStockById(int id) {
        return stockDAO.getById(id)
                .orElseThrow(() -> new NotFoundException("Stock non trouvé avec l'id : " + id));
    }

    public List<Stock> getStocksByFarm(int farmId) {
        if (!farmDAO.getById(farmId).isPresent()) {
            throw new NotFoundException("Ferme non trouvée avec l'id : " + farmId);
        }
        return stockDAO.getByFarm(farmId);
    }

    public Stock updateStock(Stock stock, int id) {
        if (!stockDAO.getById(id).isPresent()) {
            throw new NotFoundException("Stock non trouvé avec l'id : " + id);
        }
        validateStock(stock);
        return stockDAO.update(stock, id);
    }

    public boolean deleteStock(int id) {
        if (!stockDAO.getById(id).isPresent()) {
            throw new NotFoundException("Stock non trouvé avec l'id : " + id);
        }
        return stockDAO.delete(id);
    }

    private void validateStock(Stock stock) {
        if (stock.getFarmId() <= 0) {
            throw new ValidationException("L'id de la ferme est invalide.");
        }
        if (!farmDAO.getById(stock.getFarmId()).isPresent()) {
            throw new NotFoundException("Ferme non trouvée avec l'id : " + stock.getFarmId());
        }
        if (stock.getType() == null || stock.getType().isBlank()) {
            throw new ValidationException("Le type de stock est obligatoire.");
        }
        if (stock.getQuantity() < 0) {
            throw new ValidationException("La quantité ne peut pas être négative.");
        }
        if (stock.getDateAdded() == null) {
            throw new ValidationException("La date d'ajout est obligatoire.");
        }
    }
}
