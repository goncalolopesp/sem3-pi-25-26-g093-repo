package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;

import java.util.Collections;
import java.util.List;

/**
 * Result class for circular geographical area search (USEI09).
 * Contains the stations found within the radius and their distances from the center point.
 */
public class GeoAreaCircleSearchResult {

    private final List<CompleteStation> stations;
    private final List<Double> distances;
    private final long executionTimeNanos;
    private final String complexity;
    private final List<String> sampleQueries;

    public GeoAreaCircleSearchResult(List<CompleteStation> stations,
                                     List<Double> distances,
                                     long executionTimeNanos,
                                     String complexity,
                                     List<String> sampleQueries) {
        this.stations = stations == null ? Collections.emptyList() : List.copyOf(stations);
        this.distances = distances == null ? Collections.emptyList() : List.copyOf(distances);
        this.executionTimeNanos = executionTimeNanos;
        this.complexity = complexity;
        this.sampleQueries = sampleQueries == null ? Collections.emptyList() : List.copyOf(sampleQueries);
    }

    public List<CompleteStation> getStations() {
        return stations;
    }

    public List<Double> getDistances() {
        return distances;
    }

    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }

    public String getComplexity() {
        return complexity;
    }

    public List<String> getSampleQueries() {
        return sampleQueries;
    }
}

