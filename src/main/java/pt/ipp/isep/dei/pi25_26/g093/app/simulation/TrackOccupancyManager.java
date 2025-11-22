package pt.ipp.isep.dei.pi25_26.g093.app.simulation;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.RailwaySegment;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class TrackOccupancyManager {

    // guarda, por segmento, até quando está ocupado
    private final Map<Integer, LocalDateTime> occupiedUntil = new HashMap<>();

    public boolean isFree(RailwaySegment seg, LocalDateTime time) {
        LocalDateTime t = occupiedUntil.get(seg.getId());
        // livre se nunca foi marcado ou se o "occupiedUntil" já passou
        return t == null || !t.isAfter(time);
    }

    public void occupy(RailwaySegment seg, LocalDateTime until) {
        occupiedUntil.put(seg.getId(), until);
    }

    /**
     * Momento a partir do qual o segmento está livre.
     * Se já estiver livre em 'now', devolve 'now'.
     */
    public LocalDateTime getFreeTime(RailwaySegment seg, LocalDateTime now) {
        LocalDateTime t = occupiedUntil.get(seg.getId());
        if (t == null || !t.isAfter(now)) return now;
        return t;
    }
}
