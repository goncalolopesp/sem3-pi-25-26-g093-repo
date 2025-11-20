package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.Objects;

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


    public String getCountry() { return country; }
    public String getTimeZone() { return timeZone; }
    public String getTimeZoneGroup() { return timeZoneGroup; }
    public String getStation() { return station; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public boolean isCity() { return isCity; }
    public boolean isMainStation() { return isMainStation; }
    public boolean isAirport() { return isAirport; }

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
