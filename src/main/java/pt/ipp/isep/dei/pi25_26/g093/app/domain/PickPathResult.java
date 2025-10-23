package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.List;

/**
 * Represents the result of a pick path sequencing strategy.
 * Contains the ordered path and total distance.
 */
public class PickPathResult {
    private final String strategyName;
    private final List<Coordinate> path;
    private final int totalDistance;

    public PickPathResult(String strategyName, List<Coordinate> path, int totalDistance) {
        this.strategyName = strategyName;
        this.path = path;
        this.totalDistance = totalDistance;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public List<Coordinate> getPath() {
        return path;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    @Override
    public String toString() {
        return String.format("%s: Distance=%d, Path=%s", 
                strategyName, totalDistance, path);
    }
}
