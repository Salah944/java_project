package model;

public class Farm {

    private int id;
    private String name;
    private String location;
    private int adminId;

    public Farm() {
    }

    public Farm(int id, String name, String location, int adminId) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.adminId = adminId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    @Override
    public String toString() {
        return "Farm{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", adminId=" + adminId +
                '}';
    }
}