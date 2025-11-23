package pt.ipp.isep.dei.pi25_26.g093.app.domain;


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
        // 1º ordenar pela distância (ASC)
        int cmp = Double.compare(this.distanceKm, other.distanceKm);
        if (cmp != 0) return cmp;

        // 2º, se distâncias iguais, ordenar pelo nome (DESC)
        return other.station.getStation().compareTo(this.station.getStation());
    }

    @Override
    public String toString() {
        return String.format("%.2f km - %s", distanceKm, station.toString());
    }
}
