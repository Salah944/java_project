package model;

public class Vache extends Animal {

    private double milkProduction;

    public Vache() {
    }

    public Vache(int id, int farmId, int age, String healthStatus, double milkProduction) {
        super(id, farmId, age, healthStatus, "VACHE");
        this.milkProduction = milkProduction;
    }

    public double getMilkProduction() {
        return milkProduction;
    }

    public void setMilkProduction(double milkProduction) {
        this.milkProduction = milkProduction;
    }
}