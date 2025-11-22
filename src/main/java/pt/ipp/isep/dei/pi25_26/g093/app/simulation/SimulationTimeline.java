package pt.ipp.isep.dei.pi25_26.g093.app.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimulationTimeline {

    private final List<SimulationEvent> events = new ArrayList<>();

    public void addEvent(SimulationEvent event) {
        events.add(event);
    }

    public List<SimulationEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public void sort() {
        events.sort((a, b) -> a.getTime().compareTo(b.getTime()));
    }

}
