package services;

import dao.FarmDAO;
import dao.FarmDAOImpl;
import dto.FarmSummaryDTO;
import exceptions.NotFoundException;
import exceptions.ValidationException;
import model.Farm;
import model.User;
import util.SessionManager;
import dao.UserDAO;
import dao.UserDAOImpl;
import java.util.List;

public class FarmService {

    private final FarmDAO farmDAO = new FarmDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();

    public Farm createFarm(Farm farm) {
        validateFarm(farm);
        User currentUser = SessionManager.getCurrentUser().orElseThrow(() -> new ValidationException("Non authentifie"));
        farm.setAdminId(currentUser.getId());
        Farm createdFarm = farmDAO.create(farm);
        
        // If the admin doesn't have a farmId set, set it to the newly created farm
        if (currentUser.getFarmId() == null || currentUser.getFarmId() == 0) {
            currentUser.setFarmId(createdFarm.getId());
            userDAO.update(currentUser, currentUser.getId());
        }
        
        return createdFarm;
    }

    public List<Farm> getAllFarms() {
        User currentUser = SessionManager.getCurrentUser().orElseThrow(() -> new ValidationException("Non authentifie"));
        if (currentUser.getRole() == model.enums.Role.ADMIN) {
            return farmDAO.getByAdminId(currentUser.getId());
        }
        return farmDAO.getAll(); // Or handle other roles appropriately
    }

    public Farm getFarmById(int id) {
        Farm farm = farmDAO.getById(id)
                .orElseThrow(() -> new NotFoundException("Ferme non trouvée avec l'id : " + id));
        User currentUser = SessionManager.getCurrentUser().orElseThrow(() -> new ValidationException("Non authentifie"));
        if (currentUser.getRole() == model.enums.Role.ADMIN && farm.getAdminId() != currentUser.getId()) {
            throw new NotFoundException("Vous n'avez pas accès à cette ferme.");
        }
        return farm;
    }

    public List<Farm> searchFarmByName(String name) {
        User currentUser = SessionManager.getCurrentUser().orElseThrow(() -> new ValidationException("Non authentifie"));
        if (name == null || name.isBlank()) {
            return getAllFarms();
        }
        if (currentUser.getRole() == model.enums.Role.ADMIN) {
            return farmDAO.searchByNameAndAdmin(name.trim(), currentUser.getId());
        }
        return farmDAO.searchByName(name.trim());
    }

    public Farm updateFarm(Farm farm, int id) {
        Farm existing = getFarmById(id); // this will check adminId
        validateFarm(farm);
        farm.setAdminId(existing.getAdminId());
        return farmDAO.update(farm, id);
    }

    public boolean deleteFarm(int id) {
        getFarmById(id); // this checks adminId
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
