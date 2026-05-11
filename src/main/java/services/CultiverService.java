package services;

import dao.CultiverDAO;
import dao.CultiverDAOImpl;
import dao.FarmDAO;
import dao.FarmDAOImpl;
import exceptions.NotFoundException;
import exceptions.ValidationException;
import model.Cultiver;
import java.util.List;

public class CultiverService {

    private final CultiverDAO cultiverDAO = new CultiverDAOImpl();
    private final FarmDAO farmDAO = new FarmDAOImpl();

    public Cultiver createCultiver(Cultiver cultiver) {
        validateCultiver(cultiver);
        return cultiverDAO.create(cultiver);
    }

    public List<Cultiver> getAllCultivers() {
        return cultiverDAO.getAll();
    }

    public Cultiver getCultiverById(int id) {
        return cultiverDAO.getById(id)
                .orElseThrow(() -> new NotFoundException("Culture non trouvée avec l'id : " + id));
    }

    public List<Cultiver> getCultiversByFarm(int farmId) {
        if (!farmDAO.getById(farmId).isPresent()) {
            throw new NotFoundException("Ferme non trouvée avec l'id : " + farmId);
        }
        return cultiverDAO.getByFarm(farmId);
    }

    public Cultiver updateCultiver(Cultiver cultiver, int id) {
        if (!cultiverDAO.getById(id).isPresent()) {
            throw new NotFoundException("Culture non trouvée avec l'id : " + id);
        }
        validateCultiver(cultiver);
        return cultiverDAO.update(cultiver, id);
    }

    public boolean deleteCultiver(int id) {
        if (!cultiverDAO.getById(id).isPresent()) {
            throw new NotFoundException("Culture non trouvée avec l'id : " + id);
        }
        return cultiverDAO.delete(id);
    }

    private void validateCultiver(Cultiver cultiver) {
        if (cultiver.getFarmId() <= 0 || !farmDAO.getById(cultiver.getFarmId()).isPresent()) {
            throw new ValidationException("L'id de la ferme est invalide.");
        }
        if (cultiver.getName() == null || cultiver.getName().isBlank()) {
            throw new ValidationException("Le nom de la culture est obligatoire.");
        }
        if (cultiver.getPlanningDate() == null) {
            throw new ValidationException("La date de plantation est obligatoire.");
        }
        if (cultiver.getQuantity() <= 0) {
            throw new ValidationException("La quantité doit être supérieure à 0.");
        }
        if (cultiver.getStatus() == null) {
            throw new ValidationException("Le statut est obligatoire.");
        }
    }
}
