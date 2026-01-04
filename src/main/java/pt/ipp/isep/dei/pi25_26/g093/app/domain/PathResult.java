package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Edge;

import java.util.List;

public class  PathResult {
    private final boolean success;
    private final String errorMessage;
    private List<CompleteStation> path;
    private double totalCost;
    private List<CompleteStation> negativeCycleStations;
    private List<Edge<CompleteStation, Double>> negativeCycleEdges;

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

    public PathResult(List<CompleteStation> cycleStations,
                      List<Edge<CompleteStation, Double>> cycleEdges) {
        this.success = false;
        this.negativeCycleStations = cycleStations;
        this.negativeCycleEdges = cycleEdges;
        this.errorMessage = "Negative cycle detected.";
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

    public List<CompleteStation> getNegativeCycleStations() { return negativeCycleStations; }
    public List<Edge<CompleteStation, Double>> getNegativeCycleEdges() { return negativeCycleEdges; }

}
