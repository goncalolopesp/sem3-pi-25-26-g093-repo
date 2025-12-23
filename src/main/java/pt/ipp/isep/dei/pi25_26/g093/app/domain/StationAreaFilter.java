package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import pt.ipp.isep.dei.pi25_26.g093.app.service.GeoAreaSearch;
import pt.ipp.isep.dei.pi25_26.g093.app.service.NearestNSearchService;

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
 * @see NearestNSearchService
 */
public class StationAreaFilter {

    private final Boolean isCity;
    private final Boolean isMainStation;
    private final String country;
    private final String timeZone;
    private final String timeZoneGroup;

    /**
     * Constructs a filter with the given options (without time zone filters).
     *
     * @param isCity          filter only city stations (true), only non-city (false), or no filter (null)
     * @param isMainStation   filter only main stations (true), only non-main (false), or no filter (null)
     * @param country         "PT", "ES", "ALL", or null (null behaves as "ALL")
     */
    public StationAreaFilter(Boolean isCity, Boolean isMainStation, String country) {
        this(isCity, isMainStation, country, null, null);
    }

    /**
     * Constructs a filter with the given options (including time zone filters).
     *
     * @param isCity          filter only city stations (true), only non-city (false), or no filter (null)
     * @param isMainStation   filter only main stations (true), only non-main (false), or no filter (null)
     * @param country         "PT", "ES", "ALL", or null (null behaves as "ALL")
     * @param timeZone        filter by time zone (exact match, case-insensitive), or null for no filter
     * @param timeZoneGroup   filter by time zone group (exact match, case-insensitive), or null for no filter
     */
    public StationAreaFilter(Boolean isCity, Boolean isMainStation, String country,
                            String timeZone, String timeZoneGroup) {
        this.isCity = isCity;
        this.isMainStation = isMainStation;
        this.country = normalizeCountry(country);
        this.timeZone = normalizeTimeZone(timeZone);
        this.timeZoneGroup = normalizeTimeZone(timeZoneGroup);
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

    private String normalizeTimeZone(String tz) {
        if (tz == null || tz.isBlank()) {
            return null;
        }
        return tz.trim();
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
     * Returns the time zone filter value.
     * 
     * @return the time zone to filter by (case-insensitive), or null if no filter
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * Returns the time zone group filter value.
     * 
     * @return the time zone group to filter by (case-insensitive), or null if no filter
     */
    public String getTimeZoneGroup() {
        return timeZoneGroup;
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

        // Check time zone filters
        if (timeZone != null) {
            String stationTimeZone = station.getTimeZone();
            if (stationTimeZone == null || !timeZone.equalsIgnoreCase(stationTimeZone)) {
                return false;
            }
        }

        if (timeZoneGroup != null) {
            String stationTimeZoneGroup = station.getTimeZoneGroup();
            if (stationTimeZoneGroup == null || !timeZoneGroup.equalsIgnoreCase(stationTimeZoneGroup)) {
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
                ", timeZone='" + timeZone + '\'' +
                ", timeZoneGroup='" + timeZoneGroup + '\'' +
                '}';
    }
}
