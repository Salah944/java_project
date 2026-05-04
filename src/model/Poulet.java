package model;

public class Poulet extends Animal {

    private int eggProduction;

    public Poulet() {
    }

    public Poulet(int id, int farmId, int age, String healthStatus, int eggProduction) {
        super(id, farmId, age, healthStatus, "POULET");
        this.eggProduction = eggProduction;
    }

    public int getEggProduction() {
        return eggProduction;
    }

    public void setEggProduction(int eggProduction) {
        this.eggProduction = eggProduction;
    }
}