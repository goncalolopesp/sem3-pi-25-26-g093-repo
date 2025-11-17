package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.Objects;


public class LineSegment {
    private final String id;
    private final Station origin;
    private final Station destination;
    private final double lengthKm;
    private final double speedLimitKmH;  // segment limit (used with min(loco, limit))
    private final double gaugeMm;        // track gauge
    private final boolean electrified;

    public LineSegment(String id, Station origin, Station destination,
                       double lengthKm, double speedLimitKmH, double gaugeMm, boolean electrified) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Line id is required");
        if (origin == null || destination == null) throw new IllegalArgumentException("Stations are required");
        if (origin.equals(destination)) throw new IllegalArgumentException("Origin and destination must differ");
        if (lengthKm <= 0) throw new IllegalArgumentException("Length must be positive");
        if (speedLimitKmH <= 0) throw new IllegalArgumentException("Speed limit must be positive");
        if (gaugeMm <= 0) throw new IllegalArgumentException("Gauge must be positive");
        this.id = id.trim();
        this.origin = origin;
        this.destination = destination;
        this.lengthKm = lengthKm;
        this.speedLimitKmH = speedLimitKmH;
        this.gaugeMm = gaugeMm;
        this.electrified = electrified;
    }

    public String getId() { return id; }
    public Station getOrigin() { return origin; }
    public Station getDestination() { return destination; }
    public double getLengthKm() { return lengthKm; }
    public double getSpeedLimitKmH() { return speedLimitKmH; }
    public double getGaugeMm() { return gaugeMm; }
    public boolean isElectrified() { return electrified; }

    public boolean connects(Station a, Station b) {
        return (origin.equals(a) && destination.equals(b)) || (origin.equals(b) && destination.equals(a));
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LineSegment)) return false;
        LineSegment that = (LineSegment) o;
        return id.equalsIgnoreCase(that.id);
    }
    @Override public int hashCode() { return Objects.hash(id.toLowerCase()); }

    @Override public String toString() {
        return String.format("%s: %s -> %s (%.1f km, limit %.0f km/h, %.0f mm, %s)",
                id, origin.getName(), destination.getName(), lengthKm, speedLimitKmH, gaugeMm,
                electrified ? "electrified" : "non-electrified");
    }
}
