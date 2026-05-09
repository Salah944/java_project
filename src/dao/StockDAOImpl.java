package dao;

import database.ConnectionDb;
import model.Stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockDAOImpl implements StockDAO {

    @Override
    public void create(Stock stock) {
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement(
                    "INSERT INTO Stocks (type, quantity, date_added) VALUES (?, ?, ?)"
            );
            stmt.setString(1, stock.getType());
            stmt.setDouble(2, stock.getQuantity());
            stmt.setDate(3, new java.sql.Date(stock.getDateAdded().getTime()));

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Stock créé avec succès.");
            else          System.out.println("Echec de création.");

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Stock> getAll() {
        List<Stock> stocks = new ArrayList<>();
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement("SELECT * FROM Stocks");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                stocks.add(new Stock(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getDouble("quantity"),
                        rs.getDate("date_added")  // java.sql.Date est sous-classe de java.util.Date ✓
                ));
            }

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return stocks;
    }

    @Override
    public Stock getById(int id) {
        Stock stock = null;
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement(
                    "SELECT * FROM Stocks WHERE id = ?"
            );
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                stock = new Stock(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getDouble("quantity"),
                        rs.getDate("date_added")
                );
            }

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return stock;
    }

    @Override
    public void update(Stock stock, int id) {
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement(
                    "UPDATE Stocks SET type = ?, quantity = ?, date_added = ? WHERE id = ?"
            );
            stmt.setString(1, stock.getType());
            stmt.setDouble(2, stock.getQuantity());
            stmt.setDate(3, new java.sql.Date(stock.getDateAdded().getTime()));
            stmt.setInt(4, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Stock mis à jour.");
            else          System.out.println("Echec de mise à jour.");

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        try {
            Connection cnx = ConnectionDb.getConnection();
            PreparedStatement stmt = cnx.prepareStatement(
                    "DELETE FROM Stocks WHERE id = ?"
            );
            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("Stock supprimé.");
            else          System.out.println("Echec de suppression.");

            ConnectionDb.closecnx(cnx);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}