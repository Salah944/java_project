package model;

import model.enums.Role;

public class Ouvrier extends User {
    private double salaire;
    private int farmId;

    public Ouvrier() {
        setRole(Role.OUVRIER);
    }

    public Ouvrier(int id, String name, String email, String password, double salaire) {
        super(id, name, email, password, Role.OUVRIER);
        this.salaire = salaire;
    }

    public Ouvrier(int id, int farmId, double salaire) {
        super(id, null, null, null, Role.OUVRIER);
        this.farmId = farmId;
        this.salaire = salaire;
    }

    public double getSalaire() { return salaire; }
    public void setSalaire(double salaire) { this.salaire = salaire; }
    public int getFarmId() { return farmId; }
    public void setFarmId(int farmId) { this.farmId = farmId; }

    @Override
    public String toString() {
        return "Ouvrier{" +
                "id=" + getId() +
                ", farmId=" + farmId +
                ", salaire=" + salaire +
                ", role=" + getRole() +
                '}';
    }
}
