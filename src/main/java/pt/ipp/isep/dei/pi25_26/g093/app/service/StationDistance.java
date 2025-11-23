package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;

/**
 * Helper DTO to hold a station and its distance from a query point.
 * Used in nearest-N search to maintain the max-heap.
 */
public class StationDistance {
    private final CompleteStation station;
    private final double distance;

    public StationDistance(CompleteStation station, double distance) {
        this.station = station;
        this.distance = distance;
    }

    public CompleteStation getStation() {
        return station;
    }

    public double getDistance() {
        return distance;
    }
}

