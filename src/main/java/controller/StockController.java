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

    public Stock getStockById(int id) {
        return stockService.getStockById(id);
    }

    public Stock updateStock(Stock stock, int id) {
        return stockService.updateStock(stock, id);
    }

    public boolean deleteStock(int id) {
        return stockService.deleteStock(id);
    }
}
