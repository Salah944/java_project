package dao;
import database.ConnectionDb;
import model.Farm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FarmDAO {

    public boolean addFarm(Farm farm) {
        String sql = "INSERT INTO Farms (name, location) VALUES (?, ?)";

        try (
                Connection conn = ConnectionDb.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, farm.getName());
            stmt.setString(2, farm.getLocation());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public List<Farm> getAllFarms() {
        String sql = "SELECT * FROM Farms";
        List<Farm> farms = new ArrayList<>();

        try (
                Connection conn = ConnectionDb.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Farm farm = new Farm(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location")
                );

                farms.add(farm);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return farms;
    }


    public boolean updateFarm(Farm farm) {
        String sql = "UPDATE Farms SET name = ?, location = ? WHERE id = ?";

        try (
                Connection conn = ConnectionDb.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, farm.getName());
            stmt.setString(2, farm.getLocation());
            stmt.setInt(3, farm.getId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteFarm(int id) {
        String sql = "DELETE FROM Farms WHERE id = ?";

        try (
                Connection conn = ConnectionDb.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}