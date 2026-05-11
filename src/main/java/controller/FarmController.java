package controller;

import model.Farm;
import services.FarmService;
import java.util.List;

public class FarmController {
    private final FarmService farmService = new FarmService();

    public Farm createFarm(Farm farm) {
        return farmService.createFarm(farm);
    }

    public List<Farm> getAllFarms() {
        return farmService.getAllFarms();
    }

    public Farm getFarmById(int id) {
        return farmService.getFarmById(id);
    }

    public Farm updateFarm(Farm farm, int id) {
        return farmService.updateFarm(farm, id);
    }

    public boolean deleteFarm(int id) {
        return farmService.deleteFarm(id);
    }
}
