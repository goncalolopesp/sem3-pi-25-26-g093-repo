package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.Tree;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.StationAreaFilter;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.CompleteStationRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeoAreaCircleSearch {

    private final CompleteStationRepository repository;

    public GeoAreaCircleSearch(CompleteStationRepository repository) {
        this.repository = repository;
    }

    public GeoAreaCircleSearch() {
        this(Repositories.getInstance().getCompleteStationRepository());
    }

    private static final List<String> DEFAULT_SAMPLE_QUERIES = List.of(
            "center(40.0, -8.0) radius=50km isCity=true country=PT",
            "center(41.0, -3.0) radius=100km isMainStation=true country=ES",
            "center(45.0, 2.0) radius=200km country=ALL",
            "center(38.7, -9.1) radius=25km isCity=false isMainStation=false"
    );

    // Método principal
    public GeoAreaCircleSearchResult searchCircle(
            double centerLat,
            double centerLon,
            double radiusKm,
            Boolean isCityFilter,
            Boolean isMainStationFilter,
            String countryFilter
    ) {
        validateCircleParameters(centerLat, centerLon, radiusKm);

        long start = System.nanoTime();

        // Criar bounding box
        double latMin = centerLat - radiusKm / 111.0;
        double latMax = centerLat + radiusKm / 111.0;
        double lonMin = centerLon - radiusKm / (111.0 * Math.cos(Math.toRadians(centerLat)));
        double lonMax = centerLon + radiusKm / (111.0 * Math.cos(Math.toRadians(centerLat)));

        // Criar listas para RangeSearch
        ArrayList<Double> lower = new ArrayList<>(List.of(latMin, lonMin));
        ArrayList<Double> upper = new ArrayList<>(List.of(latMax, lonMax));

        // Obter árvore e fazer RangeSearch
        Tree<Double> tree = repository.getTree();
        if (tree == null || tree.isEmpty()) {
            throw new IllegalStateException("KD-tree is empty. Build it before querying.");
        }

        List<Tree.TreeNode<Double>> candidateNodes = tree.RangeSearch(lower, upper);

        // Filtrar por distância Haversine real
        List<CompleteStation> stationsFound = new ArrayList<>();
        Map<CompleteStation, Double> distanceMap = new HashMap<>();

        for (Tree.TreeNode<Double> node : candidateNodes) {
            for (Object payload : node.getPayloads()) {
                if (payload instanceof CompleteStation station) {
                    Double stationLat = station.getLatitude();
                    Double stationLon = station.getLongitude();
                    
                    if (stationLat != null && stationLon != null) {
                        double distance = distanceKm(centerLat, centerLon, stationLat, stationLon);
                        
                        if (distance <= radiusKm) {
                            stationsFound.add(station);
                            distanceMap.put(station, distance);
                        }
                    }
                }
            }
        }

        // Aplicar filtros opcionais
        StationAreaFilter filter =
                new StationAreaFilter(isCityFilter, isMainStationFilter, countryFilter);
        List<CompleteStation> filteredStations = filter.apply(stationsFound);

        // Criar mapa de distâncias apenas para estações filtradas
        Map<CompleteStation, Double> filteredDistanceMap = new HashMap<>();
        for (CompleteStation station : filteredStations) {
            filteredDistanceMap.put(station, distanceMap.get(station));
        }

        // Ordenar
        Comparator<CompleteStation> cmp =
                Comparator.<CompleteStation, Double>comparing(station -> filteredDistanceMap.get(station))
                          .thenComparing(CompleteStation::getLatitude)
                          .thenComparing(CompleteStation::getLongitude)
                          .thenComparing(CompleteStation::getStation);

        filteredStations.sort(cmp);

        // Ajustar lista de distâncias na mesma ordem
        List<Double> filteredDistances = new ArrayList<>();
        for (CompleteStation station : filteredStations) {
            filteredDistances.add(filteredDistanceMap.get(station));
        }

        long elapsed = System.nanoTime() - start;

        return new GeoAreaCircleSearchResult(
                filteredStations,
                filteredDistances,
                elapsed,
                "Approx. O(sqrt(n) + m) with KD-tree pruning",
                DEFAULT_SAMPLE_QUERIES
        );
    }

    private void validateCircleParameters(double centerLat, double centerLon, double radiusKm) {
        if (centerLat < -90 || centerLat > 90) {
            throw new IllegalArgumentException("Latitude must be within [-90, 90].");
        }
        if (centerLon < -180 || centerLon > 180) {
            throw new IllegalArgumentException("Longitude must be within [-180, 180].");
        }
        if (radiusKm <= 0) {
            throw new IllegalArgumentException("Radius must be greater than 0.");
        }
    }

    private double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2)*Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon/2)*Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}

