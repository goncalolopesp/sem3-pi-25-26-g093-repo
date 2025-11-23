package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.StationDistance;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.CompleteStationRepository;

import java.util.*;

public class GeoRadiusSearchService {

    // stations que vêm do repositório ou do mock nos testes
    private List<CompleteStation> stations;

    public GeoRadiusSearchService() {
        // carregar estações reais
        this.stations = CompleteStationRepository
                .getInstance()
                .getAllStations();
    }

    // usado apenas em testes unitários
    public void injectMockStations(List<CompleteStation> mockStations) {
        this.stations = mockStations;
    }

    /* ===== RESULT ===== */
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

    /* ===== SEARCH ===== */

    public Result search(double centerLat, double centerLon, double radiusKm) {
        if (stations == null || stations.isEmpty())
            throw new IllegalStateException("There are no stations loaded.");

        List<StationDistance> inside = new ArrayList<>();

        for (CompleteStation s : stations) {
            double dist = haversine(centerLat, centerLon, s.getLatitude(), s.getLongitude());
            if (dist <= radiusKm) inside.add(new StationDistance(s, dist));
        }

        Collections.sort(inside);

        Map<String, Integer> countryCount = new HashMap<>();
        int city = 0, nonCity = 0;

        for (StationDistance sd : inside) {
            CompleteStation s = sd.getStation();

            countryCount.put(s.getCountry(),
                    countryCount.getOrDefault(s.getCountry(), 0) + 1);

            if (s.isCity()) city++;
            else nonCity++;
        }

        return new Result(inside, countryCount, city, nonCity);
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2)*Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon/2)*Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
