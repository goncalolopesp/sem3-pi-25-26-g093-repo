package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents the optional filtering criteria used in USEI08
 * (Search by Geographical Area).
 *
 * Each field may be:
 *  - a Boolean value (true/false) to apply the filter, or
 *  - null, meaning "no filter" for that field.
 *
 * The "country" filter may be:
 *  - "PT"
 *  - "ES"
 *  - "ALL" (or null → treated as no filter)
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

    // Getters
    public Boolean getIsCity() {
        return isCity;
    }

    public Boolean getIsMainStation() {
        return isMainStation;
    }

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
     *
     * @param station station to evaluate
     * @return true if the station satisfies the filter (or if no filters are set)
     */
    public boolean matches(CompleteStation station) {
        if (station == null) {
            return false;
        }

        if (isCity != null && station.isCity() != isCity) {
            return false;
        }

        if (isMainStation != null && station.isMainStation() != isMainStation) {
            return false;
        }

        if (!"ALL".equals(country) && !country.equalsIgnoreCase(station.getCountry())) {
            return false;
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
