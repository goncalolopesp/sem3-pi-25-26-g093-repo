package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.Tree;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.StationAreaFilter;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.CompleteStationRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Service class for USEI09 - Proximity Search (Nearest-N with Filters).
 * 
 * <p>This service performs efficient nearest-N proximity searches over railway stations
 * using a 2D KD-tree with backtracking and pruning. The algorithm finds the N closest
 * stations to a target point using Haversine distance.
 * 
 * <p>The search process:
 * <ol>
 *   <li>Traverses the KD-tree with backtracking</li>
 *   <li>Maintains a max-heap of size N to store the N closest stations</li>
 *   <li>Prunes branches where minimum possible distance exceeds the N-th closest distance</li>
 *   <li>Applies optional filters (isCity, isMainStation, country, timeZone, timeZoneGroup) during search</li>
 *   <li>Returns exactly N nearest stations (unless filters reduce the count)</li>
 * </ol>
 * 
 * <p>The Haversine formula uses Earth radius = 6371.0 km for accurate distance
 * calculations between geographical coordinates.
 * 
 * <p>Time complexity: O(log n + k) where n is the total number of stations and
 * k is the number of nodes visited (typically much less than n due to pruning).
 * 
 * @author ESINF Sprint 2 Team
 * @see NearestNSearchResult
 * @see StationAreaFilter
 * @see Tree
 */
public class NearestNSearchService {

    private final CompleteStationRepository repository;
    private static final double EARTH_RADIUS_KM = 6371.0;

    /**
     * Constructs a new NearestNSearchService service with the specified repository.
     * 
     * @param repository the CompleteStationRepository containing the KD-tree
     *                   and station data. Must not be null.
     */
    public NearestNSearchService(CompleteStationRepository repository) {
        this.repository = repository;
    }

    /**
     * Constructs a new NearestNSearchService service using the default repository instance.
     */
    public NearestNSearchService() {
        this(Repositories.getInstance().getCompleteStationRepository());
    }

    private static final List<String> DEFAULT_SAMPLE_QUERIES = List.of(
            "center(40.0, -8.0) n=10 isCity=true country=PT timeZoneGroup=CET",
            "center(41.0, -3.0) n=20 isMainStation=true country=ES",
            "center(45.0, 2.0) n=15 timeZoneGroup=WET",
            "center(38.7, -9.1) n=5 isCity=false isMainStation=false timeZone=Europe/Lisbon"
    );

    /**
     * Searches for the N nearest stations to a target point using KD-tree
     * nearest-N search with backtracking and pruning.
     * 
     * <p>The search process:
     * <ol>
     *   <li>Validates input parameters (latitude [-90, 90], longitude [-180, 180], n &gt; 0)</li>
     *   <li>Traverses the KD-tree with backtracking</li>
     *   <li>Maintains a max-heap of size N to store the N closest stations</li>
     *   <li>Prunes branches where minimum possible distance exceeds the N-th closest distance</li>
     *   <li>Applies optional StationAreaFilter during search (early pruning)</li>
     *   <li>Returns exactly N nearest stations sorted by distance (ascending), then station name</li>
     * </ol>
     * 
     * <p>The KD-tree must be built before calling this method (typically done
     * during application startup via USEI07).
     * 
     * @param centerLat center point latitude (range [-90, 90])
     * @param centerLon center point longitude (range [-180, 180])
     * @param n number of nearest stations to find (must be &gt; 0)
     * @param isCityFilter filter for city stations (true = only cities, false = only non-cities, null = no filter)
     * @param isMainStationFilter filter for main stations (true = only main, false = only non-main, null = no filter)
     * @param countryFilter filter for country ("PT", "ES", "ALL", or null = all countries)
     * @param timeZoneFilter filter for time zone (exact match, case-insensitive), or null = no filter
     * @param timeZoneGroupFilter filter for time zone group (exact match, case-insensitive), or null = no filter
     * @return NearestNSearchResult containing the N nearest stations with distances,
     *         execution time, complexity metrics, and sample queries. Never null.
     * @throws IllegalArgumentException if:
     *         <ul>
     *           <li>centerLat is outside [-90, 90]</li>
     *           <li>centerLon is outside [-180, 180]</li>
     *           <li>n &lt;= 0</li>
     *         </ul>
     * @throws IllegalStateException if the KD-tree is null or empty
     *         (must be built before querying)
     */
    public NearestNSearchResult searchNearestN(
            double centerLat,
            double centerLon,
            int n,
            Boolean isCityFilter,
            Boolean isMainStationFilter,
            String countryFilter,
            String timeZoneFilter,
            String timeZoneGroupFilter
    ) {
        validateNearestNParameters(centerLat, centerLon, n);

        long start = System.nanoTime();

        Tree<Double> tree = repository.getTree();
        if (tree == null || tree.isEmpty()) {
            throw new IllegalStateException("KD-tree is empty. Build it before querying.");
        }

        // Create filter
        StationAreaFilter filter = new StationAreaFilter(
                isCityFilter, isMainStationFilter, countryFilter,
                timeZoneFilter, timeZoneGroupFilter
        );

        // Max-heap to maintain N closest stations (reverse comparator for max-heap)
        PriorityQueue<StationDistance> maxHeap = new PriorityQueue<>(
                n + 1,  // +1 to allow easy peek at max
                Comparator.<StationDistance>comparingDouble(StationDistance::getDistance)
                        .reversed()
                        .thenComparing(sd -> sd.getStation().getStation(), 
                                Comparator.nullsLast(Comparator.naturalOrder()))
        );

        // Perform nearest-N search with backtracking
        nearestNSearch(tree.getRoot(), centerLat, centerLon, n, filter, maxHeap, 0);

        // Extract results from max-heap and sort
        List<StationDistance> results = new ArrayList<>(maxHeap);
        results.sort(Comparator
                .<StationDistance>comparingDouble(StationDistance::getDistance)
                .thenComparing(sd -> sd.getStation().getStation(),
                        Comparator.nullsLast(Comparator.naturalOrder())));

        // Limit to exactly N (in case filters reduced count)
        if (results.size() > n) {
            results = results.subList(0, n);
        }

        // Separate stations and distances
        List<CompleteStation> stations = new ArrayList<>();
        List<Double> distances = new ArrayList<>();
        for (StationDistance sd : results) {
            stations.add(sd.getStation());
            distances.add(sd.getDistance());
        }

        long elapsed = System.nanoTime() - start;

        return new NearestNSearchResult(
                stations,
                distances,
                elapsed,
                DEFAULT_SAMPLE_QUERIES
        );
    }

    /**
     * Recursive nearest-N search with backtracking and pruning.
     * 
     * @param node current node in the KD-tree
     * @param queryLat query point latitude
     * @param queryLon query point longitude
     * @param n number of nearest neighbors to find
     * @param filter filter to apply during search
     * @param maxHeap max-heap of size N to maintain closest stations
     * @param depth current depth in the tree (for dimension selection)
     */
    private void nearestNSearch(
            Tree.TreeNode<Double> node,
            double queryLat,
            double queryLon,
            int n,
            StationAreaFilter filter,
            PriorityQueue<StationDistance> maxHeap,
            int depth
    ) {
        if (node == null) {
            return;
        }

        // Get current dimension (alternates between 0=latitude and 1=longitude)
        int dimension = depth % 2;
        double nodeValue = node.getElement(dimension).doubleValue();
        double queryValue = (dimension == 0) ? queryLat : queryLon;

        // Determine which subtree to explore first (closer to query point)
        Tree.TreeNode<Double> nearChild;
        Tree.TreeNode<Double> farChild;

        if (queryValue < nodeValue) {
            nearChild = node.getLeft();
            farChild = node.getRight();
        } else {
            nearChild = node.getRight();
            farChild = node.getLeft();
        }

        // Recursively search the near subtree first
        nearestNSearch(nearChild, queryLat, queryLon, n, filter, maxHeap, depth + 1);

        // Process current node's payloads (stations at this coordinate)
        double nodeLat = node.getElement(0).doubleValue();
        double nodeLon = node.getElement(1).doubleValue();
        double nodeDistance = haversineDistance(queryLat, queryLon, nodeLat, nodeLon);

        // Get the current N-th best distance (or infinity if heap not full)
        double nthBestDistance = (maxHeap.size() >= n) ? maxHeap.peek().getDistance() : Double.POSITIVE_INFINITY;

        // Check if this node could contain stations closer than the N-th best
        if (nodeDistance <= nthBestDistance || maxHeap.size() < n) {
            // Process all stations at this node
            for (Object payload : node.getPayloads()) {
                if (payload instanceof CompleteStation station) {
                    // Apply filter early
                    if (filter.matches(station)) {
                        double stationDistance = haversineDistance(
                                queryLat, queryLon,
                                station.getLatitude(), station.getLongitude()
                        );

                        // Add to heap if better than N-th best or heap not full
                        if (maxHeap.size() < n) {
                            maxHeap.offer(new StationDistance(station, stationDistance));
                        } else if (stationDistance < nthBestDistance) {
                            maxHeap.poll(); // Remove worst
                            maxHeap.offer(new StationDistance(station, stationDistance));
                        } else if (stationDistance == nthBestDistance) {
                            // Tie: add if station name is lexicographically smaller
                            StationDistance worst = maxHeap.peek();
                            if (worst != null) {
                                String stationName = station.getStation();
                                String worstName = worst.getStation().getStation();
                                if (stationName != null && worstName != null &&
                                    stationName.compareTo(worstName) < 0) {
                                    maxHeap.poll();
                                    maxHeap.offer(new StationDistance(station, stationDistance));
                                }
                            }
                        }
                    }
                }
            }
        }

        // Update nthBestDistance after processing node
        nthBestDistance = (maxHeap.size() >= n) ? maxHeap.peek().getDistance() : Double.POSITIVE_INFINITY;

        // Calculate minimum possible distance from query to the far subtree
        // This is the distance from query point to the split plane
        double splitPlaneDistance = Math.abs(queryValue - nodeValue);

        // For latitude dimension, convert to approximate km (1 degree ≈ 111 km)
        // For longitude dimension, convert using latitude-dependent factor
        double minPossibleDistance;
        if (dimension == 0) {
            // Latitude dimension: 1 degree ≈ 111 km
            minPossibleDistance = splitPlaneDistance * 111.0;
        } else {
            // Longitude dimension: depends on latitude
            double latRad = Math.toRadians(queryLat);
            minPossibleDistance = splitPlaneDistance * 111.0 * Math.cos(latRad);
        }

        // Prune far subtree if minimum possible distance exceeds N-th best
        if (minPossibleDistance < nthBestDistance) {
            // Need to search far subtree
            nearestNSearch(farChild, queryLat, queryLon, n, filter, maxHeap, depth + 1);
        }
    }

    /**
     * Calculates Haversine distance between two points on Earth.
     * 
     * @param lat1 latitude of first point
     * @param lon1 longitude of first point
     * @param lat2 latitude of second point
     * @param lon2 longitude of second point
     * @return distance in kilometers
     */
    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    private void validateNearestNParameters(double centerLat, double centerLon, int n) {
        if (centerLat < -90 || centerLat > 90) {
            throw new IllegalArgumentException("Latitude must be within [-90, 90].");
        }
        if (centerLon < -180 || centerLon > 180) {
            throw new IllegalArgumentException("Longitude must be within [-180, 180].");
        }
        if (n <= 0) {
            throw new IllegalArgumentException("N must be greater than 0.");
        }
    }
}

