package dto;

public class FarmSummaryDTO {
    private final int farmId;
    private final long animalsCount;
    private final long workersCount;
    private final long tasksCount;
    private final long stocksCount;

    public FarmSummaryDTO(int farmId, long animalsCount, long workersCount, long tasksCount, long stocksCount) {
        this.farmId = farmId;
        this.animalsCount = animalsCount;
        this.workersCount = workersCount;
        this.tasksCount = tasksCount;
        this.stocksCount = stocksCount;
    }

    public int getFarmId() {
        return farmId;
    }

    public long getAnimalsCount() {
        return animalsCount;
    }

    public long getWorkersCount() {
        return workersCount;
    }

    public long getTasksCount() {
        return tasksCount;
    }

    public long getStocksCount() {
        return stocksCount;
    }

    @Override
    public String toString() {
        return "FarmSummaryDTO{" +
                "farmId=" + farmId +
                ", animalsCount=" + animalsCount +
                ", workersCount=" + workersCount +
                ", tasksCount=" + tasksCount +
                ", stocksCount=" + stocksCount +
                '}';
    }
}
