package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Facility;
import java.util.ArrayList;
import java.util.List;

public class FacilityRepository {

    private final List<Facility> facilities;

    public FacilityRepository() {
        this.facilities = new ArrayList<>();
    }

    public void add(Facility f) {
        facilities.add(f);
    }

    public Facility getById(int id) {
        for (Facility f : facilities) {
            if (f.getId() == id) return f;
        }
        return null;
    }

    public List<Facility> getAll() {
        return facilities;
    }
}
