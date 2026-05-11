package model;

import model.enums.CropStatus;
import java.util.Date;

public class Cultiver {
    private int id;
    private int farmId;
    private String name;
    private Date planningDate;
    private Date hervesDate;
    private int quantity;
    private CropStatus status;

    public Cultiver() {}

    public Cultiver(int id, int farmId, String name, Date planningDate, Date hervesDate, int quantity, CropStatus status) {
        this.id = id;
        this.farmId = farmId;
        this.name = name;
        this.planningDate = planningDate;
        this.hervesDate = hervesDate;
        this.quantity = quantity;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getFarmId() { return farmId; }
    public void setFarmId(int farmId) { this.farmId = farmId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Date getPlanningDate() { return planningDate; }
    public void setPlanningDate(Date planningDate) { this.planningDate = planningDate; }
    public Date getHervesDate() { return hervesDate; }
    public void setHervesDate(Date hervesDate) { this.hervesDate = hervesDate; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public CropStatus getStatus() { return status; }
    public void setStatus(CropStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Cultiver{" + "id=" + id + ", farmId=" + farmId + ", name='" + name + "', status=" + status + '}';
    }
}
