package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.CompleteStationRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;

import java.util.*;


/**
 * Controller for the USEI06 - filters the data from the Repository to return to the UI.
 */
public class CompleteStationIndexController {

    private final CompleteStationRepository repo;

    /**
     * Gets the repository instance.
     */
    public CompleteStationIndexController() {
        this.repo = Repositories.getInstance().getCompleteStationRepository();
    }

    /**
     * Checks if stations are loaded.
     * @return true if stations are loaded, false otherwise.
     */
    private boolean stationsLoaded() {
        return repo != null && repo.getStations() != null && !repo.getStations().isEmpty();
    }

    /**
     * Gets stations by latitude range.
     * @param min the lower value of the range
     * @param max the upper value of the range
     * @return a map with the execution time as key and the list of stations as value
     */
    public Map<Double,List<CompleteStation>> getByLatitudeRange(double min, double max) {

        long startTime = System.nanoTime();

        if (!stationsLoaded()) {
            return null;
        }

        if (min > max) {
            double tmp = min;
            min = max;
            max = tmp;
        }

        List<CompleteStation> result = new ArrayList<>();
        for (CompleteStation cs : repo.getStations()) {
            Double lat = cs.getLatitude();
            if (lat == null) continue;
            if (lat >= min && lat <= max) {
                result.add(cs);
            }
        }

        result.sort(Comparator
                .comparing(CompleteStation::getLatitude, Comparator.nullsLast(Double::compareTo))
                .thenComparing(CompleteStation::getStation)
                .thenComparing(CompleteStation::getCountry));

        long endTime = System.nanoTime();

        Double executionTime = (endTime - startTime) / 1e6;

        Map<Double, List<CompleteStation>> resultado = new HashMap<>();
        resultado.put(executionTime, result);

        return resultado;
    }

    /**
     * Gets stations by longitude range.
     * @param min the lower value of the range
     * @param max the upper value of the range
     * @return a map with the execution time as key and the list of stations as value
     */
    public Map<Double,List<CompleteStation>> getByLongitudeRange(double min, double max) {

        long startTime = System.nanoTime();

        if (!stationsLoaded()) {
            return null;
        }

        if (min > max) {
            double tmp = min;
            min = max;
            max = tmp;
        }

        List<CompleteStation> result = new ArrayList<>();
        for (CompleteStation cs : repo.getStations()) {
            Double lon = cs.getLongitude();
            if (lon == null) continue;
            if (lon >= min && lon <= max) {
                result.add(cs);
            }
        }

        result.sort(Comparator
                .comparing(CompleteStation::getLongitude, Comparator.nullsLast(Double::compareTo))
                .thenComparing(CompleteStation::getStation)
                .thenComparing(CompleteStation::getCountry));

        long endTime = System.nanoTime();

        Double executionTime = (endTime - startTime) / 1e6;

        Map<Double, List<CompleteStation>> resultado = new HashMap<>();
        resultado.put(executionTime, result);

        return resultado;
    }

    /**
     * Gets stations by time zone group.
     * @param tzg the time zone group
     * @return a map with the execution time as key and the list of stations as value
     */
    public Map<Double, List<CompleteStation>> getByTimeZoneGroup(String tzg) {

        long startTime = System.nanoTime();

        if (!stationsLoaded()) {
            return null;
        }
        if (tzg == null || tzg.isBlank()) return new HashMap<>();

        String target = tzg.trim();

        List<CompleteStation> result = new ArrayList<>();
        for (CompleteStation cs : repo.getStations()) {
            if (target.equalsIgnoreCase(cs.getTimeZoneGroup())) {
                result.add(cs);
            }
        }

        result.sort(Comparator
                .comparing(CompleteStation::getCountry)
                .thenComparing(CompleteStation::getStation));

        long endTime = System.nanoTime();

        Double executionTime = (endTime - startTime) / 1e6;

        Map<Double, List<CompleteStation>> resultado = new HashMap<>();
        resultado.put(executionTime, result);

        return resultado;
    }

    /**
     * Gets stations within a TimeZone Group Window.
     * @param start the start of the window
     * @param end the end of the window
     * @return a map with the execution time as key and the list of stations as value
     */
    public Map<Double, List<CompleteStation>> getByTimeZoneGroupWindow(String start, String end) {

        long startTime = System.nanoTime();

        if (!stationsLoaded()) {
            return null;
        }
        if (start == null || end == null) return new HashMap<>();

        String a = start.trim();
        String b = end.trim();
        if (a.compareTo(b) > 0) {
            String tmp = a;
            a = b;
            b = tmp;
        }

        List<CompleteStation> result = new ArrayList<>();
        for (CompleteStation cs : repo.getStations()) {
            String g = cs.getTimeZoneGroup();
            if (g == null) continue;
            if (g.compareTo(a) >= 0 && g.compareTo(b) <= 0) {
                result.add(cs);
            }
        }

        result.sort(Comparator
                .comparing(CompleteStation::getTimeZoneGroup)
                .thenComparing(CompleteStation::getCountry)
                .thenComparing(CompleteStation::getStation));

        long endTime = System.nanoTime();

        Double executionTime = (endTime - startTime) / 1e6;

        Map<Double, List<CompleteStation>> resultado = new HashMap<>();
        resultado.put(executionTime, result);

        return resultado;
    }

    /**
     * Gets stations by time zone group and country.
     * @param tzg the Time Zone Group
     * @param country the Country
     * @return a map with the execution time as key and the list of stations as value
     */
    public Map<Double, List<CompleteStation>> getByTimeZoneGroupAndCountry(String tzg, String country) {

        long startTime = System.nanoTime();

        if (!stationsLoaded()) {
            return null;
        }
        if (tzg == null || country == null) return new HashMap<>();

        String tzgNorm = tzg.trim();
        String countryNorm = country.trim();

        List<CompleteStation> result = new ArrayList<>();
        for (CompleteStation cs : repo.getStations()) {
            if (cs.getTimeZoneGroup() == null || cs.getCountry() == null) continue;

            if (tzgNorm.equalsIgnoreCase(cs.getTimeZoneGroup())
                    && countryNorm.equalsIgnoreCase(cs.getCountry())) {
                result.add(cs);
            }
        }

        result.sort(Comparator.comparing(CompleteStation::getStation));
        long endTime = System.nanoTime();

        Double executionTime = (endTime - startTime) / 1e6;

        Map<Double, List<CompleteStation>> resultado = new HashMap<>();
        resultado.put(executionTime, result);

        return resultado;
    }

}
