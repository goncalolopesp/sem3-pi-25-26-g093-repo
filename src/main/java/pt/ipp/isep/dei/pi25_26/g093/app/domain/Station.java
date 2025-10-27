package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.Objects;

/**
 * USPL03 - Railway Station (minimal model for travel-time calculation).
 */
public class Station {
    private final String id;
    private final String name;
    private final String location;

    public Station(String id, String name, String location) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Station id is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Station name is required");
        this.id = id.trim();
        this.name = name.trim();
        this.location = location == null ? "" : location.trim();
    }

    public Station(String id, String name) { this(id, name, null); }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public boolean hasLocation() { return !location.isEmpty(); }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Station)) return false;
        Station station = (Station) o;
        return id.equalsIgnoreCase(station.id);
    }
    @Override public int hashCode() { return Objects.hash(id.toLowerCase()); }

    @Override public String toString() {
        return hasLocation() ? String.format("%s (%s) - %s", name, id, location)
                : String.format("%s (%s)", name, id);
    }
}
