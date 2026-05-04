package service;
import java.util.List;
import dao.FarmDAO;
import model.Farm;

public class FarmService {

    private final FarmDAO farmDAO = new FarmDAO();

    public boolean addFarm(Farm farm) {
        return farmDAO.addFarm(farm);
    }

    public List<Farm> getAllFarms() {
        return farmDAO.getAllFarms();
    }


    public boolean updateFarm(Farm farm) {
        return farmDAO.updateFarm(farm);
    }

    public boolean deleteFarm(int id) {
        return farmDAO.deleteFarm(id);
    }
}