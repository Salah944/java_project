package services;

import dao.CultiverDAO;
import dao.CultiverDAOImpl;
import model.Cultiver;

import java.util.List;

public class CultiverService {

    private final CultiverDAO cultiverDAO = new CultiverDAOImpl();

    public void createCultiver(Cultiver cultiver) {
        if (cultiver.getName() == null || cultiver.getName().isEmpty()) {
            System.out.println("Erreur : le nom est obligatoire.");
            return;
        }
        if (cultiver.getPlanningDate() == null) {
            System.out.println("Erreur : la date de plantation est obligatoire.");
            return;
        }
        if (cultiver.getQuantity() <= 0) {
            System.out.println("Erreur : la quantité doit être positive.");
            return;
        }
        cultiverDAO.create(cultiver);
    }

    public List<Cultiver> getAllCultivers() {
        return cultiverDAO.getAll();
    }

    public Cultiver getCultiverById(int id) {
        if (id <= 0) {
            System.out.println("Erreur : id invalide.");
            return null;
        }
        return cultiverDAO.getById(id);
    }

    public void updateCultiver(Cultiver cultiver, int id) {
        if (id <= 0) {
            System.out.println("Erreur : id invalide.");
            return;
        }
        cultiverDAO.update(cultiver, id);
    }

    public void deleteCultiver(int id) {
        if (id <= 0) {
            System.out.println("Erreur : id invalide.");
            return;
        }
        cultiverDAO.delete(id);
    }
}
