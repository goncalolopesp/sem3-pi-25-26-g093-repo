package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Freight;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Locomotive;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Train;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TrainBuilderService {

    public Train buildTrain(
            List<Locomotive> locomotives,
            List<Freight> freights,
            LocalDate date,
            LocalTime time
    ) {
        int tempId = -1;
        String tempNumber = "PREVIEW";

        return new Train(tempId, tempNumber, locomotives, freights, date, time);
    }

    public Train buildTrain(
            int id,
            String number,
            List<Locomotive> locomotives,
            List<Freight> freights,
            LocalDate date,
            LocalTime time
    ) {
        return new Train(id, number, locomotives, freights, date, time);
    }

    public double computeTrainLength(Train t) {
        double locoLen = t.getLocomotives()
                .stream()
                .mapToDouble(Locomotive::getLengthMeters)
                .sum();

        double freightLen = t.getFreights()
                .stream()
                .mapToDouble(Freight::getTotalLengthMeters)
                .sum();

        return locoLen + freightLen;
    }

    public double computeTrainWeight(Train t) {
        double freightW = t.getFreights()
                .stream()
                .mapToDouble(Freight::getTotalMaxWeightTon)
                .sum();

        return freightW;
    }

    public double computeAllowedSpeed(Train t) {
        return t.getLocomotives()
                .stream()
                .mapToDouble(Locomotive::getMaxSpeedKmh)
                .min()
                .orElse(0);
    }
}
