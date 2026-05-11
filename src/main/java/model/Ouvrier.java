package model;

import model.enums.Role;

public class Ouvrier extends User {
    private double salaire;

    public Ouvrier() {
        setRole(Role.OUVRIER);
    }

    public Ouvrier(int id, String name, String email, String password, double salaire) {
        super(id, name, email, password, Role.OUVRIER);
        this.salaire = salaire;
    }

    public double getSalaire() { return salaire; }
    public void setSalaire(double salaire) { this.salaire = salaire; }

    @Override
    public String toString() {
        return "Ouvrier{" + "salaire=" + salaire + "} " + super.toString();
    }
}
