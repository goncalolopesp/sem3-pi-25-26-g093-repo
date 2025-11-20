package pt.ipp.isep.dei.pi25_26.g093.app.domain;

/**
 * Key class for sorting CompleteStation objects by longitude.
 */
public class LonKey implements Comparable<LonKey> {

    private final CompleteStation station;

    /**
     * Creates a LonKey for the given CompleteStation.
     * @param station the CompleteStation to create the key for.
     */
    public LonKey(CompleteStation station) {
        this.station = station;
    }

    /**
     * Gets the CompleteStation associated with this key.
     * @return the CompleteStation.
     */
    public CompleteStation getStation() {
        return station;
    }

    /**
     * Gets the longitude of the CompleteStation.
     * @return the longitude.
     */
    public Double getLongitude() {
        return station.getLongitude();
    }

    /**
     * Compares this LonKey with another based on longitude, station name, and country.
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(LonKey o) {
        Double lon1 = this.station.getLongitude();
        Double lon2 = o.station.getLongitude();

        if (lon1 == null && lon2 == null)
            return this.station.getStation().compareTo(o.station.getStation());
        if (lon1 == null) return 1;
        if (lon2 == null) return -1;

        int cmp = lon1.compareTo(lon2);
        if (cmp != 0) return cmp;

        cmp = this.station.getStation().compareTo(o.station.getStation());
        if (cmp != 0) return cmp;

        return this.station.getCountry().compareTo(o.station.getCountry());
    }
}
