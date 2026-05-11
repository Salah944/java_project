package model;

import java.util.Date;

public class Stock {
    private int id;
    private int farmId;
    private String type;
    private double quantity;
    private Date dateAdded;

    public Stock() {}

    public Stock(int id, int farmId, String type, double quantity, Date dateAdded) {
        this.id = id;
        this.farmId = farmId;
        this.type = type;
        this.quantity = quantity;
        this.dateAdded = dateAdded;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", farmId=" + farmId +
                ", type='" + type + '\'' +
                ", quantity=" + quantity +
                ", dateAdded=" + dateAdded +
                '}';
    }
}
