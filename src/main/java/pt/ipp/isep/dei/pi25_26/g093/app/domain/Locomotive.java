package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.Objects;

/**
 * USPL03 - Locomotive (minimal model).
 */
public class Locomotive {
    private final String id;
    private final String type;           // e.g., "diesel" or "electric"
    private final double maxSpeedKmH;    // top speed
    private final double gaugeMm;        // track gauge (mm)

    public Locomotive(String id, String type, double maxSpeedKmH, double gaugeMm) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Locomotive id is required");
        if (type == null || type.isBlank()) throw new IllegalArgumentException("Locomotive type is required");
        if (maxSpeedKmH <= 0) throw new IllegalArgumentException("Max speed must be positive");
        if (gaugeMm <= 0) throw new IllegalArgumentException("Gauge must be positive");
        this.id = id.trim();
        this.type = type.trim().toLowerCase();
        this.maxSpeedKmH = maxSpeedKmH;
        this.gaugeMm = gaugeMm;
    }

    public String getId() { return id; }
    public String getType() { return type; }
    public double getMaxSpeedKmH() { return maxSpeedKmH; }
    public double getGaugeMm() { return gaugeMm; }
    public boolean isElectric() { return "electric".equals(type); }
    public boolean isDiesel() { return "diesel".equals(type); }
    public boolean isGaugeCompatible(double otherGaugeMm) { return Math.abs(gaugeMm - otherGaugeMm) <= 1.0; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Locomotive)) return false;
        Locomotive that = (Locomotive) o;
        return id.equalsIgnoreCase(that.id);
    }
    @Override public int hashCode() { return Objects.hash(id.toLowerCase()); }

    @Override public String toString() {
        return String.format("%s (%s, %.0f km/h, %.0f mm)", id, type, maxSpeedKmH, gaugeMm);
    }
}
