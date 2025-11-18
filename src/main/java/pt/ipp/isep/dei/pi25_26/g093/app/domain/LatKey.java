package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class LatKey implements Comparable<LatKey> {

    private final CompleteStation station;

    public LatKey(CompleteStation station) {
        this.station = station;
    }

    public CompleteStation getStation() {
        return station;
    }

    public Double getLatitude() {
        return station.getLatitude();
    }

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
