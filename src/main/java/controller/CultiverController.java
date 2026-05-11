package controller;

import model.Cultiver;
import services.CultiverService;
import java.util.List;

public class CultiverController {
    private final CultiverService cultiverService = new CultiverService();

    public Cultiver createCultiver(Cultiver cultiver) {
        return cultiverService.createCultiver(cultiver);
    }

    public List<Cultiver> getAllCultivers() {
        return cultiverService.getAllCultivers();
    }

    public List<Cultiver> getCultiversByFarm(int farmId) {
        return cultiverService.getCultiversByFarm(farmId);
    }

    public Cultiver getCultiverById(int id) {
        return cultiverService.getCultiverById(id);
    }

    public Cultiver updateCultiver(Cultiver cultiver, int id) {
        return cultiverService.updateCultiver(cultiver, id);
    }

    public boolean deleteCultiver(int id) {
        return cultiverService.deleteCultiver(id);
    }

    public boolean updateCultiverStatus(int id, String status) {
        return cultiverService.updateCultiverStatus(id, status);
    }

    public List<Cultiver> calculateHarvestDates() {
        return cultiverService.calculateHarvestDates();
    }
}
