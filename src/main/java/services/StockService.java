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

    private static final double LOW_STOCK_THRESHOLD = 10.0;

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

    public List<Stock> searchStockByType(String type) {
        if (type == null || type.isBlank()) {
            return stockDAO.getAll();
        }
        return stockDAO.getByType(type.trim());
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

    public Stock addProduct(int stockId, double quantity) {
        validateQuantityToMove(quantity);
        Stock stock = getStockById(stockId);
        double newQuantity = stock.getQuantity() + quantity;
        stockDAO.updateQuantity(stockId, newQuantity);
        stock.setQuantity(newQuantity);
        return stock;
    }

    public Stock removeProduct(int stockId, double quantity) {
        validateQuantityToMove(quantity);
        Stock stock = getStockById(stockId);
        if (stock.getQuantity() < quantity) {
            throw new ValidationException("Quantité insuffisante en stock.");
        }
        double newQuantity = stock.getQuantity() - quantity;
        stockDAO.updateQuantity(stockId, newQuantity);
        stock.setQuantity(newQuantity);
        return stock;
    }

    public boolean checkAvailability(int stockId, double quantity) {
        validateQuantityToMove(quantity);
        return getStockById(stockId).getQuantity() >= quantity;
    }

    public boolean isLowStock(int stockId) {
        return getStockById(stockId).getQuantity() <= LOW_STOCK_THRESHOLD;
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

    private void validateQuantityToMove(double quantity) {
        if (quantity <= 0) {
            throw new ValidationException("La quantité doit être supérieure à 0.");
        }
    }
}
