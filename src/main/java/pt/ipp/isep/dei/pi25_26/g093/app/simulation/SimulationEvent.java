package pt.ipp.isep.dei.pi25_26.g093.app.simulation;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.RailwaySegment;

import java.time.LocalDateTime;

public class SimulationEvent {

    public enum Type {
        ENTER_SEGMENT,
        EXIT_SEGMENT,
        WAITING
    }

    private final LocalDateTime time;
    private final Type type;
    private final RailwaySegment segment;
    private final String message;

    public SimulationEvent(LocalDateTime time, Type type,
                           RailwaySegment segment, String message) {
        this.time = time;
        this.type = type;
        this.segment = segment;
        this.message = message;
    }

    public LocalDateTime getTime() { return time; }
    public Type getType() { return type; }
    public RailwaySegment getSegment() { return segment; }
    public String getMessage() { return message; }

    @Override
    public String toString() {
        return time + " | " + type + " | SEG " + segment.getId() + " | " + message;
    }
}
