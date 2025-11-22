package pt.ipp.isep.dei.pi25_26.g093.app.simulation;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;

import java.time.LocalDateTime;
import java.util.List;

public class SimulationState {

    private final Train train;
    private final List<RailwaySegment> route;
    private final LocalDateTime startTime;

    private int currentSegmentIndex = 0;
    private LocalDateTime currentTime;

    public SimulationState(Train train,
                           List<RailwaySegment> route,
                           LocalDateTime startTime) {

        this.train = train;
        this.route = route;
        this.startTime = startTime;
        this.currentTime = startTime;
    }

    public Train getTrain() { return train; }
    public List<RailwaySegment> getRoute() { return route; }
    public LocalDateTime getCurrentTime() { return currentTime; }
    public int getCurrentSegmentIndex() { return currentSegmentIndex; }

    public RailwaySegment getCurrentSegment() {
        if (currentSegmentIndex >= route.size()) return null;
        return route.get(currentSegmentIndex);
    }

    public void advanceTime(long minutes) {
        currentTime = currentTime.plusMinutes(minutes);
    }

    public void moveToNextSegment() {
        currentSegmentIndex++;
    }

    public boolean finished() {
        return currentSegmentIndex >= route.size();
    }

    public void setCurrentTime(LocalDateTime t) {
        this.currentTime = t;
    }

}
