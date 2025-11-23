package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.database.*;
import pt.ipp.isep.dei.pi25_26.g093.app.service.RouteService;
import pt.ipp.isep.dei.pi25_26.g093.app.simulation.SimulationEngine;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * UI for USPL03 – Travel time estimation between two stations.
 */
public class TravelTimeUI {

    private final Scanner scanner = new Scanner(System.in);

    private final LocomotiveRepository locomotiveRepo = new LocomotiveRepository();
    private final RailwayNetworkRepository networkRepo = new RailwayNetworkRepository();

    private final RouteService routeService = new RouteService(networkRepo);
    private final SimulationEngine simulationEngine = new SimulationEngine();

    /**
     * Entry point for the UI.
     */
    public void showMenu() {

        System.out.println("\n=== USPL03 – Travel Time Estimator ===");

        Locomotive loco = chooseLocomotive();
        if (loco == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        Facility origin = chooseFacility("origin");
        if (origin == null) return;

        Facility destination = chooseFacility("destination");
        if (destination == null) return;

        if (origin.getId() == destination.getId()) {
            System.out.println("Origin and destination must be different.");
            return;
        }

        computeTravelTime(loco, origin, destination);
    }

    private Locomotive chooseLocomotive() {

        List<Locomotive> locos = new ArrayList<>(locomotiveRepo.getAll());

        if (locos.isEmpty()) {
            System.out.println("No locomotives found.");
            return null;
        }

        System.out.println("\nAvailable locomotives:");
        for (int i = 0; i < locos.size(); i++) {
            Locomotive l = locos.get(i);
            System.out.printf("%d) %s (%s – %.0f km/h, %.0f kW)%n",
                    i + 1,
                    l.getIdentifier(),
                    l.getModelName(),
                    l.getMaxSpeedKmh(),
                    l.getPowerKw());
        }
        System.out.println("0) Cancel");

        int opt = readInt("Choose locomotive: ");
        if (opt == 0) return null;
        if (opt < 1 || opt > locos.size()) return null;

        return locos.get(opt - 1);
    }


    private Facility chooseFacility(String description) {

        List<Facility> facs = new ArrayList<>(networkRepo.getAllFacilities());

        System.out.println("\nChoose " + description + " station:");
        for (int i = 0; i < facs.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, facs.get(i).getName());
        }
        System.out.println("0) Cancel");

        int opt = readInt("Pick: ");
        if (opt == 0) return null;
        if (opt < 1 || opt > facs.size()) return null;

        return facs.get(opt - 1);
    }


    private void computeTravelTime(Locomotive loco, Facility origin, Facility destination) {

        System.out.println("\n=== Computing travel time... ===");

        List<RailwaySegment> route;
        try {
            route = routeService.buildRouteBetweenFacilities(origin.getId(), destination.getId());
        } catch (Exception e) {
            System.out.println("No direct route found: " + e.getMessage());
            return;
        }

        double totalMeters = route.stream()
                .mapToDouble(RailwaySegment::getLengthMeters)
                .sum();

        Train fakeTrain = new Train(
                0,
                "TEMP",
                List.of(loco),
                List.of(),
                LocalDate.now(),
                LocalTime.now()
        );

        double allowed = loco.getMaxSpeedKmh();
        for (RailwaySegment seg : route) {
            allowed = Math.min(allowed,
                    simulationEngine.computeAllowedSpeedKmh(fakeTrain, seg));
        }

        double hours = (totalMeters / 1000.0) / allowed;
        long seconds = (long) (hours * 3600);

        // 5) Output results
        System.out.println("\n=== TRAVEL SUMMARY ===");
        System.out.println("From: " + origin.getName());
        System.out.println("To:   " + destination.getName());
        System.out.printf("Total distance: %.2f km%n", totalMeters / 1000.0);
        System.out.printf("Speed used: %.1f km/h%n", allowed);
        System.out.printf("Estimated travel time: %02dm %02ds%n",
                seconds / 60, seconds % 60);

        System.out.println("\nSegments:");
        for (RailwaySegment seg : route) {
            RailwayLine line = seg.getRailwayLine();
            System.out.printf("- %s | SEG %d | %.0f m%n",
                    line.getName(), seg.getId(), seg.getLengthMeters());
        }

        System.out.println();
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.nextLine();
        }
        int v = scanner.nextInt();
        scanner.nextLine();
        return v;
    }
}
