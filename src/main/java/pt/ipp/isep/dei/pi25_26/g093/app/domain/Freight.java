package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.time.LocalDate;
import java.util.List;

public class Freight {

    private final int id;
    private final Facility origin;
    private final Facility destination;
    private final LocalDate date;
    private final List<WagonReal> wagons;
    private int currentFacilityId;


    public Freight(int id, Facility origin, Facility destination,
                   LocalDate date, List<WagonReal> wagons) {

        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.wagons = List.copyOf(wagons);
        this.currentFacilityId = origin.getId();
    }

    public int getCurrentFacilityId() {
        return currentFacilityId;
    }

    public void setCurrentFacilityId(int currentFacilityId) {
        this.currentFacilityId = currentFacilityId;
    }


    public int getId() { return id; }
    public Facility getOrigin() { return origin; }
    public Facility getDestination() { return destination; }
    public LocalDate getDate() { return date; }
    public List<WagonReal> getWagons() { return wagons; }


    public double getTotalLengthMeters() {
        return wagons.stream()
                .mapToDouble(WagonReal::getLengthMeters)
                .sum();
    }

    public double getTotalMaxWeightTon() {
        return wagons.stream()
                .mapToDouble(WagonReal::getMaxTotalWeightTon)
                .sum();
    }

    
}
