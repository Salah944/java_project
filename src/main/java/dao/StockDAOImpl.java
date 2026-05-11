package dao;

import database.ConnectionDb;
import model.Stock;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StockDAOImpl implements StockDAO {

    @Override
    public Stock create(Stock stock) {
        String sql = "INSERT INTO Stocks (farm_id, type, quantity, date_added) VALUES (?, ?, ?, ?)";
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, stock.getFarmId());
            stmt.setString(2, stock.getType());
            stmt.setDouble(3, stock.getQuantity());
            stmt.setDate(4, new java.sql.Date(stock.getDateAdded().getTime()));
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    stock.setId(keys.getInt(1));
                }
            }
            return stock;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating stock: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Stock> getAll() {
        return fetchStocks("SELECT * FROM Stocks", null);
    }

    @Override
    public Optional<Stock> getById(int id) {
        List<Stock> stocks = fetchStocks("SELECT * FROM Stocks WHERE id = ?", id);
        return stocks.isEmpty() ? Optional.empty() : Optional.of(stocks.get(0));
    }

    @Override
    public List<Stock> getByFarm(int farmId) {
        return fetchStocks("SELECT * FROM Stocks WHERE farm_id = ?", farmId);
    }

    @Override
    public Stock update(Stock stock, int id) {
        String sql = "UPDATE Stocks SET farm_id = ?, type = ?, quantity = ?, date_added = ? WHERE id = ?";
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, stock.getFarmId());
            stmt.setString(2, stock.getType());
            stmt.setDouble(3, stock.getQuantity());
            stmt.setDate(4, new java.sql.Date(stock.getDateAdded().getTime()));
            stmt.setInt(5, id);
            stmt.executeUpdate();
            stock.setId(id);
            return stock;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating stock: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Stocks WHERE id = ?";
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting stock: " + e.getMessage(), e);
        }
    }

    private List<Stock> fetchStocks(String sql, Integer param) {
        List<Stock> stocks = new ArrayList<>();
        try (Connection cnx = ConnectionDb.getConnection();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            if (param != null) {
                stmt.setInt(1, param);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    stocks.add(new Stock(
                            rs.getInt("id"),
                            rs.getInt("farm_id"),
                            rs.getString("type"),
                            rs.getDouble("quantity"),
                            rs.getDate("date_added")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching stocks: " + e.getMessage(), e);
        }
        return stocks;
    }
}
