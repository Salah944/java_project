package model;

import model.enums.TaskStatus;
import java.util.Date;

public class Task {
    private int id;
    private int farmId;
    private Integer workerId; // Nullable if not assigned yet
    private String description;
    private TaskStatus status;
    private Date dueDate;

    public Task() {}

    public Task(int id, int farmId, Integer workerId, String description, TaskStatus status, Date dueDate) {
        this.id = id;
        this.farmId = farmId;
        this.workerId = workerId;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
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

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", farmId=" + farmId +
                ", workerId=" + workerId +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", dueDate=" + dueDate +
                '}';
    }
}
