package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import pt.ipp.isep.dei.pi25_26.g093.app.simulation.SimulationTimeline;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TrainSchedule {

    public enum Status {
        SCHEDULED,
        RUNNING,
        FINISHED,
        CANCELLED
    }

    private final Train train;
    private final List<Freight> freights;
    private final List<RailwaySegment> route;
    private final LocalDateTime departureTime;

    private Status status = Status.SCHEDULED;
    private SimulationTimeline resultTimeline;

    public TrainSchedule(Train train,
                         List<Freight> freights,
                         List<RailwaySegment> route,
                         LocalDateTime departureTime) {

        this.train = train;
        this.freights = List.copyOf(freights);
        this.route = List.copyOf(route);
        this.departureTime = departureTime;
    }

    public Train getTrain() {
        return train;
    }

    public List<Freight> getFreights() {
        return freights;
    }

    public List<RailwaySegment> getRoute() {
        return route;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public SimulationTimeline getResultTimeline() {
        return resultTimeline;
    }

    public void setResultTimeline(SimulationTimeline resultTimeline) {
        this.resultTimeline = resultTimeline;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return String.format(
                "Train %s | Departure: %s | Status: %s",
                train.getNumber(),
                departureTime.format(fmt),
                status
        );
    }
}
