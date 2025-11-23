package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;

import java.util.Collections;
import java.util.List;

/**
 * Result object for USEI09 - Search by Circular Region (Radius Query).
 * 
 * <p>This immutable class encapsulates the results of a circular geographical
 * search, including the matching stations, their distances from the center point,
 * execution metrics, and metadata.
 * 
 * <p>All lists are immutable copies to prevent external modification.
 * The stations and distances lists are aligned: stations.get(i) corresponds
 * to distances.get(i) for the same station.
 * 
 * @author ESINF Sprint 2 Team
 * @see GeoAreaCircleSearch
 */
public class GeoAreaCircleSearchResult {

    private final List<CompleteStation> stations;
    private final List<Double> distances;
    private final long executionTimeNanos;
    private final String complexity;
    private final List<String> sampleQueries;

    /**
     * Constructs a new GeoAreaCircleSearchResult with the specified data.
     * 
     * <p>All lists are copied to ensure immutability. Null lists are replaced
     * with empty lists. The stations and distances lists must be aligned:
     * stations.get(i) corresponds to distances.get(i).
     * 
     * @param stations the list of matching stations, sorted by distance,
     *                 then latitude, longitude, and station name. May be null (treated as empty).
     * @param distances the list of distances (in km) from center point to each station,
     *                  aligned with stations list. May be null (treated as empty).
     * @param executionTimeNanos the execution time in nanoseconds
     * @param complexity a description of the time complexity (e.g.,
     *                   "Approx. O(sqrt(n) + m) with KD-tree pruning")
     * @param sampleQueries example queries for reference. May be null (treated as empty).
     */
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

    /**
     * Returns the list of matching stations.
     * 
     * <p>The stations are sorted by:
     * <ol>
     *   <li>Distance from center (ascending)</li>
     *   <li>Latitude (ascending)</li>
     *   <li>Longitude (ascending)</li>
     *   <li>Station name (ascending)</li>
     * </ol>
     * 
     * <p>The stations list is aligned with the distances list:
     * stations.get(i) corresponds to distances.get(i).
     * 
     * @return an immutable list of matching stations. Never null, may be empty.
     */
    public List<CompleteStation> getStations() {
        return stations;
    }

    /**
     * Returns the list of distances from the center point to each station.
     * 
     * <p>Distances are in kilometers, calculated using the Haversine formula.
     * The distances list is aligned with the stations list:
     * distances.get(i) is the distance to stations.get(i).
     * 
     * @return an immutable list of distances in kilometers. Never null, may be empty.
     */
    public List<Double> getDistances() {
        return distances;
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
    public String getComplexity() {
        return complexity;
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

