package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.Objects;

public class Facility {

    private final int id;
    private final String name;
    private final FacilityType type;

    public Facility(int id, String name, FacilityType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FacilityType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Facility facility = (Facility) o;
        return id == facility.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name + " [" + type + "]";
    }
}