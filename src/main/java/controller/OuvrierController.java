package controller;

import model.Ouvrier;
import services.OuvrierService;
import java.util.List;

public class OuvrierController {
    private final OuvrierService ouvrierService = new OuvrierService();

    public Ouvrier createOuvrier(Ouvrier ouvrier) {
        return ouvrierService.createOuvrier(ouvrier);
    }

    public List<Ouvrier> getAllOuvriers() {
        return ouvrierService.getAllOuvriers();
    }

    public Ouvrier getOuvrierById(int id) {
        return ouvrierService.getOuvrierById(id);
    }
}
