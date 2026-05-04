package model;

public class Animal {

    private int id;
    private int farmId;
    private int age;
    private String healthStatus;
    private String type;

    public Animal() {
    }

    public Animal(int id, int farmId, int age, String healthStatus, String type) {
        this.id = id;
        this.farmId = farmId;
        this.age = age;
        this.healthStatus = healthStatus;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getFarmId() {
        return farmId;
    }

    public void setFarmId(int farmId) {
        this.farmId = farmId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}