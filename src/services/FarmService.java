package service;

import dao.FarmDAO;
import dao.FarmDAOImpl;
import model.Farm;

import java.util.List;

public class FarmService {

    private final FarmDAO farmDAO = new FarmDAOImpl();

    public void createFarm(Farm farm) {
        if (farm.getName() == null || farm.getName().isEmpty()) {
            System.out.println("Erreur : le nom de la ferme est obligatoire.");
            return;
        }
        if (farm.getLocation() == null || farm.getLocation().isEmpty()) {
            System.out.println("Erreur : la localisation est obligatoire.");
            return;
        }
        farmDAO.create(farm);
    }

    public List<Farm> getAllFarms() {
        return farmDAO.getAll();
    }

    public Farm getFarmById(int id) {
        if (id <= 0) {
            System.out.println("Erreur : id invalide.");
            return null;
        }
        return farmDAO.getById(id);
    }

    public void updateFarm(Farm farm, int id) {
        if (id <= 0) {
            System.out.println("Erreur : id invalide.");
            return;
        }
        farmDAO.update(farm, id);
    }

    public void deleteFarm(int id) {
        if (id <= 0) {
            System.out.println("Erreur : id invalide.");
            return;
        }
        farmDAO.delete(id);
    }
}