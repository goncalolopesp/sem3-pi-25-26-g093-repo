package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.Tree;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.StationAreaFilter;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.CompleteStationRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Service class for USEI08 - Search by Geographical Area (Rectangular Query).
 * 
 * <p>This service performs efficient rectangular region searches over railway stations
 * using a 2D KD-tree (Tree&lt;Double&gt;). The search uses RangeSearch to prune
 * candidates before applying filters, ensuring optimal performance even with large
 * datasets (~62k stations).
 * 
 * <p>The search accepts:
 * <ul>
 *   <li>A rectangular region defined by latitude and longitude bounds</li>
 *   <li>Optional filters: isCity, isMainStation, country</li>
 * </ul>
 * 
 * <p>Results are sorted by latitude (ascending), then longitude (ascending),
 * then station name (ascending).
 * 
 * <p>Time complexity: Approximately O(sqrt(n) + m) where n is the total number
 * of stations and m is the number of stations in the result set.
 * 
 * @author ESINF Sprint 2 Team
 * @see GeoAreaSearchResult
 * @see StationAreaFilter
 * @see Tree
 */
public class GeoAreaSearch {

    private static final List<String> DEFAULT_SAMPLE_QUERIES = List.of(
            "lat[37.0, 41.5] lon[-9.5, -6.0] isCity=true country=PT",
            "lat[40.0, 43.0] lon[-8.0, -2.0] isMainStation=true country=ES",
            "lat[35.0, 45.0] lon[-10.0, 5.0] country=ALL",
            "lat[38.5, 39.5] lon[-9.0, -8.5] isCity=false isMainStation=false"
    );

    private final CompleteStationRepository repository;

    /**
     * Constructs a new GeoAreaSearch service with the specified repository.
     * 
     * @param repository the CompleteStationRepository containing the KD-tree
     *                   and station data. Must not be null.
     * @throws IllegalArgumentException if repository is null
     */
    public GeoAreaSearch(CompleteStationRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("CompleteStationRepository is required");
        }
        this.repository = repository;
    }

    /**
     * Searches for stations within a rectangular geographical area using the KD-tree.
     * 
     * <p>The search process:
     * <ol>
     *   <li>Validates input ranges (latitude [-90, 90], longitude [-180, 180])</li>
     *   <li>Uses KD-tree RangeSearch to find candidate stations within bounding box</li>
     *   <li>Filters candidates by exact bounds (handles stations sharing coordinates)</li>
     *   <li>Applies optional StationAreaFilter (isCity, isMainStation, country)</li>
     *   <li>Sorts results by latitude, longitude, then station name</li>
     * </ol>
     * 
     * <p>The KD-tree must be built before calling this method (typically done
     * during application startup via USEI07).
     * 
     * @param latMin minimum latitude of the search rectangle (inclusive, range [-90, 90])
     * @param latMax maximum latitude of the search rectangle (inclusive, range [-90, 90])
     * @param lonMin minimum longitude of the search rectangle (inclusive, range [-180, 180])
     * @param lonMax maximum longitude of the search rectangle (inclusive, range [-180, 180])
     * @param filter optional filter for isCity, isMainStation, and country.
     *               If null, no filtering is applied (all countries).
     * @return GeoAreaSearchResult containing matching stations, execution time,
     *         complexity description, and sample queries. Never null.
     * @throws IllegalArgumentException if:
     *         <ul>
     *           <li>latMin &gt; latMax</li>
     *           <li>lonMin &gt; lonMax</li>
     *           <li>latitude values are outside [-90, 90]</li>
     *           <li>longitude values are outside [-180, 180]</li>
     *         </ul>
     * @throws IllegalStateException if the KD-tree is null or empty
     *         (must be built before querying)
     */
    public GeoAreaSearchResult searchByArea(double latMin, double latMax,
                                            double lonMin, double lonMax,
                                            StationAreaFilter filter) {

        validateRanges(latMin, latMax, lonMin, lonMax);

        StationAreaFilter effectiveFilter = (filter == null)
                ? new StationAreaFilter(null, null, "ALL")
                : filter;

        Tree<Double> tree = repository.getTree();
        if (tree == null || tree.isEmpty()) {
            throw new IllegalStateException("KD-tree is empty. Build it before querying.");
        }

        // Create bounds lists more efficiently
        ArrayList<Double> lower = new ArrayList<>(2);
        lower.add(latMin);
        lower.add(lonMin);
        ArrayList<Double> upper = new ArrayList<>(2);
        upper.add(latMax);
        upper.add(lonMax);

        long start = System.nanoTime();
        List<Tree.TreeNode<Double>> candidateNodes = tree.RangeSearch(lower, upper);
        
        // Pre-allocate list with estimated size (nodes typically contain 1-2 stations)
        int estimatedSize = Math.max(16, candidateNodes.size());
        List<CompleteStation> matches = new ArrayList<>(estimatedSize);

        // Process nodes and filter stations
        for (Tree.TreeNode<Double> node : candidateNodes) {
            List<Object> payloads = node.getPayloads();
            for (Object payload : payloads) {
                if (payload instanceof CompleteStation station) {
                    // Verify station is within bounds (necessary because multiple stations
                    // can share same coordinates, and we need to check each one)
                    if (withinBounds(station, latMin, latMax, lonMin, lonMax) &&
                            effectiveFilter.matches(station)) {
                        matches.add(station);
                    }
                }
            }
        }

        // Sort results: latitude (asc), then longitude (asc), then station name (asc)
        // Using method references for better performance
        matches.sort(Comparator
                .comparing(CompleteStation::getLatitude, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(CompleteStation::getLongitude, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(CompleteStation::getStation, Comparator.nullsLast(Comparator.naturalOrder())));

        long elapsed = System.nanoTime() - start;

        return new GeoAreaSearchResult(
                matches,
                elapsed,
                "Approx. O(sqrt(n) + m) with KD-tree pruning",
                DEFAULT_SAMPLE_QUERIES
        );
    }

    private boolean withinBounds(CompleteStation station,
                                 double latMin, double latMax,
                                 double lonMin, double lonMax) {
        // Cache getter calls to avoid repeated method invocations
        Double lat = station.getLatitude();
        Double lon = station.getLongitude();
        
        // Null check (stations without coordinates should not be in tree, but defensive check)
        if (lat == null || lon == null) {
            return false;
        }

        // Use inclusive bounds check
        return lat >= latMin && lat <= latMax &&
               lon >= lonMin && lon <= lonMax;
    }

    private void validateRanges(double latMin, double latMax,
                                double lonMin, double lonMax) {
        if (latMin > latMax) {
            throw new IllegalArgumentException("latMin cannot be greater than latMax.");
        }
        if (lonMin > lonMax) {
            throw new IllegalArgumentException("lonMin cannot be greater than lonMax.");
        }
        if (latMin < -90 || latMax > 90) {
            throw new IllegalArgumentException("Latitude must be within [-90, 90].");
        }
        if (lonMin < -180 || lonMax > 180) {
            throw new IllegalArgumentException("Longitude must be within [-180, 180].");
        }
    }
}
