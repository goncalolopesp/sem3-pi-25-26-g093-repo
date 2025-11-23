package pt.ipp.isep.dei.pi25_26.g093.app.service;


import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.AVL;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.StationDistance;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.CompleteStationRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeoRadiusSearchService {

    private final CompleteStationRepository repo;

    public GeoRadiusSearchService() {
        this.repo = Repositories.getInstance().getCompleteStationRepository();
    }

    /**
     * Performs a radius search around a given coordinate.
     *
     * @param centerLat  latitude of center point
     * @param centerLon  longitude of center point
     * @param radiusKm   radius in kilometers
     * @return Result object containing sorted stations and summaries
     */
    public Result search(double centerLat, double centerLon, double radiusKm) {

        if (repo.getStations().isEmpty()) {
            throw new IllegalStateException("There are no stations loaded.");
        }

        AVL<StationDistance> tree = new AVL<>();
        Map<String, Integer> countryCounts = new HashMap<>();

        int cityCount = 0;
        int nonCityCount = 0;

        for (CompleteStation s : repo.getStations()) {

            double d = haversine(centerLat, centerLon, s.getLatitude(), s.getLongitude());

            if (d <= radiusKm) {

                StationDistance sd = new StationDistance(s, d);
                tree.insert(sd);

                // update country counter
                countryCounts.merge(s.getCountry(), 1, Integer::sum);

                // update city vs non-city
                if (s.isCity()) cityCount++;
                else nonCityCount++;
            }
        }

        List<StationDistance> ordered = tree.inOrder();

        return new Result(ordered, countryCounts, cityCount, nonCityCount);
    }


    /**
     * Haversine distance in km between two coordinates.
     */
    private double haversine(double lat1, double lon1, double lat2, double lon2) {

        final double R = 6371.0; // Earth radius (km)

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }


    /**
     * Simple container for results (perfect for UI and unit testing)
     */
    public static class Result {
        private final List<StationDistance> stations;
        private final Map<String, Integer> countryCounts;
        private final int cityCount;
        private final int nonCityCount;

        public Result(List<StationDistance> stations,
                      Map<String, Integer> countryCounts,
                      int cityCount,
                      int nonCityCount) {

            this.stations = stations;
            this.countryCounts = countryCounts;
            this.cityCount = cityCount;
            this.nonCityCount = nonCityCount;
        }

        public List<StationDistance> getStations() { return stations; }
        public Map<String, Integer> getCountryCounts() { return countryCounts; }
        public int getCityCount() { return cityCount; }
        public int getNonCityCount() { return nonCityCount; }
    }
}

