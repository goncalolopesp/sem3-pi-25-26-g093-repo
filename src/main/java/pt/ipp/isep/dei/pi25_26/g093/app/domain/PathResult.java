package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.List;

public class  PathResult {
    private final boolean success;
    private final String errorMessage;
    private final List<CompleteStation> path;
    private final double totalCost;

    public PathResult(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.path = null;
        this.totalCost = 0.0;
    }

    public PathResult(boolean success, List<CompleteStation> path, double totalCost) {
        this.success = success;
        this.errorMessage = null;
        this.path = path;
        this.totalCost = totalCost;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<CompleteStation> getPath() {
        return path;
    }

    public double getTotalCost() {
        return totalCost;
    }
}
