package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.Objects;

/**
 * CompleteStation class represents a station with complete details including country, time zone, coordinates, and type indicators.
 */
public class CompleteStation {

    private final String country;
    private final String timeZone;
    private final String timeZoneGroup;
    private final String station;
    private Double latitude;
    private Double longitude;
    private final boolean isCity;
    private final boolean isMainStation;
    private final boolean isAirport;

    /**
     * Constructor for CompleteStation
     * @param country the Country where the station is located
     * @param timeZone the TimeZone of the station
     * @param timeZoneGroup the TimeZoneGroup of the station
     * @param station the name of the station
     * @param latitude the latitude of the station
     * @param longitude the longitude of the station
     * @param isCity boolean indicating if the station is in a city
     * @param isMainStation boolean indicating if the station is the main station
     * @param isAirport boolean indicating if the station is an airport
     */
    public CompleteStation(String country, String timeZone, String timeZoneGroup, String station, Double latitude, Double longitude, boolean isCity, boolean isMainStation, boolean isAirport) {

        this.country = country;
        this.timeZone = timeZone;
        this.timeZoneGroup = timeZoneGroup;
        this.station = station;

        this.latitude = latitude;
        this.longitude = longitude;

        this.isCity = isCity;
        this.isMainStation = isMainStation;
        this.isAirport = isAirport;
    }

    /**
     * Gets the country of the station.
     * @return the country
     */
    public String getCountry() { return country; }

    /**
     * Gets the time zone of the station.
     * @return the time zone
     */
    public String getTimeZone() { return timeZone; }

    /**
     * Gets the time zone group of the station.
     * @return the time zone group
     */
    public String getTimeZoneGroup() { return timeZoneGroup; }

    /**
     * Gets the name of the station.
     * @return the station name
     */
    public String getStation() { return station; }

    /**
     * Gets the latitude of the station.
     * @return the latitude
     */
    public Double getLatitude() { return latitude; }

    /**
     * Gets the longitude of the station.
     * @return the longitude
     */
    public Double getLongitude() { return longitude; }

    /**
     * Checks if the station is in a city.
     * @return true if it is a city, false otherwise
     */
    public boolean isCity() { return isCity; }

    /**
     * Checks if the station is the main station.
     * @return true if it is the main station, false otherwise
     */
    public boolean isMainStation() { return isMainStation; }

    /**
     * Checks if the station is an airport.
     * @return true if it is an airport, false otherwise
     */
    public boolean isAirport() { return isAirport; }

    /**
     * String representation of the CompleteStation.
     * @return string representation
     */
    @Override
    public String toString() {
        return String.format(
                "%s [%s] - %s (lat=%.4f, lon=%.4f, city=%s, main=%s, airport=%s)",
                station,
                timeZoneGroup,
                country,
                latitude,
                longitude,
                isCity,
                isMainStation,
                isAirport
        );
    }

    /**
     * Equality check for CompleteStation.
     * @param o the object to compare with
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompleteStation)) return false;
        CompleteStation other = (CompleteStation) o;

        return Double.compare(other.latitude, latitude) == 0 &&
                Double.compare(other.longitude, longitude) == 0 &&
                Objects.equals(country, other.country) &&
                Objects.equals(station, other.station);
    }
}
