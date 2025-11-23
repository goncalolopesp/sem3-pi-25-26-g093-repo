package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.Locale;

/**
 * Represents a station paired with its computed distance (in kilometers)
 * relative to a reference point.
 *
 * <p>This class implements {@link Comparable} so results can be sorted:</p>
 * <ul>
 *     <li>1) By distance ascending</li>
 *     <li>2) If distance is equal, by station name alphabetically (ASC)</li>
 * </ul>
 */
public class StationDistance implements Comparable<StationDistance> {

    private final CompleteStation station;
    private final double distanceKm;

    public StationDistance(CompleteStation station, double distanceKm) {
        this.station = station;
        this.distanceKm = distanceKm;
    }

    public CompleteStation getStation() {
        return station;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    @Override
    public int compareTo(StationDistance other) {
        int cmp = Double.compare(this.distanceKm, other.distanceKm);
        if (cmp != 0) return cmp;

        return this.station.getStation().compareTo(other.station.getStation());
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%.2f km - %s", distanceKm, station.toString());
    }
}
