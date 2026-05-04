package service;

import dao.AnimalDAO;
import model.Poulet;
import model.Vache;

public class AnimalService {

    private final AnimalDAO animalDAO = new AnimalDAO();

    public boolean addVache(Vache vache) {
        return animalDAO.addVache(vache);
    }

    public boolean addPoulet(Poulet poulet) {
        return animalDAO.addPoulet(poulet);
    }
}