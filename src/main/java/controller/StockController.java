package controller;

import model.Stock;
import services.StockService;
import java.util.List;

public class StockController {
    private final StockService stockService = new StockService();

    public Stock createStock(Stock stock) {
        return stockService.createStock(stock);
    }

    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

    public List<Stock> getStocksByFarm(int farmId) {
        return stockService.getStocksByFarm(farmId);
    }

    public List<Stock> searchStockByType(String type) {
        return stockService.searchStockByType(type);
    }

    public Stock getStockById(int id) {
        return stockService.getStockById(id);
    }

    public Stock updateStock(Stock stock, int id) {
        return stockService.updateStock(stock, id);
    }

    public boolean deleteStock(int id) {
        return stockService.deleteStock(id);
    }

    public Stock addProduct(int stockId, double quantity) {
        return stockService.addProduct(stockId, quantity);
    }

    public Stock removeProduct(int stockId, double quantity) {
        return stockService.removeProduct(stockId, quantity);
    }

    public boolean checkAvailability(int stockId, double quantity) {
        return stockService.checkAvailability(stockId, quantity);
    }

    public boolean isLowStock(int stockId) {
        return stockService.isLowStock(stockId);
    }
}
