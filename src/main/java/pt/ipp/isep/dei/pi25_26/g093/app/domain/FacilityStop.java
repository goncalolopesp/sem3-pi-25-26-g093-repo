package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Facility;

import java.time.LocalDateTime;

public class FacilityStop {
    private Facility facility;
    private LocalDateTime arrival;
    private LocalDateTime departure;
    private boolean unloading;
    private int unloadingFreights;

    public FacilityStop(Facility facility,
                        LocalDateTime arrival,
                        LocalDateTime departure,
                        boolean unloading,
                        int unloadingFreights) {
        this.facility = facility;
        this.arrival = arrival;
        this.departure = departure;
        this.unloading = unloading;
        this.unloadingFreights = unloadingFreights;
    }

    public Facility getFacility() {
        return facility;
    }

    public LocalDateTime getArrival() {
        return arrival;
    }

    public LocalDateTime getDeparture() {
        return departure;
    }

    public boolean isUnloading() {
        return unloading;
    }

    public int getUnloadingFreights() {
        return unloadingFreights;
    }

    public void setUnloading(boolean b) {
        this.unloading = b;
    }

    public void setUnloadingFreights(int i) {
        this.unloadingFreights = i;
    }

    public void setDeparture(LocalDateTime newDeparture) {
        this.departure = newDeparture;
    }
}
