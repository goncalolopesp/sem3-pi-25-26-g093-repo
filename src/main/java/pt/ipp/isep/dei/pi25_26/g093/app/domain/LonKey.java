package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class LonKey implements Comparable<LonKey> {

    private final CompleteStation station;

    public LonKey(CompleteStation station) {
        this.station = station;
    }

    public CompleteStation getStation() {
        return station;
    }

    public Double getLongitude() {
        return station.getLongitude();
    }

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
