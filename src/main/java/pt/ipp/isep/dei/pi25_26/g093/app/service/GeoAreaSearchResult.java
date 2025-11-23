package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;

import java.util.Collections;
import java.util.List;

/**
 * Result object for USEI08 - Search by Geographical Area (Rectangular Query).
 * 
 * <p>This immutable class encapsulates the results of a rectangular geographical
 * search, including the matching stations, execution metrics, and metadata.
 * 
 * <p>All lists are immutable copies to prevent external modification.
 * 
 * @author ESINF Sprint 2 Team
 * @see GeoAreaSearch
 */
public class GeoAreaSearchResult {

    private final List<CompleteStation> stations;
    private final long executionTimeNanos;
    private final String temporalComplexity;
    private final List<String> sampleQueries;

    /**
     * Constructs a new GeoAreaSearchResult with the specified data.
     * 
     * <p>All lists are copied to ensure immutability. Null lists are replaced
     * with empty lists.
     * 
     * @param stations the list of matching stations, sorted by latitude,
     *                 longitude, then station name. May be null (treated as empty).
     * @param executionTimeNanos the execution time in nanoseconds
     * @param temporalComplexity a description of the time complexity (e.g.,
     *                          "Approx. O(sqrt(n) + m) with KD-tree pruning")
     * @param sampleQueries example queries for reference. May be null (treated as empty).
     */
    public GeoAreaSearchResult(List<CompleteStation> stations,
                               long executionTimeNanos,
                               String temporalComplexity,
                               List<String> sampleQueries) {
        this.stations = stations == null ? Collections.emptyList() : List.copyOf(stations);
        this.executionTimeNanos = executionTimeNanos;
        this.temporalComplexity = temporalComplexity;
        this.sampleQueries = sampleQueries == null ? Collections.emptyList() : List.copyOf(sampleQueries);
    }

    /**
     * Returns the list of matching stations.
     * 
     * <p>The stations are sorted by:
     * <ol>
     *   <li>Latitude (ascending)</li>
     *   <li>Longitude (ascending)</li>
     *   <li>Station name (ascending)</li>
     * </ol>
     * 
     * @return an immutable list of matching stations. Never null, may be empty.
     */
    public List<CompleteStation> getStations() {
        return stations;
    }

    /**
     * Returns the execution time of the search operation.
     * 
     * @return execution time in nanoseconds
     */
    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }

    /**
     * Returns the temporal complexity description.
     * 
     * @return a string describing the time complexity (e.g.,
     *         "Approx. O(sqrt(n) + m) with KD-tree pruning")
     */
    public String getTemporalComplexity() {
        return temporalComplexity;
    }

    /**
     * Returns example queries for reference.
     * 
     * @return an immutable list of sample query strings. Never null, may be empty.
     */
    public List<String> getSampleQueries() {
        return sampleQueries;
    }
}

