package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.StationDistance;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.CompleteStationRepository;

import java.util.*;

public class GeoRadiusSearchService {

    /** Lista de estações carregadas — pode ser substituída em testes */
    private List<CompleteStation> stations;

    public GeoRadiusSearchService() {
        // Carrega estações reais a partir do repositório real
        this.stations = CompleteStationRepository.getInstance().getStations();
    }

    /** Usado apenas nos testes unitários para injetar estações mock */
    public void injectMockStations(List<CompleteStation> mockStations) {
        this.stations = mockStations;
    }

    /** ================= RESULT STRUCT ================= */
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

    /** =================== SEARCH LOGIC ====================== */
    public Result search(double lat, double lon, double radiusKm) {

        if (stations == null || stations.isEmpty())
            throw new IllegalStateException("There are no stations loaded.");

        List<StationDistance> inside = new ArrayList<>();
        Map<String, Integer> perCountry = new HashMap<>();
        int city = 0;
        int nonCity = 0;

        for (CompleteStation s : stations) {
            double d = distanceKm(lat, lon, s.getLatitude(), s.getLongitude());

            if (d <= radiusKm) {
                inside.add(new StationDistance(s, d));

                perCountry.put(s.getCountry(),
                        perCountry.getOrDefault(s.getCountry(), 0) + 1);

                if (s.isCity()) city++;
                else nonCity++;
            }
        }

        inside.sort(Comparator.naturalOrder());

        return new Result(inside, perCountry, city, nonCity);
    }

    /** Haversine formula */
    private double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
