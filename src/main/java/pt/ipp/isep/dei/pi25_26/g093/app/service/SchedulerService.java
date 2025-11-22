package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.simulation.*;

import java.time.LocalDateTime;
import java.util.*;

public class SchedulerService {

    private final TrainBuilderService trainBuilderService;
    private final SimulationEngine simulationEngine;
    private int nextTrainId = 1;


    private final List<TrainSchedule> schedules = new ArrayList<>();

    public SchedulerService(TrainBuilderService trainBuilderService,
                            SimulationEngine simulationEngine) {
        this.trainBuilderService = trainBuilderService;
        this.simulationEngine = simulationEngine;
    }

    public TrainSchedule scheduleTrain(List<Locomotive> locomotives,
                                       List<Freight> freights,
                                       List<RailwaySegment> route,
                                       LocalDateTime departureTime) {

        int id = nextTrainId();
        String number = generateTrainNumber(id);

        Train train = trainBuilderService.buildTrain(
                id,
                number,
                locomotives,
                freights,
                departureTime.toLocalDate(),
                departureTime.toLocalTime()
        );


        // marcar locos como IN USE
        for (Locomotive loco : locomotives) {
            loco.setAvailable(false);
        }

        TrainSchedule schedule = new TrainSchedule(
                train,
                freights,
                route,
                departureTime
        );

        schedules.add(schedule);
        schedules.sort(Comparator.comparing(TrainSchedule::getDepartureTime));

        return schedule;
    }

    // overload de conveniência (se algum dia quiseres usar só 1 loco)
    public TrainSchedule scheduleTrain(Locomotive locomotive,
                                       List<Freight> freights,
                                       List<RailwaySegment> route,
                                       LocalDateTime departureTime) {
        return scheduleTrain(List.of(locomotive), freights, route, departureTime);
    }


    // =========================
    // EXECUTA UM SCHEDULE (usar internamente e na UI se quiseres)
    // =========================
    public SimulationTimeline runSchedule(TrainSchedule schedule) {
        if (schedule.getStatus() == TrainSchedule.Status.CANCELLED
                || schedule.getStatus() == TrainSchedule.Status.FINISHED) {
            throw new IllegalStateException("Cannot run this schedule.");
        }

        schedule.setStatus(TrainSchedule.Status.RUNNING);

        SimulationState initialState = new SimulationState(
                schedule.getTrain(),
                schedule.getRoute(),
                schedule.getDepartureTime()
        );

        SimulationTimeline timeline = simulationEngine.simulate(initialState);

        schedule.setResultTimeline(timeline);
        schedule.setStatus(TrainSchedule.Status.FINISHED);

        // libertar locomotivas (ficam novamente AVAILABLE)
        for (Locomotive loco : schedule.getTrain().getLocomotives()) {
            loco.setAvailable(true);
        }

        return timeline;
    }


    // =========================
    // SCHEDULER CLOCK
    // =========================
    public void processScheduler(LocalDateTime now) {

        for (TrainSchedule schedule : schedules) {

            if (schedule.getStatus() != TrainSchedule.Status.SCHEDULED)
                continue;

            // Se now >= departureTime → dispara
            if (!now.isBefore(schedule.getDepartureTime())) {
                runSchedule(schedule);
            }
        }
    }

    public List<TrainSchedule> listSchedules() {
        return Collections.unmodifiableList(schedules);
    }
    private int nextTrainId() {
        return nextTrainId++;
    }

    private String generateTrainNumber(int id) {
        return "T" + id;
    }
}
