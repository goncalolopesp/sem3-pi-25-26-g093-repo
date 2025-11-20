package pt.ipp.isep.dei.pi25_26.g093.app.domain;

/**
 * Class that represents a key for Time Zone Group (TZG) based on CompleteStation attributes.
 */
public class TzgKey implements Comparable<TzgKey> {

    private final CompleteStation station;

    /**
     * Constructor for TzgKey.
     * @param station the CompleteStation used to create the key.
     */
    public TzgKey(CompleteStation station) {
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
     * Gets the Time Zone Group of the CompleteStation.
     * @return the Time Zone Group.
     */
    public String getTimeZoneGroup() {
        return station.getTimeZoneGroup();
    }

    /**
     * Gets the country of the CompleteStation.
     * @return the country.
     */
    public String getCountry() {
        return station.getCountry();
    }

    /**
     * Compares this TzgKey with another TzgKey based on Time Zone Group, Country, and Station.
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(TzgKey o) {
        int cmp = this.station.getTimeZoneGroup()
                .compareTo(o.station.getTimeZoneGroup());
        if (cmp != 0) return cmp;

        cmp = this.station.getCountry()
                .compareTo(o.station.getCountry());
        if (cmp != 0) return cmp;

        return this.station.getStation()
                .compareTo(o.station.getStation());
    }
}
