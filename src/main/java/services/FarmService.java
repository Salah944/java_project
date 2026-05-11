package services;

import dao.FarmDAO;
import dao.FarmDAOImpl;
import dto.FarmSummaryDTO;
import exceptions.NotFoundException;
import exceptions.ValidationException;
import model.Farm;
import java.util.List;

public class FarmService {

    private final FarmDAO farmDAO = new FarmDAOImpl();

    public Farm createFarm(Farm farm) {
        validateFarm(farm);
        return farmDAO.create(farm);
    }

    public List<Farm> getAllFarms() {
        return farmDAO.getAll();
    }

    public Farm getFarmById(int id) {
        return farmDAO.getById(id)
                .orElseThrow(() -> new NotFoundException("Ferme non trouvée avec l'id : " + id));
    }

    public List<Farm> searchFarmByName(String name) {
        if (name == null || name.isBlank()) {
            return farmDAO.getAll();
        }
        return farmDAO.searchByName(name.trim());
    }

    public Farm updateFarm(Farm farm, int id) {
        if (!farmDAO.getById(id).isPresent()) {
            throw new NotFoundException("Ferme non trouvée avec l'id : " + id);
        }
        validateFarm(farm);
        return farmDAO.update(farm, id);
    }

    public boolean deleteFarm(int id) {
        if (!farmDAO.getById(id).isPresent()) {
            throw new NotFoundException("Ferme non trouvée avec l'id : " + id);
        }
        return farmDAO.delete(id);
    }

    public FarmSummaryDTO getFarmSummary(int farmId) {
        getFarmById(farmId);
        return new FarmSummaryDTO(
                farmId,
                countAnimals(farmId),
                countWorkers(farmId),
                countTasks(farmId),
                countStocks(farmId)
        );
    }

    public long countAnimals(int farmId) {
        getFarmById(farmId);
        return farmDAO.countAnimals(farmId);
    }

    public long countWorkers(int farmId) {
        getFarmById(farmId);
        return farmDAO.countWorkers(farmId);
    }

    public long countTasks(int farmId) {
        getFarmById(farmId);
        return farmDAO.countTasks(farmId);
    }

    public long countStocks(int farmId) {
        getFarmById(farmId);
        return farmDAO.countStocks(farmId);
    }

    private void validateFarm(Farm farm) {
        if (farm.getName() == null || farm.getName().isBlank()) {
            throw new ValidationException("Le nom de la ferme est obligatoire.");
        }
        if (farm.getLocation() == null || farm.getLocation().isBlank()) {
            throw new ValidationException("La localisation de la ferme est obligatoire.");
        }
    }
}
