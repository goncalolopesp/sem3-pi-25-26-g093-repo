package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;

import java.util.Collections;
import java.util.List;

public class GeoAreaSearchResult {

    private final List<CompleteStation> stations;
    private final long executionTimeNanos;
    private final String temporalComplexity;
    private final List<String> sampleQueries;

    public GeoAreaSearchResult(List<CompleteStation> stations,
                               long executionTimeNanos,
                               String temporalComplexity,
                               List<String> sampleQueries) {
        this.stations = stations == null ? Collections.emptyList() : List.copyOf(stations);
        this.executionTimeNanos = executionTimeNanos;
        this.temporalComplexity = temporalComplexity;
        this.sampleQueries = sampleQueries == null ? Collections.emptyList() : List.copyOf(sampleQueries);
    }

    public List<CompleteStation> getStations() {
        return stations;
    }

    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }

    public String getTemporalComplexity() {
        return temporalComplexity;
    }

    public List<String> getSampleQueries() {
        return sampleQueries;
    }
}

