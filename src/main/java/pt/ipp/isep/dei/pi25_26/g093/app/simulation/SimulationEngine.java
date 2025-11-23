package pt.ipp.isep.dei.pi25_26.g093.app.simulation;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;

import java.time.LocalDateTime;
import java.time.Duration;

public class SimulationEngine {

    private final TrackOccupancyManager trackManager = new TrackOccupancyManager();

    public SimulationTimeline simulate(SimulationState state) {

        SimulationTimeline timeline = new SimulationTimeline();

        while (!state.finished()) {

            RailwaySegment seg = state.getCurrentSegment();
            LocalDateTime now = state.getCurrentTime();

            // 1. SE O TRACK ESTIVER OCUPADO → ESPERA ATÉ ESTAR LIVRE (FIFO)
            if (!trackManager.isFree(seg, now)) {

                LocalDateTime freeAt = trackManager.getFreeTime(seg, now);
                if (freeAt == null || !freeAt.isAfter(now)) {
                    // fallback defensivo – mas em condições normais não cai aqui
                    freeAt = now.plusSeconds(1);
                }

                long waitSeconds = Duration.between(now, freeAt).getSeconds();

                timeline.addEvent(new SimulationEvent(
                        now,
                        SimulationEvent.Type.WAITING,
                        seg,
                        "TRACK OCCUPIED — TRAIN WAITING " + waitSeconds + " s BEFORE ENTERING SEGMENT " + seg.getId()
                ));

                // avança o relógio diretamente para o momento em que o track fica livre
                state.setCurrentTime(freeAt);
                continue;
            }

            // 2. ENTER SEGMENT
            timeline.addEvent(new SimulationEvent(
                    now,
                    SimulationEvent.Type.ENTER_SEGMENT,
                    seg,
                    "ENTERED SEGMENT " + seg.getId()
            ));

            // 3. VELOCIDADE REALISTA (locos + via + potência vs peso)
            double allowedKmh = computeAllowedSpeedKmh(state.getTrain(), seg);
            double allowedMps = allowedKmh * (1000.0 / 3600.0); // km/h → m/s
            if (allowedMps < 0.1) allowedMps = 0.1;

            double travelSeconds = seg.getLengthMeters() / allowedMps;

            LocalDateTime leaveTime = now.plusSeconds((long) travelSeconds);

            // marca o segmento como ocupado até leaveTime
            trackManager.occupy(seg, leaveTime);

            // 4. EXIT SEGMENT
            timeline.addEvent(new SimulationEvent(
                    leaveTime,
                    SimulationEvent.Type.EXIT_SEGMENT,
                    seg,
                    "EXITED SEGMENT " + seg.getId()
            ));

            state.moveToNextSegment();
            state.setCurrentTime(leaveTime);
        }

        return timeline;
    }

    public double computeAllowedSpeedKmh(Train train, RailwaySegment segment) {

        // 1) Limite das locomotivas
        double locoLimit = train.getLocomotives().stream()
                .mapToDouble(Locomotive::getMaxSpeedKmh)
                .min()
                .orElse(40);

        // 2) Limite da via (se não existir, assume 120)
        double trackLimit = 120;
        try {
            trackLimit = segment.getSpeedLimitKmh();
        } catch (Exception ignored) {}

        // 3) Peso total (usa freight max weight)
        double totalWeightTon = train.getFreights().stream()
                .mapToDouble(Freight::getTotalMaxWeightTon)
                .sum();

        // 4) Potência total
        double totalPowerKw = train.getLocomotives().stream()
                .mapToDouble(Locomotive::getPowerKw)
                .sum();

        // Evitar divisões por zero
        if (totalWeightTon < 1) totalWeightTon = 1;

        // 5) Velocidade limitada pela potência (modelo semi-realista)
        double powerLimit = (totalPowerKw / totalWeightTon) * 10.0;

        // 6) Resultado final
        return Math.max(5, Math.min(locoLimit, Math.min(trackLimit, powerLimit)));
    }
}
