package services;

import dao.FarmDAO;
import dao.FarmDAOImpl;
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

    private void validateFarm(Farm farm) {
        if (farm.getName() == null || farm.getName().isBlank()) {
            throw new ValidationException("Le nom de la ferme est obligatoire.");
        }
        if (farm.getLocation() == null || farm.getLocation().isBlank()) {
            throw new ValidationException("La localisation de la ferme est obligatoire.");
        }
    }
}
