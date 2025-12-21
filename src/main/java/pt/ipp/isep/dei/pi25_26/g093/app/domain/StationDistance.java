package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.Locale;

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
        // 1. Comparar Distância (Crescente / Menor para Maior)
        int distComparison = Double.compare(this.distanceKm, other.distanceKm);

        if (distComparison != 0) {
            return distComparison;
        }

        // 2. Comparar Nome (Decrescente / Z -> A) [
        // Para ordenar Z->A, comparamos o 'other' com o 'this'
        String myName = (this.station != null && this.station.getStation() != null) ? this.station.getStation() : "";
        String otherName = (other.station != null && other.station.getStation() != null) ? other.station.getStation() : "";

        return otherName.compareTo(myName);
    }

    @Override
    public String toString() {
        // Formata a string para passar no teste
        String name = (station != null && station.getStation() != null) ? station.getStation() : "Unknown";
        return String.format(Locale.US, "%.2f km - %s", distanceKm, name);
    }
}