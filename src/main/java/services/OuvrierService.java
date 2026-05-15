package services;

import dao.FarmDAO;
import dao.FarmDAOImpl;
import dao.OuvrierDAO;
import dao.OuvrierDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import exceptions.NotFoundException;
import exceptions.ValidationException;
import model.Ouvrier;
import model.User;
import model.enums.Role;
import java.util.List;

public class OuvrierService {
    private final OuvrierDAO ouvrierDAO = new OuvrierDAOImpl();
    private final FarmDAO farmDAO = new FarmDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();

    public Ouvrier createOuvrier(Ouvrier ouvrier) {
        validateOuvrier(ouvrier);
        ouvrier.setRole(Role.OUVRIER);
        return ouvrierDAO.create(ouvrier);
    }

    public List<Ouvrier> getAllOuvriers() {
        return ouvrierDAO.getAll();
    }

    public Ouvrier getOuvrierById(int id) {
        return ouvrierDAO.getById(id)
                .orElseThrow(() -> new NotFoundException("Ouvrier non trouve avec l'id : " + id));
    }

    private void validateOuvrier(Ouvrier ouvrier) {
        if (ouvrier.getId() <= 0) {
            throw new ValidationException("L'id utilisateur de l'ouvrier est obligatoire.");
        }
        User user = userDAO.getById(ouvrier.getId())
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouve avec l'id : " + ouvrier.getId()));
        if (user.getRole() != Role.OUVRIER) {
            throw new ValidationException("L'utilisateur doit avoir le role OUVRIER.");
        }
        if (ouvrierDAO.getById(ouvrier.getId()).isPresent()) {
            throw new ValidationException("Un profil ouvrier existe deja pour l'id : " + ouvrier.getId());
        }
        if (ouvrier.getFarmId() <= 0) {
            throw new ValidationException("L'id de la ferme est invalide.");
        }
        if (!farmDAO.getById(ouvrier.getFarmId()).isPresent()) {
            throw new NotFoundException("Ferme non trouvee avec l'id : " + ouvrier.getFarmId());
        }
        if (ouvrier.getSalaire() < 0) {
            throw new ValidationException("Le salaire doit etre positif ou nul.");
        }
    }

    public void updateOuvrier(Ouvrier selectedWorker, int id) {
    }

    public void deleteOuvrier(int id) {
    }
}
