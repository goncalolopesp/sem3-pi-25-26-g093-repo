package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Train {

    private final int id;
    private final String number;                 // identificador do train, se precisares
    private final List<Locomotive> locomotives;  // 1 ou 2
    private final List<Freight> freights;        // 0..N (depois do dispatcher)
    private final LocalDate startDate;
    private final LocalTime startTime;

    public Train(int id,
                 String number,
                 List<Locomotive> locomotives,
                 List<Freight> freights,
                 LocalDate startDate,
                 LocalTime startTime) {

        this.id = id;
        this.number = number;
        this.locomotives = List.copyOf(locomotives);
        this.freights = List.copyOf(freights);
        this.startDate = startDate;
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public List<Locomotive> getLocomotives() {
        return locomotives;
    }

    public List<Freight> getFreights() {
        return freights;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public double getTotalLengthMeters() {
        double length = 0;

        for (Locomotive loco : locomotives)
            length += loco.getLengthMeters();

        for (Freight freight : freights)
            for (WagonReal w : freight.getWagons())
                length += w.getLengthMeters();

        return length;
    }

    /**
     * Velocidade máxima do train.
     * Apenas a mais lenta das locomotivas conta.
     */
    public double getMaxSpeedMetersPerHour() {
        double min = Double.MAX_VALUE;

        for (Locomotive loco : locomotives) {
            double v = loco.getMaxSpeedKmh() * 1000 / 1;  // km/h → m/h
            if (v < min) min = v;
        }

        return min;
    }

    /**
     * Peso total do comboio (tare + carga).
     */
    public double getTotalWeightTon() {
        double w = 0;

        // locomotivas
        for (Locomotive loco : locomotives)
            w += loco.getModel().getTareWeightTon();

        // wagons + cargas
        for (Freight f : freights) {
            for (WagonReal wgn : f.getWagons()) {
                w += wgn.getTareWeightTon();
                // Se quisesse somar carga, adicionava aqui
            }
        }

        return w;
    }
}
