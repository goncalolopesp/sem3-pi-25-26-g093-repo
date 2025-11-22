package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.Tree;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.StationAreaFilter;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.CompleteStationRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GeoAreaSearch {

    private static final List<String> DEFAULT_SAMPLE_QUERIES = List.of(
            "lat[37.0, 41.5] lon[-9.5, -6.0] isCity=true country=PT",
            "lat[40.0, 43.0] lon[-8.0, -2.0] isMainStation=true country=ES",
            "lat[35.0, 45.0] lon[-10.0, 5.0] country=ALL",
            "lat[38.5, 39.5] lon[-9.0, -8.5] isCity=false isMainStation=false"
    );

    private final CompleteStationRepository repository;

    public GeoAreaSearch(CompleteStationRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("CompleteStationRepository is required");
        }
        this.repository = repository;
    }

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
