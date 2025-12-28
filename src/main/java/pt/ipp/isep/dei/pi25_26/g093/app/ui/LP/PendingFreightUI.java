package pt.ipp.isep.dei.pi25_26.g093.app.ui.LP;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Freight;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.RailwaySegment;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.WagonReal;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.database.FacilityRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.database.FreightRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.database.RailwayNetworkRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.service.RouteService;

import java.util.Scanner;

import java.util.*;

public class PendingFreightUI {

    private Scanner scanner = new Scanner(System.in);

    private final FreightRepository freightRepository = new FreightRepository();
    private final FacilityRepository facilityRepository = new FacilityRepository();
    private final RailwayNetworkRepository railwayNetworkRepository = new RailwayNetworkRepository();
    private final RouteService routeService = new RouteService(railwayNetworkRepository);
    private final DispatcherUI dispatch = new DispatcherUI();

    private final Map<Integer, List<RailwaySegment>> plannedRoutes = new HashMap<>();
    private final Map<Integer, List<Integer>> plannedFacilityPaths = new HashMap<>();


    public void showMenu() {

        int option = 0;

        do {

            System.out.println("\n=== USLP08 - Pending Freights Planning ===");
            System.out.println("1) List all pending freights.");
            System.out.println("2) Plan route for pending freight.");
            System.out.println("3) Exit program.");
            System.out.println("Choose an option: ");

            option = scanner.nextInt();

            switch (option) {

                case 1 -> listPendingFreights();
                case 2 -> planPendingFreights();
                case 3 -> System.out.println("Exiting program.");
                default -> System.out.println("Invalid option.");
            }
        } while (option != 3);
    }


    private void listPendingFreights() {

        Collection<Freight> freights = freightRepository.getAll();

        if (freights.isEmpty()) {
            System.out.println("\nNo pending freights.");
            return;
        }

        System.out.println("\n=== PENDING FREIGHTS ===");

        for (Freight f : freights) {

            boolean hasPlan = plannedRoutes.containsKey(f.getId());

            System.out.printf(
                    "Freight %d | %s → %s | %d wagons | %s%n",
                    f.getId(),
                    f.getOrigin().getName(),
                    f.getDestination().getName(),
                    f.getWagons().size(),
                    hasPlan ? "ROUTE PLANNED" : "NO ROUTE ASSIGNED"
            );

            for (WagonReal w : f.getWagons()) {
                System.out.printf(
                        " - %s | Model: %s | Max load: %.1f t | Cargo Type: %s | Max speed: %.0f km/h%n",
                        w.getIdentifier(),
                        w.getModel().getMaker(),
                        w.getMaxLoadTon(),
                        w.getModel().getType().getTypeName(),
                        w.getMaxSpeedKmh()
                );
            }

            List<Integer> path = plannedFacilityPaths.get(f.getId());

            if (path != null) {
                System.out.print("   Planned route: ");

                for (int i = 0; i < path.size(); i++) {
                    if (i > 0) System.out.print(" → ");
                    System.out.print(
                            facilityRepository.findById(path.get(i)).getName()
                    );
                }
                System.out.println();
            }

            System.out.println();
        }
    }


    private void planPendingFreights() {

        Collection<Freight> freights = freightRepository.getAll();

        if (freights.isEmpty()) {
            System.out.println("\nNo pending freights available.");
            return;
        }

        System.out.println("\n=== PLAN PENDING FREIGHT ===");
        System.out.println("\nAvailable pending freights:");

        List<Freight> list = new ArrayList<>(freights);

        for (int i = 0; i < list.size(); i++) {
            Freight f = list.get(i);
            System.out.printf(
                    "%d) Freight %d | %s → %s | %d wagons%n",
                    i + 1,
                    f.getId(),
                    f.getOrigin().getName(),
                    f.getDestination().getName(),
                    f.getWagons().size()
            );
        }

        System.out.println("0) Cancel");


        System.out.println("Choose a freight to plan: ");
        int opt = scanner.nextInt();
        if (opt == 0) return;
        if (opt < 1 || opt > list.size()) {
            System.out.println("Invalid option.");
            return;
        }

        Freight freight = list.get(opt - 1);


        List<Integer> facilityPath = dispatch.askUserForFacilityPath(
                freight.getOrigin().getId(),
                List.of(freight)
        );

        if (facilityPath == null) {
            System.out.println("Route planning cancelled.");
            return;
        }

        List<RailwaySegment> route;
        try {
            route = routeService.buildManualRoute(facilityPath);

            plannedRoutes.put(freight.getId(), route);
            plannedFacilityPaths.put(freight.getId(), facilityPath);

        } catch (Exception e) {
            System.out.println("Error building route: " + e.getMessage());
            return;
        }


        System.out.println("\n=== ROUTE PLAN REVIEW ===");

        System.out.printf(
                "Freight %d | %s → %s%n",
                freight.getId(),
                freight.getOrigin().getName(),
                freight.getDestination().getName()
        );

        System.out.println("\nWagons:");
        for (WagonReal w : freight.getWagons()) {
            System.out.printf(
                    " - %s | Model: %s | Max load: %.1f t | Cargo Type: %s | Max speed: %.0f km/h%n",
                    w.getIdentifier(),
                    w.getModel().getMaker(),
                    w.getMaxLoadTon(),
                    w.getModel().getType().getTypeName(),
                    w.getMaxSpeedKmh()
            );
        }

        double totalDistance = route.stream()
                .mapToDouble(RailwaySegment::getLengthMeters)
                .sum();

        System.out.println("\nPlanned route:");
        for (RailwaySegment seg : route) {
            System.out.printf(
                    " - %s | SEG %d | %.0f m%n",
                    seg.getRailwayLine().getName(),
                    seg.getId(),
                    seg.getLengthMeters()
            );
        }

        System.out.printf(
                "%nTotal planned distance: %.1f km%n",
                totalDistance / 1000.0
        );

        System.out.println("\nRoute planned successfully (not dispatched).");
    }

    private List<String> buildFacilityOrder(
            Freight freight,
            List<RailwaySegment> route
    ) {
        List<String> facilities = new ArrayList<>();

        int currentFacilityId = freight.getOrigin().getId();
        facilities.add(freight.getOrigin().getName());

        for (RailwaySegment seg : route) {

            int a = seg.getRailwayLine().getStart().getId();
            int b = seg.getRailwayLine().getEnd().getId();

            int nextFacilityId;

            if (currentFacilityId == a) {
                nextFacilityId = b;
            } else if (currentFacilityId == b) {
                nextFacilityId = a;
            } else {
                continue;
            }

            facilities.add(
                    facilityRepository.findById(nextFacilityId).getName()
            );

            currentFacilityId = nextFacilityId;
        }

        return facilities;
    }



}

