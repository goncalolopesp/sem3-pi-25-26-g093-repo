package pt.ipp.isep.dei.pi25_26.g093.app.domain;

/**
 * Key class to compare CompleteStation objects by latitude.
 */
public class LatKey implements Comparable<LatKey> {

    private final CompleteStation station;

    /**
     * Constructor.
     * @param station the CompleteStation to be wrapped by this LatKey.
     */
    public LatKey(CompleteStation station) {
        this.station = station;
    }

    /**
     * Gets the CompleteStation wrapped by this LatKey.
     * @return the CompleteStation.
     */
    public CompleteStation getStation() {
        return station;
    }

    /**
     * Gets the latitude of the CompleteStation.
     * @return the latitude.
     */
    public Double getLatitude() {
        return station.getLatitude();
    }

    /**
     * Compares this LatKey to another LatKey.
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(LatKey o) {
        Double lat1 = this.station.getLatitude();
        Double lat2 = o.station.getLatitude();

        if (lat1 == null && lat2 == null)
            return this.station.getStation().compareTo(o.station.getStation());
        if (lat1 == null) return 1;
        if (lat2 == null) return -1;

        int cmp = lat1.compareTo(lat2);
        if (cmp != 0) return cmp;

        cmp = this.station.getStation().compareTo(o.station.getStation());
        if (cmp != 0) return cmp;

        return this.station.getCountry().compareTo(o.station.getCountry());
    }
}
