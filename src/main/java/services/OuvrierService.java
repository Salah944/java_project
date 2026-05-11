package services;

import dao.OuvrierDAO;
import dao.OuvrierDAOImpl;
import model.Ouvrier;

import java.util.List;

public class OuvrierService {

    private final OuvrierDAO ouvrierDAO = new OuvrierDAOImpl();

    public void createOuvrier(Ouvrier ouvrier) {
        if (ouvrier.getName() == null || ouvrier.getName().isEmpty()) {
            System.out.println("Erreur : le nom est obligatoire.");
            return;
        }
        if (ouvrier.getSalaire() <= 0) {
            System.out.println("Erreur : le salaire doit être positif.");
            return;
        }
        ouvrierDAO.create(ouvrier);
    }

    public List<Ouvrier> getAllOuvriers() {
        return ouvrierDAO.getAll();
    }

    public Ouvrier getOuvrierById(int id) {
        if (id <= 0) {
            System.out.println("Erreur : id invalide.");
            return null;
        }
        return ouvrierDAO.getById(id);
    }

    public void updateOuvrier(Ouvrier ouvrier, int id) {
        if (id <= 0) {
            System.out.println("Erreur : id invalide.");
            return;
        }
        ouvrierDAO.update(ouvrier, id);
    }

    public void deleteOuvrier(int id) {
        if (id <= 0) {
            System.out.println("Erreur : id invalide.");
            return;
        }
        ouvrierDAO.delete(id);
    }
}