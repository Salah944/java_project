package controller;

import dto.FarmSummaryDTO;
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

    public List<Farm> searchFarmByName(String name) {
        return farmService.searchFarmByName(name);
    }

    public Farm updateFarm(Farm farm, int id) {
        return farmService.updateFarm(farm, id);
    }

    public boolean deleteFarm(int id) {
        return farmService.deleteFarm(id);
    }

    public FarmSummaryDTO getFarmSummary(int farmId) {
        return farmService.getFarmSummary(farmId);
    }

    public long countAnimals(int farmId) {
        return farmService.countAnimals(farmId);
    }

    public long countWorkers(int farmId) {
        return farmService.countWorkers(farmId);
    }

    public long countTasks(int farmId) {
        return farmService.countTasks(farmId);
    }

    public long countStocks(int farmId) {
        return farmService.countStocks(farmId);
    }
}
