package model;

public class Ouvrier extends User {
    private double salaire;
    private String role;

    public Ouvrier(int id, String name, String email, String password, double salaire, String role) {
        super(id, name, email, password, "OUVRIER");
        this.salaire = salaire;
        this.role = role;
    }

    public double getSalaire() {
        return salaire;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Ouvrier{" +
                "salaire=" + salaire +
                ", role='" + role + '\'' +
                '}';
    }
}
