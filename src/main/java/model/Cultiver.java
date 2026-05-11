package model;

import java.util.Date;

public class Cultiver {
    private int id;
    private String name;
    private Date planningDate;
    private Date hervesDate;
    private int quantity;
    private String status;

    public Cultiver(int id, String name, Date planningDate, Date hervesDate, int quantity, String status) {
        this.id = id;
        this.name = name;
        this.planningDate = planningDate;
        this.hervesDate = hervesDate;
        this.quantity = quantity;
        this.status = status;
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

    public Date getPlanningDate() {
        return planningDate;
    }

    public void setPlanningDate(Date planningDate) {
        this.planningDate = planningDate;
    }

    public Date getHervesDate() {
        return hervesDate;
    }

    public void setHervesDate(Date hervesDate) {
        this.hervesDate = hervesDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Cultiver{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", planningDate=" + planningDate +
                ", hervesDate=" + hervesDate +
                ", quantity=" + quantity +
                ", status='" + status + '\'' +
                '}';
    }
}
