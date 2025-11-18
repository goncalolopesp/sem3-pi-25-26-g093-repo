package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class TzgKey implements Comparable<TzgKey> {

    private final CompleteStation station;

    public TzgKey(CompleteStation station) {
        this.station = station;
    }

    public CompleteStation getStation() {
        return station;
    }

    public String getTimeZoneGroup() {
        return station.getTimeZoneGroup();
    }

    public String getCountry() {
        return station.getCountry();
    }

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
