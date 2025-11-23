package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents the optional filtering criteria used in USEI08 and USEI09
 * (Search by Geographical Area).
 *
 * <p>Each field may be:
 * <ul>
 *   <li>a Boolean value (true/false) to apply the filter, or</li>
 *   <li>null, meaning "no filter" for that field.</li>
 * </ul>
 *
 * <p>The "country" filter may be:
 * <ul>
 *   <li>"PT" - filter for Portuguese stations only</li>
 *   <li>"ES" - filter for Spanish stations only</li>
 *   <li>"ALL" (or null) - no country filter (all countries)</li>
 * </ul>
 *
 * <p>Filters are applied with AND logic: a station must match all
 * specified filter criteria to be included in results.
 * 
 * @author ESINF Sprint 2 Team
 * @see GeoAreaSearch
 * @see GeoAreaCircleSearch
 */
public class StationAreaFilter {

    private final Boolean isCity;
    private final Boolean isMainStation;
    private final String country;

    /**
     * Constructs a filter with the given options.
     *
     * @param isCity          filter only city stations (true), only non-city (false), or no filter (null)
     * @param isMainStation   filter only main stations (true), only non-main (false), or no filter (null)
     * @param country         "PT", "ES", "ALL", or null (null behaves as "ALL")
     */
    public StationAreaFilter(Boolean isCity, Boolean isMainStation, String country) {
        this.isCity = isCity;
        this.isMainStation = isMainStation;
        this.country = normalizeCountry(country);
    }

    private String normalizeCountry(String country) {
        if (country == null || country.isBlank()) {
            return "ALL";
        }
        String normalized = country.trim().toUpperCase(Locale.ROOT);
        return switch (normalized) {
            case "PT", "ES", "ALL" -> normalized;
            default -> "ALL";
        };
    }

    /**
     * Returns the city filter value.
     * 
     * @return true if filtering for city stations only,
     *         false if filtering for non-city stations only,
     *         null if no city filter is applied
     */
    public Boolean getIsCity() {
        return isCity;
    }

    /**
     * Returns the main station filter value.
     * 
     * @return true if filtering for main stations only,
     *         false if filtering for non-main stations only,
     *         null if no main station filter is applied
     */
    public Boolean getIsMainStation() {
        return isMainStation;
    }

    /**
     * Returns the country filter value.
     * 
     * @return "PT" for Portuguese stations only,
     *         "ES" for Spanish stations only,
     *         "ALL" if no country filter is applied
     */
    public String getCountry() {
        return country;
    }

    /**
     * Applies the filter to a list of stations.
     *
     * @param input list of stations that may be null
     * @return filtered list (never null)
     */
    public List<CompleteStation> apply(List<CompleteStation> input) {
        if (input == null || input.isEmpty()) {
            return Collections.emptyList();
        }
        return input.stream()
                .filter(Objects::nonNull)
                .filter(this::matches)
                .collect(Collectors.toList());
    }

    /**
     * Checks if the provided station matches the filter.
     * Optimized with early returns and cached getter calls.
     *
     * @param station station to evaluate
     * @return true if the station satisfies the filter (or if no filters are set)
     */
    public boolean matches(CompleteStation station) {
        if (station == null) {
            return false;
        }

        // Early return for most selective filters first (country is typically most selective)
        if (!"ALL".equals(country)) {
            String stationCountry = station.getCountry();
            if (stationCountry == null || !country.equalsIgnoreCase(stationCountry)) {
                return false;
            }
        }

        // Check boolean filters (cache getter calls)
        if (isCity != null) {
            if (station.isCity() != isCity) {
                return false;
            }
        }

        if (isMainStation != null) {
            if (station.isMainStation() != isMainStation) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "StationAreaFilter{" +
                "isCity=" + isCity +
                ", isMainStation=" + isMainStation +
                ", country='" + country + '\'' +
                '}';
    }
}
