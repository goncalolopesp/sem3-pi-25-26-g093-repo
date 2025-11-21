package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.Tree;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.StationAreaFilter;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.CompleteStationRepository;

import java.util.ArrayList;
import java.util.Arrays;
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

        ArrayList<Double> lower = new ArrayList<>(Arrays.asList(latMin, lonMin));
        ArrayList<Double> upper = new ArrayList<>(Arrays.asList(latMax, lonMax));

        long start = System.nanoTime();
        List<Tree.TreeNode<Double>> candidateNodes = tree.RangeSearch(lower, upper);
        List<CompleteStation> matches = new ArrayList<>();

        for (Tree.TreeNode<Double> node : candidateNodes) {
            for (Object payload : node.getPayloads()) {
                if (payload instanceof CompleteStation station &&
                        withinBounds(station, latMin, latMax, lonMin, lonMax) &&
                        effectiveFilter.matches(station)) {
                    matches.add(station);
                }
            }
        }

        matches.sort(Comparator
                .comparing(CompleteStation::getLatitude)
                .thenComparing(CompleteStation::getLongitude)
                .thenComparing(CompleteStation::getStation));

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

        Double lat = station.getLatitude();
        Double lon = station.getLongitude();
        if (lat == null || lon == null) return false;

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
